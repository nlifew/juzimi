package cn.nlifew.support;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.nlifew.support.task.EHandler;

public class BaseActivity extends AppCompatActivity {

    public interface OnPermRequestListener {
        void onPermGranted(String perm);
        void onPermDenied(String perm);
    }
    private Map<String, OnPermRequestListener> mPermMap;
    public void tryToRequestPerm(String perm, OnPermRequestListener callback) {
        if (ContextCompat.checkSelfPermission(this, perm)
                == PackageManager.PERMISSION_GRANTED) {
            callback.onPermGranted(perm);
            return;
        }
        if (shouldShowRequestPermissionRationale(perm)) {
            callback.onPermDenied(perm);
            return;
        }
        if (mPermMap == null) {
            mPermMap = new HashMap<>(4);
        }
        mPermMap.put(perm, callback);
        requestPermissions(new String[] {perm}, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermRequestListener callback = mPermMap.remove(permissions[0]);
        if (callback != null) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callback.onPermGranted(permissions[0]);
            } else {
                callback.onPermDenied(permissions[0]);
            }
        }
    }

    private boolean mActive = false;
    private EHandler mEHandler;

    @Override
    protected void onPause() {
        super.onPause();
        mActive = false;
        if (mEHandler != null) {
            mEHandler.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActive = true;
        if (mEHandler != null) {
            mEHandler.onResume(this);
        }
    }

    @Override
    protected void onDestroy() {
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
            throw new RuntimeException("A lifecycle object has only one EHandler.");
        }
        mEHandler = handler;
        if (mActive) {
            mEHandler.onResume(this);
        } else {
            mEHandler.onPause();
        }
    }
}
