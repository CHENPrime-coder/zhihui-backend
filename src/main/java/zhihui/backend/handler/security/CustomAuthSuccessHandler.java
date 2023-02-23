package zhihui.backend.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import zhihui.backend.pojo.ResultData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登陆成功处理器
 * @author CHENPrime-Coder
 */
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 1. 填充返回值
        ResultData<Object> resultData = ResultData.success(null);

        // 2. 格式转换并返回
        String json = new ObjectMapper().writeValueAsString(resultData);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }
}
