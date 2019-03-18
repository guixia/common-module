package com.fenlibao.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fenlibao.common.base.BaseApplication;

public class NetworkUtil {
    /**
     * 判断网络访问是否正常
     */
    public static boolean isNetworkConnected() {
        BaseApplication application = BaseApplication.getInstance();
        if (application != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
        }
        return false;
    }


}
