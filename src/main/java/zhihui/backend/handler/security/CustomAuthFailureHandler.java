package zhihui.backend.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import zhihui.backend.constant.ResponseStateConstant;
import zhihui.backend.pojo.ResultData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登陆失败处理器
 * @author CHENPrime-Coder
 */
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 1. 填充返回值
        ResultData<Object> resultData = ResultData.error(ResponseStateConstant.STATE_UNAUTHORIZED.getCode(),
                ResponseStateConstant.STATE_UNAUTHORIZED.getMessage()+": "+exception.getMessage());

        // 2. 格式转换并返回
        String json = new ObjectMapper().writeValueAsString(resultData);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }
}
