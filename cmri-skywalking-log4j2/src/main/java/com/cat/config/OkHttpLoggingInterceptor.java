package com.cat.config;

import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Log4j2
public class OkHttpLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String requestBodyStr = null;
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            requestBodyStr = getRequestBody(requestBody);
        }
        log.info("Feign Request-> method: {}, url: {}, headers: {}, requestBody: {}", request.method(), request.url(),
                request.headers(), requestBodyStr);

        Response response = chain.proceed(request);
        String responseBodyStr = null;
        try {
            responseBodyStr = response.peekBody(Long.MAX_VALUE).string();
        } catch (IOException e) {
            log.error("Feign Response-> get response body exception");
        }
        log.info("Feign Response-> method: {}, url: {}, statusCode: {}, responseBody: {}", response.request().method(),
                response.request().url(), response.code(), responseBodyStr);
        return response;
    }

    private String getRequestBody(RequestBody requestBody) {
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
            Charset charset = StandardCharsets.UTF_8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(StandardCharsets.UTF_8);
            }
            return buffer.readString(charset);
        } catch (IOException e) {
            log.error("Feign Request-> get request body exception");
        }
        return null;
    }
}
