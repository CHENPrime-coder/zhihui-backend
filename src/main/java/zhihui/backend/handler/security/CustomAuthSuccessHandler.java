package zhihui.backend.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.pojo.User;
import zhihui.backend.service.UserServiceImpl;
import zhihui.backend.util.JwtUtils;

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
@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserServiceImpl userService;

    @Autowired
    public CustomAuthSuccessHandler(JwtUtils jwtUtils, UserServiceImpl userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String oldToken = request.getHeader("Token");
        // 如果之前是登陆状态时再次登陆，就销毁旧的 token
        if (StringUtils.hasText(oldToken)) {
            userService.destroyToken(oldToken);
        }

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

        result.put("token", jwtUtils.create(user));

        ResultData<Map<String, Object>> resultData = ResultData.success(result);

        // 2. 格式转换并返回
        String json = new ObjectMapper().writeValueAsString(resultData);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }
}
