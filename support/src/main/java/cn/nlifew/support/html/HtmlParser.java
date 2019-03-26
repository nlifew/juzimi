package cn.nlifew.support.html;

import java.io.IOException;

public interface HtmlParser {
    int TYPE_END_TAG = 0;
    int TYPE_START_TAG = 1;
    int TYPE_END_DOCUMENT = 2;
    int TYPE_START_DOCUMENT = 3;

    int next() throws IOException;
    String text() throws IOException;
    void skip(int len) throws IOException;

    String name();
    String value(String key);
}
