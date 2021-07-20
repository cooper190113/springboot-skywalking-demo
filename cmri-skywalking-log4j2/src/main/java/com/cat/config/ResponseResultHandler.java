package com.cat.config;
import lombok.extern.log4j.Log4j2;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import java.lang.reflect.Field;

@Log4j2
@ControllerAdvice
@ConditionalOnClass(LoggingAutoConfiguration.class)
public class ResponseResultHandler implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (mediaType.equals(mediaType.APPLICATION_JSON_UTF8)) {
            try {
                Field field = body.getClass().getDeclaredField("requestId");
                field.setAccessible(true);
                field.set(body, TraceContext.traceId());
            }catch (NoSuchFieldException e) {
            } catch (Throwable e) {
                log.warn("traceId inject failed,traceId:" + TraceContext.traceId(), e);
            }
        }
        return body;
    }
}
