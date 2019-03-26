package cn.nlifew.support;

import java.io.IOException;
import java.io.Reader;

public final class TextUtils {

    private TextUtils() {}
    private static final StringBuilder sBuilder = new StringBuilder(64);

    public static void replaceFirst(
            StringBuilder builder, String src, String dst) {
        int index = builder.indexOf(src);
        if (index != -1) builder.replace(index,
                index + src.length(), dst);
    }

    public static void replaceLastAfter(
            StringBuilder builder, String src, int len, String dst) {
        int index = builder.lastIndexOf(src);
        if (index == -1) return;
        index += src.length();
        builder.replace(index, index + len, dst);
    }

    public static void replaceLast(
            StringBuilder builder, String src, String dst) {
        int index = builder.lastIndexOf(src);
        if (index != -1) builder.replace(index,
                index + src.length(), dst);
    }

    public static void subBuilder(
            StringBuilder builder, String head, String tail) {
        int index = builder.indexOf(head);
        if (index != -1) builder.delete(0, index);
        index = builder.indexOf(tail);
        if (index != -1) builder.delete(index, builder.length());
    }

    public static String append(String s1, String s2) {
        synchronized (TextUtils.class) {
            sBuilder.setLength(0);
            return sBuilder.append(s1).append(s2).toString();
        }
    }

    public static String append(String s1, String s2, String s3) {
        synchronized (TextUtils.class) {
            sBuilder.setLength(0);
            return sBuilder.append(s1).append(s2).append(s3).toString();
        }
    }

    public static void readAll(StringBuilder builder, Reader reader)
        throws IOException {
        int count;
        char[] buff = new char[64];
        while ((count = reader.read(buff)) != -1)
            builder.append(buff, 0, count);
    }
}
