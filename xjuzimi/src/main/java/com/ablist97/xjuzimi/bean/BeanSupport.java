package com.ablist97.xjuzimi.bean;

import android.os.Parcelable;

public class BeanSupport {

    public interface Creator<T, F> {
        T create(F f);
        T[] createArray(F f);
    }

    public interface HtmlCreator<T extends BeanSupport> extends Creator<T, String> {
        @Override
        T create(String s);

        @Override
        T[] createArray(String s);
    }
}
