package com.example.commonlibs.mvp.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;

import com.example.commonlibs.mvp.BasePresenter;
import com.example.commonlibs.mvp.IBaseActivity;


/**
 * Created by Horrarndoo on 2017/4/6.
 * <p>
 * Mvp Activity基类
 */
public abstract class BaseMVPCompatActivity<P extends BasePresenter> extends
        BaseCompatActivity implements IBaseActivity {
    /**
     * presenter 具体的presenter由子类确定
     */
    protected P mPresenter;
    private static final String TAG = "BaseMVPCompatActivity";

    /**
     * 初始化数据
     * <p>
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {
        super.initData();
        mPresenter = (P) initPresenter();
        if (mPresenter != null) {
            mPresenter.attachMV(this);
            Log.d(TAG, "attach M V success: ");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachMV();
            //Logger.d("detach M V success.");
        }
    }

    @Override
    public void showWaitDialog(String msg) {
    }

    @Override
    public void hideWaitDialog() {
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz) {
        startActivity(clz);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
        startActivity(clz, bundle);
    }

    @Override
    public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
        startActivityForResult(clz, bundle, requestCode);
    }

    @Override
    public void hideKeybord() {
        hiddenKeyboard();
    }

    @Override
    public void back() {
        super.onBackPressedSupport();
    }
}
