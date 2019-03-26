package cn.nlifew.support.fragment;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import cn.nlifew.support.BaseActivity;
import cn.nlifew.support.task.EHandler;

public class BaseFragment extends Fragment {


    private Map<String, BaseActivity.OnPermRequestListener> mPermMap;
    public void tryToRequestPerm(String perm, BaseActivity.OnPermRequestListener callback) {
        if (ContextCompat.checkSelfPermission(getContext(), perm)
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
        BaseActivity.OnPermRequestListener callback = mPermMap.remove(permissions[0]);
        if (callback != null) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callback.onPermGranted(permissions[0]);
            } else {
                callback.onPermDenied(permissions[0]);
            }
        }
    }



    private boolean mActive;
    private EHandler mEHandler;

    @Override
    public void onPause() {
        super.onPause();
        mActive = false;
        if (mEHandler != null) {
            mEHandler.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mActive = true;
        if (mEHandler != null) {
            mEHandler.onResume(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mActive = false;
        if (mEHandler != null) {
            mEHandler.onDestroy();
        }
    }

    public void setEHandler(EHandler handler) {
        if (mEHandler != null) {
            mEHandler.onPause();
            mEHandler.onDestroy();
        }
        mEHandler = handler;
        if (mActive) {
            mEHandler.onResume(this);
        } else {
            mEHandler.onPause();
        }
    }

    public EHandler getEHandler() {
        return mEHandler;
    }
}
