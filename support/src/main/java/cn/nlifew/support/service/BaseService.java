package cn.nlifew.support.service;

import android.app.Service;
import android.content.Intent;

import cn.nlifew.support.task.EHandler;

public abstract class BaseService extends Service {

    private boolean mAlive;
    private EHandler mEHandler;

    @Override
    public void onCreate() {
        mAlive = true;
        super.onCreate();
        if (mEHandler != null) {
            mEHandler.onResume(this);
        }
    }

    @Override
    public void onDestroy() {
        mAlive = false;
        super.onDestroy();
        if (mEHandler != null) {
            mEHandler.onDestroy();
        }
    }

    public EHandler getEHandler() {
        return mEHandler;
    }

    public void setEHandler(EHandler handler) {
        if (mEHandler != null) {
            mEHandler.onPause();
            mEHandler.onDestroy();
        }
        this.mEHandler = handler;
        if (mAlive) {
            handler.onResume(this);
        } else {
            handler.onPause();
        }
    }
}
