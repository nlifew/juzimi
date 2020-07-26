package com.ablist97.xjuzimi.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public final class InterceptorImpl implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
