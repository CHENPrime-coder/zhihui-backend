package zhihui.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import zhihui.backend.constant.ResponseStateConstant;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检测是否登陆的拦截器
 * @author CHENPrime-Coder
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final UserServiceImpl userService;

    @Autowired
    public LoginInterceptor(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) throws Exception {
        String token = request.getHeader("Token");
        Boolean verify = userService.checkToken(token);
        if (verify == null || !verify) {
            // 1. 填充返回值
            ResultData<Object> resultData = ResultData.success("Token验证失败，登陆已过期", null);

            // 2. 格式转换并返回
            String json = new ObjectMapper().writeValueAsString(resultData);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(json);
            return false;
        }

        return true;
    }
}
