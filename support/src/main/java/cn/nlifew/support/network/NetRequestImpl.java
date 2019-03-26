package cn.nlifew.support.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetRequestImpl implements NetRequest, Callback {

    private int mId;
    private String mUrl;
    private Object mTag;

    private Response mResponse;
    private Request.Builder mRequest;
    private OnNetResponseListener mCallback;

    private static OkHttpClient sClient;
    private static MediaType sFormType;

    static {
        sFormType = MediaType.parse("application/x-www-form-urlencoded");
        try {
            HostnameVerifier verifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            X509TrustManager x509 = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            sslContext.init(null, new TrustManager[] {x509},
                    new SecureRandom());
            sClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(verifier)
                    .build();
        } catch (Exception exp) {
            exp.printStackTrace();
            sClient = new OkHttpClient();
        }
    }

    public NetRequestImpl() {
        mRequest = new Request.Builder();
    }

    @Override
    public int id() {
        return mId;
    }

    @Override
    public String url() {
        return mUrl;
    }

    @Override
    public Object tag() {
        return mTag;
    }

    @Override
    public NetRequestImpl id(int id) {
        mId = id;
        return this;
    }

    @Override
    public NetRequestImpl url(String url) {
        mUrl = url;
        mRequest.url(url);
        return this;
    }

    @Override
    public NetRequestImpl tag(Object tag) {
        mTag = tag;
        return this;
    }

    @Override
    public NetRequestImpl head(String key, String value) {
        mRequest.header(key, value);
        return this;
    }

    @Override
    public NetRequestImpl remove(String key) {
        mRequest.removeHeader(key);
        return this;
    }

    @Override
    public Reader reader() {
        return mResponse.body().charStream();
    }

    @Override
    public InputStream input() {
        return mResponse.body().byteStream();
    }

    @Override
    public String string() throws IOException {
        return mResponse.body().string();
    }

    @Override
    public NetRequestImpl get() throws IOException {
        mResponse = sClient
                .newCall(mRequest.get().build())
                .execute();
        return this;
    }

    @Override
    public NetRequestImpl post(String text) throws IOException {
        RequestBody body = RequestBody.create(sFormType, text);
        mResponse = sClient
                .newCall(mRequest.post(body).build())
                .execute();
        return this;
    }

    @Override
    public NetRequestImpl get(OnNetResponseListener callback) {
        mCallback = callback;
        sClient.newCall(mRequest.get().build())
                .enqueue(this);
        return this;
    }

    @Override
    public NetRequestImpl post(String text, OnNetResponseListener callback) {
        mCallback = callback;
        RequestBody body = RequestBody.create(sFormType, text);
        sClient.newCall(mRequest.post(body).build())
                .enqueue(this);
        return this;
    }

    @Override
    public void close() {
        if (mResponse != null) {
            ResponseBody body = mResponse.body();
            if (body != null) {
                body.close();
            }
            mResponse.close();
        }
        mRequest = null;
        mResponse = null;
        mCallback = null;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (mCallback != null) {
            mCallback.onNetFailed(this, e);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (mCallback != null) {

            try {
                mCallback.onNetResponse(this);
            } catch (Exception exp) {
                mCallback.onNetFailed(this, exp);
            }
        }
    }
}
