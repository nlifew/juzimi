package com.ablist97.xjuzimi.network;

import android.util.Log;

import com.ablist97.xjuzimi.bean.BeanSupport;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Converter;

class ResponseConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "ResponseConverter";

    ResponseConverter(Type type) {
        mType = type;
    }

    private final Type mType;

    @Override
    public T convert(ResponseBody value) throws IOException {
        BeanSupport.HtmlCreator creator = findCreator(mType);
        if (creator == null) {
            return null;
        }

        String s = value.string();

        return create(creator, mType, s);
    }

    private static BeanSupport.HtmlCreator findCreator(Type type) {
        Class clazz;

        if (type instanceof GenericArrayType) {     // 数组类型
            GenericArrayType at = (GenericArrayType) type;
            clazz = (Class) at.getGenericComponentType();
        }
        else if (type instanceof ParameterizedType) {    // 泛型类型
            ParameterizedType pt = (ParameterizedType) type;
            clazz = (Class) pt.getActualTypeArguments()[0];
        }
        else if (type instanceof Class) {
            clazz = (Class) type;
        }
        else {
            Log.w(TAG, "findCreator: unknown type: " + type);
            return null;
        }

        BeanSupport.HtmlCreator htmlCreator;

        try {
            Field field = clazz.getField("HTML_CREATOR");
            htmlCreator = (BeanSupport.HtmlCreator) field.get(null);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "findCreator: no HTML_CREATOR found");
            return null;
        } catch (IllegalAccessException e) {
            Log.w(TAG, "findCreator: HTML_CREATOR should be public", e);
            return null;
        }
        if (htmlCreator == null) {
            Log.w(TAG, "findCreator: HTML_CREATOR should be nonnull");
            return null;
        }
        return htmlCreator;
    }

    @SuppressWarnings("unchecked")
    private T create(BeanSupport.HtmlCreator creator, Type type, String s) {
        if (type instanceof Class) {
            return (T) creator.create(s);
        }
        if (type instanceof GenericArrayType) {
            return (T) creator.createArray(s);
        }
        ParameterizedType pt = (ParameterizedType) type;
        Class clazz = (Class) pt.getRawType();

        if (clazz == List.class) {
            return (T) Arrays.asList(creator.createArray(s));
        }
        if (clazz == ArrayList.class) {
            return (T) new ArrayList(Arrays.asList(creator.createArray(s)));
        }
        if (clazz == LinkedList.class) {
            return (T) new LinkedList(Arrays.asList(creator.createArray(s)));
        }
        Log.w(TAG, "create: unknown type: " + type);
        return null;
    }
}
