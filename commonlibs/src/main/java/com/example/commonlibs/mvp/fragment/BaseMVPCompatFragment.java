package com.example.commonlibs.mvp.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.commonlibs.mvp.BasePresenter;
import com.example.commonlibs.mvp.IBaseFragment;
import com.example.commonlibs.mvp.activity.BaseCompatActivity;

import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by Horrarndoo on 2017/9/6.
 * <p>
 * Mvp Fragment基类
 * <p>
 * 实现IBaseView方法、绑定butterknife
 */

public abstract class BaseMVPCompatFragment<P extends BasePresenter> extends
        BaseCompatFragment implements IBaseFragment {
    public P mPresenter;

    /**
     * 在监听器之前把数据准备好
     */
    public void initData() {
        super.initData();

        mPresenter = (P) initPresenter();
        if (mPresenter != null) {
            mPresenter.attachMV(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachMV();
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
    public void back() {
        this.onBackPressedSupport();
    }

    @Override
    public void startNewFragment(@NonNull SupportFragment supportFragment) {
        start(supportFragment);
    }

    @Override
    public void startNewFragmentWithPop(@NonNull SupportFragment supportFragment) {
        startWithPop(supportFragment);
    }

    @Override
    public void startNewFragmentForResult(@NonNull SupportFragment supportFragment, int
            requestCode) {
        startForResult(supportFragment, requestCode);
    }

    @Override
    public void popToFragment(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        popTo(targetFragmentClass, includeTargetFragment);
    }

    @Override
    public void hideKeybord() {
        hideSoftInput();
    }

    @Override
    public void setOnFragmentResult(int ResultCode, Bundle data) {
        setFragmentResult(ResultCode, data);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz) {
        ((BaseCompatActivity) mActivity).startActivity(clz);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
        ((BaseCompatActivity) mActivity).startActivity(clz, bundle);
    }

    @Override
    public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
        ((BaseCompatActivity) mActivity).startActivityForResult(clz, bundle, requestCode);
    }

    @Override
    public boolean isVisiable() {
        return isSupportVisible();
    }

    @Override
    public Activity getBindActivity() {
        return mActivity;
    }
}