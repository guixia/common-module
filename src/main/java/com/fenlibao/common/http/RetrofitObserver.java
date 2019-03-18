package com.fenlibao.common.http;

import com.fenlibao.common.base.BaseActivity;
import com.fenlibao.common.model.CommonJson;
import com.fenlibao.common.utils.NetworkUtil;
import com.fenlibao.common.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class RetrofitObserver<T extends CommonJson> implements Observer<T> {

    private BaseActivity _activity;

    public RetrofitObserver(BaseActivity _activity) {
        this._activity = _activity;
    }

    public RetrofitObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();
    }

    @Override
    public void onNext(T json) {
        onRequestEnd();
        try {
            onSuccess(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        try {
            //回调
            onFailure(e);
            //网络异常在这里统一判断
            handleErrorMessage();
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
    }

    /**
     * 返回成功
     */
    protected abstract void onSuccess(T json);


    /**
     * 返回失败
     * isNetWorkError 是否是网络错误
     */
    protected abstract void onFailure(Throwable e);

    /**
     * 开始请求
     */
    private void onRequestStart() {


    }

    private void onRequestEnd() {


    }


    /**
     * 处理网络连接提示信息
     */
    private static void handleErrorMessage() {
        if (!NetworkUtil.isNetworkConnected()) {
            ToastUtil.showToast("网络异常,请稍后再试");
        }
    }

}
