package zhihui.backend.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import zhihui.backend.pojo.ResultData;

/**
 * 全局返回值处理
 * @author CHENPrime-Coder
 */
@RestControllerAdvice
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 已经包装好了，不用处理
        if (body instanceof ResultData) {
            return body;
        }

        // 如果返回值是 String 就不处理直接返回
        if (body instanceof String) {
            return new ObjectMapper().writeValueAsString(body);
        }

        return ResultData.success(body);
    }
}
