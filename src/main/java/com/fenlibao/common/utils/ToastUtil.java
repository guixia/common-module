package com.fenlibao.common.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fenlibao.arouter.common.R;
import com.fenlibao.common.base.BaseApplication;

public class ToastUtil {
    private static final int DEFAULT_DURATION = 2000;
    private static String lastToast = "";
    private Context mContext = BaseApplication.getInstance();
    private static long lastToastTime;
    private static Toast toast;
    private View toastView;
    private TextView tv_message;

    public static ToastUtil getInstance() {
        return new ToastUtil();
    }


    public static Toast showTextShort(Context context, CharSequence text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static Toast showTextShort(Context context, int resId) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static Toast showTextLong(Context context, CharSequence text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    public static Toast showTextLong(Context context, int resId) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }


    public static void showErrorCustomToast() {
        if (!NetworkUtil.isNetworkConnected()) {
            showToast("网络未连接,请检查网络");
        } else {
            showToast("系统繁忙,请稍后再试");
        }
    }

    /**
     * 连续点击不会重复显示Toast
     *
     * @param message 提示内容
     */
    public static void showToast(@NonNull String message) {
        try {
            showToast(message, Toast.LENGTH_SHORT, Gravity.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showToast(String message, int duration, int gravity) {
        if (message != null && !"".equalsIgnoreCase(message)) {
            long time = System.currentTimeMillis();
            if ((message.equalsIgnoreCase(lastToast) && Math.abs(time - lastToastTime) > 3000)
                    || Math.abs(time - lastToastTime) > DEFAULT_DURATION) {
                ToastUtil toastUtil = ToastUtil.getInstance();
                if (toast == null || toastUtil.toastView == null || toastUtil.tv_message == null) {
                    toastUtil.toastView = View.inflate(toastUtil.mContext, R.layout.custom_toast, null);
                    toastUtil.tv_message = (toastUtil.toastView.findViewById(R.id.tv_forToast));
                    toast = new Toast(toastUtil.mContext);
                    toast.setView(toastUtil.toastView);
                    toast.setGravity(gravity, 0, 0);
                    toast.setDuration(duration);
                }
                toastUtil.tv_message.setText(message);
                toast.show();
                lastToast = message;
                lastToastTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * 取消toast 显示
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
