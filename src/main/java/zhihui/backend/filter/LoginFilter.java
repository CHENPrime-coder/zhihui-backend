package zhihui.backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zhihui.backend.config.security.EmailAuthenticationToken;
import zhihui.backend.constant.LoginTypeConstant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    public LoginFilter() {
        this.setFilterProcessesUrl("/login");
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 1. 判断是否为 POST 方式
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 2. 判断是否以 JSON 格式
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            // 3. 从 JSON 中获取用户输入的用户名和密码
            Map<String, Object> userInfo = null;
            try {
                userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            } catch (IOException e) {
                throw new IllegalArgumentException("登陆请求参数获取失败："+e.getMessage());
            }

            String typeParam = (String) userInfo.get("type");
            LoginTypeConstant type = LoginTypeConstant.valueOf(typeParam);
            switch (type) {
                case EMAIL:
                    // 邮箱，验证码登陆
                    String email = (String) userInfo.get("email");
                    String captcha = (String) userInfo.get("captcha");

                    // 封装验证需要的东西
                    EmailAuthenticationToken emailAuthenticationToken = new EmailAuthenticationToken(email);
                    emailAuthenticationToken.setVc(captcha);

                    // 执行验证

                    return getAuthenticationManager().authenticate(emailAuthenticationToken);
                case CLASSIC:
                    // 用户名，密码登陆
                    String username = (String) userInfo.get(getUsernameParameter());
                    String password = (String) userInfo.get(getPasswordParameter());

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

                    // 执行验证
                    return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
                default:
                    log.error("登陆方式不存在 type: "+typeParam);
                    throw new IllegalArgumentException("登陆方式不存在");
            }
        }
        return super.attemptAuthentication(request, response);
    }

}
