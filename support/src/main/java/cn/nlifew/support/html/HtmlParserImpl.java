package cn.nlifew.support.html;

import android.util.Log;

import java.io.IOException;
import java.io.Reader;

/**
 * 基于字符串流的 Html 解析工具类
 * 由于句子迷没有公开的 API，只能从网页中抓取特征信息
 * 然而 JSoup 一类的 DOM 解析方式消耗内存比较大，
 * PullParser 在遇到 <br> 或 <br/> 时直接抛异常，
 * 索性直接造一个轮子。
 * 需要注意的是：由于每次以 tag 为单位进行解析，
 * 函数会被频繁访问，因此要求效率足够高
 */

public class HtmlParserImpl implements HtmlParser {
    private static final String TAG = "HtmlParserImpl";

    /**
     * 解析工具类，实际上是类似于 BufferedReader 和 StringBuilder.
     * 测试证明相比于调用 StringBuilder 的 API，效率的确有提升
     */
    private static final class Helper {

        private char[] mIOBuffer;
        private int mIOBuffPos;
        private int mIOBuffLen;

        private char[] builder;
        private int length;

        private Reader mReader;

        Helper(Reader reader) {
            mReader = reader;

            mIOBuffPos = 0;
            mIOBuffLen = 0;
            mIOBuffer = new char[1024];

            length = 0;
            builder = new char[1024];
        }

        /**
         * 用于将 mIOBuffer 中的字符串复制到 mBuilder 中
         * @param offset mIOBuffer 中复制的起始位置
         * @param count 复制的字符数
         */
        void append(int offset, int count) {
            int need = length + count;
            if (need > builder.length) {
                char[] alloc = new char[Math.max(need,
                        builder.length + (builder.length >> 1))];
                System.arraycopy(builder, 0, alloc, 0, builder.length);
                builder = alloc;
            }
            System.arraycopy(mIOBuffer, offset, builder, length, count);
            length += count;
        }

        /**
         * 直接在字符串流中寻找字符串
         * @param ch 待寻找的字符串
         * @param copy 是否复制到 mBuilder 中
         * @return 是否读到流的末尾
         * @throws IOException 不处理异常
         */
        boolean find(char ch, boolean copy) throws IOException {
            if (mIOBuffPos > mIOBuffLen - 1) {
                mIOBuffPos = 0;
                mIOBuffLen = mReader.read(mIOBuffer);
                if (mIOBuffLen == -1) return false;
            }

            length = 0;
            int start = mIOBuffPos;

            while (mIOBuffer[mIOBuffPos] != ch) {
                if (++mIOBuffPos > mIOBuffLen - 1) {
                    if (copy) append(start, mIOBuffPos - start);
                    start = 0;
                    mIOBuffPos = 0;
                    mIOBuffLen = mReader.read(mIOBuffer);
                    if (mIOBuffLen == -1) return false;
                }
            }
            if (copy) append(start, mIOBuffPos - start + 1);
            return true;
        }

        /**
         * 在 mBuilder 中寻找特征字符串
         * @param offset 查找的起始位置
         * @param s 待查找的字符串
         * @return 字符串的首次出现位置。未发现则返回 -1
         */
        int indexOf(int offset, String s) {
            int end = length - s.length();
            if (end < 0) {
                return -1;
            }
            int j, count = s.length();
            for (int i = offset; i < end; i++) {
                for (j = 0; j < count; j++) {
                    if (builder[j + i] != s.charAt(j)) {
                        break;
                    }
                }
                if (j == count) {
                    return i;
                }
            }
            return -1;
        }

        int indexOf(int offset, char ch) {
            for (int i = offset; i < length; i++) {
                if (builder[i] == ch) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 在流中跳过制定字符数
         * @param len 跳过的字符串长度
         * @throws IOException 不处理异常
         */
        void skip(int len) throws IOException {
            int left = mIOBuffLen - mIOBuffPos;
            while (mIOBuffLen != -1 && len >= left) {
                len -= left;
                mIOBuffPos = 0;
                mIOBuffLen = mReader.read(mIOBuffer);
                left = mIOBuffLen - mIOBuffPos;
            }
            if (mIOBuffLen != -1) {
                mIOBuffPos += len;
            }
        }
    }

    private int mType;
    private String mName;
    private String mText;
    private Helper mHelper;

    public HtmlParserImpl(Reader reader) {
        mHelper = new Helper(reader);
    }

    @Override
    public int next() throws IOException {
        if (mType == TYPE_START_TAG
                && mHelper.builder[mHelper.length - 2] == '/') {
            mType = TYPE_END_TAG;
        } else {
            mName = null;
            mText = null;

            mHelper.find('<', false);
            if (! mHelper.find('>', true)) {
                mType = TYPE_END_DOCUMENT;
            } else if (mHelper.builder[1] == '/') {
                mType = TYPE_END_TAG;
            } else {
                mType = TYPE_START_TAG;
            }
        }
        return mType;
    }

    @Override
    public String text() throws IOException {
        mText = mHelper.find('<', true) ?
                new String(mHelper.builder, 1, mHelper.length - 2) :
                "";
        return mText;
    }

    @Override
    public void skip(int len) throws IOException {
        mHelper.skip(len);
    }

    @Override
    public String name() {
        if (mName != null) {
            return mName;
        }

        char[] buff = mHelper.builder;
        int len = mHelper.length;

        if (buff[1] == '/') {
            mName = new String(buff, 2, len - 3);
            return mName;
        }

        char ch;
        int index = 1;
        while (index < len && (ch = buff[index++]) != ' '
                && ch != '/' && ch != '\n');

        mName = new String(buff, 1, index - 2);
        return mName;
    }

    @Override
    public String value(String key) {
        int index = mHelper.indexOf(3, key);
        int keyLength = key.length();
        char[] buff = mHelper.builder;

        char c, ch;

        while (index != -1) {
            if (buff[index - 1] != ' ') {
                index += keyLength;
                index = mHelper.indexOf(index, key);
                continue;
            }
            index += keyLength;
            c = buff[index++];
            ch = buff[index++];
            if (c == '=' && (ch == '"' || ch == '\'')) {
                int end = mHelper.indexOf(index, ch);
                if (end != -1) return new String(buff, index, end - index);
            }
        }
        return null;
    }
}
