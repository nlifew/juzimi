package com.ablist97.xjuzimi.network;

import android.util.Log;

import com.ablist97.xjuzimi.BuildConfig;
import com.ablist97.xjuzimi.utils.Singleton;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public final class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    private NetworkUtils() {  }

    private static final Singleton<Retrofit> sRetrofit = new Singleton<Retrofit>() {
        @Override
        protected Retrofit create() {
            return createRetrofit();
        }

        private SSLSocketFactory sslFactory() {
            X509TrustManager x509 = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, new TrustManager[] {x509},
                        new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                Log.e(TAG, "createRetrofit: ", e);
                throw new UnsupportedOperationException(e);
            }
        }

        private Retrofit createRetrofit() {

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(new InterceptorImpl());

            if (BuildConfig.DEBUG) {
                builder.hostnameVerifier((hostname, session) -> true)
                        .sslSocketFactory(sslFactory());
            }

            Executor callbackExecutor = command -> {
                try {
                    command.run();
                } catch (Throwable e) {
                    String TAG = Thread.currentThread().getName();
                    Log.e(TAG, "execute: avoid to shutting down", e);
                }
            };

            return new Retrofit.Builder()
                    .baseUrl("http://m.shuoshuodaitupian.com/")
                    .client(builder.build())
                    .callbackExecutor(callbackExecutor)
                    .addConverterFactory(new HtmlConverter())
                    .build();
        }
    };



    private static final Map<Class<?>, Object> sCachedMap = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz) {
        T t = (T) sCachedMap.get(clazz);
        if (t == null) {
            synchronized (sCachedMap) {
                t = sRetrofit.get().create(clazz);
                sCachedMap.put(clazz, t);
            }
        }
        return t;
    }
}
