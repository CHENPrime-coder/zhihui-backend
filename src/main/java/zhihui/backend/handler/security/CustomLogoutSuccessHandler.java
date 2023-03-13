package zhihui.backend.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义注销登陆处理器
 * @author CHENPrime
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserServiceImpl userService;

    @Autowired
    public CustomLogoutSuccessHandler(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 销毁 token
        String oldToken = request.getHeader("Token");
        if (StringUtils.hasText(oldToken)) {
            userService.destroyToken(oldToken);
        }


        // 1. 填充返回值
        ResultData<Object> resultData = ResultData.success(null);

        // 2. 格式转换并返回
        String json = new ObjectMapper().writeValueAsString(resultData);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }
}
