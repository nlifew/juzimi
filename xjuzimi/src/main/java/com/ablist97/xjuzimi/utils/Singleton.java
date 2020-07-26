package com.ablist97.xjuzimi.utils;

public abstract class Singleton<T> {

    private T mInstance;
    private boolean mShouldCreateInstance = true;

    public final T get() {
        if (mShouldCreateInstance) {
            synchronized (this) {
                if (mShouldCreateInstance) {
                    mInstance = create();
                    mShouldCreateInstance = false;
                }
            }
        }
        return mInstance;
    }

    protected abstract T create();

    @Deprecated
    public final void set(T t) {
        synchronized (this) {
            mInstance = t;
            mShouldCreateInstance = true;
        }
    }
}
