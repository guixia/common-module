package com.fenlibao.common.base;

import android.app.Application;

/**
 * 项目Application基类主项Application需集成此类1453
 */
public class BaseApplication extends Application {
    /**
     * 系统上下文
     */
    private static BaseApplication mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
    }

    /**
     * 获取系统上下文单例
     */
    public static BaseApplication getInstance() {
        return mAppContext;
    }
}
