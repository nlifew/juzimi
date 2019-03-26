package cn.nlifew.support.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface NetRequest {

    int id();
    String url();
    Object tag();

    NetRequest id(int id);
    NetRequest url(String url);
    NetRequest tag(Object tag);
    NetRequest head(String key, String value);
    NetRequest remove(String key);

    Reader reader();
    InputStream input();
    String string() throws IOException;

    NetRequest get() throws IOException;
    NetRequest post(String text) throws IOException;

    NetRequest get(OnNetResponseListener callback);
    NetRequest post(String text, OnNetResponseListener callback);

    void close();
}
