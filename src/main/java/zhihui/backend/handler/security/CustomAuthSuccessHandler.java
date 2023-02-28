package zhihui.backend.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.pojo.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登陆成功处理器
 * @author CHENPrime-Coder
 */
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 1. 填充返回值
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> result = new HashMap<>(7);
        result.put("userId", user.getUserId());
        result.put("userEmail", user.getUserEmail());
        result.put("userNameId", user.getUserNameId());
        result.put("userName", user.getUsername());
        result.put("userFaceUrl", user.getUserFaceUrl());
        result.put("userMajor", user.getUserMajor());
        result.put("userGrade", user.getUserGrade());

        ResultData<Map<String, Object>> resultData = ResultData.success(result);

        // 2. 格式转换并返回
        String json = new ObjectMapper().writeValueAsString(resultData);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }
}
