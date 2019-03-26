package cn.nlifew.juzimi.network;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import cn.nlifew.juzimi.application.Juzimi;
import cn.nlifew.juzimi.bean.User;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.support.network.NetRequest;
import cn.nlifew.support.network.NetRequestImpl;
import cn.nlifew.support.task.ESyncTaskFactory;

public abstract class NetworkTask implements ESyncTaskFactory.ESyncInterface {

    public static String realUrl(String url) {
        if (url.startsWith("//")) {
            return  "https:" + url;
        } else if (url.startsWith("/")) {
            return  "https://m.juzimi.com" + url;
        }
        return url;
    }

    public static NetRequest get(String url) {
        Settings settings = Settings.getInstance(null);
        User user = settings.getUser();

        NetRequest request = new NetRequestImpl();
        request.url(realUrl(url))
                .head("Host", "m.juzimi.com")
                .head("Upgrade-Insecure-Requests", "1")
                .head("User-Agent", settings.getUserAgent())
                .head("juzimiappver", "2.0.9")
                .head("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .head("Accept-Language", "zh-CN,en-US;q=0.8")
                .head("X-Requested-With", "com.example.juzimi");
        return user != null && user.cookie != null ?
                request.head("Cookie", user.cookie) :
                request;
    }

    public static NetRequest post(String url) {
        Settings settings = Settings.getInstance(null);
        User user = settings.getUser();

        NetRequest request = new NetRequestImpl();
        request.url(realUrl(url))
                .head("Host", "m.juzimi.com")
                .head("Origin", "https://m.juzimi.com")
                .head("User-Agent", settings.getUserAgent())
                .head("Accept", "application/json, text/javascript, */*")
                .head("Accept-Language", "zh-CN,en-US;q=0.8")
                .head("X-Requested-With", "XMLHttpRequest");
        return user != null && user.cookie != null ?
                request.head("Cookie", user.cookie) :
                request;
    }

    public NetworkTask() {}

    public NetworkTask(String url) {
    }

    protected Reader dumpReader(Reader reader) throws IOException {
        File dir = Juzimi.getContext().getExternalCacheDir();
        File file = new File(dir, "dump.html");
        if (reader != null) {
            FileWriter fw = new FileWriter(file);
            char[] buff = new char[1024];
            int count;
            while ((count = reader.read(buff)) != -1) {
                fw.write(buff, 0, count);
            }
            fw.flush();
            fw.close();
            reader.close();
        }
        return new FileReader(file);
    }
}
