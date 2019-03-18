package com.fenlibao.common.http;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if ("GET".equals(request.method()) || "DELETE".equals(request.method())) {
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
        return chain.proceed(builder.build());
    }

    /**
     * 增加公共参数
     */
    private void addCommParams(FormBody.Builder  bodyBuilder) {
    }
}
