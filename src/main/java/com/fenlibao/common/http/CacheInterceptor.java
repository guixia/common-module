package com.fenlibao.common.http;

import android.text.TextUtils;

import com.fenlibao.common.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    /**
     * 无网络,设缓存有效期为一周
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 7;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        String cacheControl = request.cacheControl().toString();
        if ("GET".equals(request.method())) {
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            builder.url(urlBuilder.build());
        } else if ("POST".equals(request.method())) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                if (formBody != null) {
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                }
                addCommParams(bodyBuilder);
                builder.post(bodyBuilder.build());
            } else {//POST请求 无参数 未使用@FormUrlEncoded注解访问需求
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                addCommParams(bodyBuilder);
                builder.post(bodyBuilder.build());
            }
        } else if ("PUT".equals(request.method())) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            FormBody formBody = (FormBody) request.body();
            if (formBody != null) {
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }
            }
            addCommParams(bodyBuilder);
            builder.put(bodyBuilder.build());
        }
        //设置无网络时从缓存中取数据
        if (!NetworkUtil.isNetworkConnected()) {
            builder.cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_CACHE : CacheControl.FORCE_NETWORK);
        }
        Response originalResponse = chain.proceed(builder.build());
        if (!NetworkUtil.isNetworkConnected()) {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                    .removeHeader("Pragma")
                    .build();
        }
        return originalResponse;
    }

    /**
     * 增加公共参数
     */
    private void addCommParams(FormBody.Builder bodyBuilder) {
    }
}
