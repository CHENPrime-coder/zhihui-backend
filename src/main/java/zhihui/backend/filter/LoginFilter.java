package zhihui.backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import zhihui.backend.config.security.EmailAuthenticationProvider;
import zhihui.backend.config.security.EmailAuthenticationToken;
import zhihui.backend.constant.CommonConstant;
import zhihui.backend.constant.LoginTypeConstant;
import zhihui.backend.handler.security.CustomAuthFailureHandler;
import zhihui.backend.handler.security.CustomAuthSuccessHandler;
import zhihui.backend.util.SpringContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final EmailAuthenticationProvider emailAuthenticationProvider;

    private final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

    @Autowired
    public LoginFilter(EmailAuthenticationProvider emailAuthenticationProvider, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.emailAuthenticationProvider = emailAuthenticationProvider;
        setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());

        this.setFilterProcessesUrl("/login");
        // 登陆成功处理器
        this.setAuthenticationSuccessHandler(new CustomAuthSuccessHandler());
        // 登陆失败处理器
        this.setAuthenticationFailureHandler(new CustomAuthFailureHandler());
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 1. 判断是否为 POST 方式
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 2. 判断是否以 JSON 格式
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            // 3. 从 JSON 中获取用户输入的用户名和密码
            Map<String, Object> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);

            String typeParam = (String) userInfo.get("type");
            LoginTypeConstant type = LoginTypeConstant.valueOf(typeParam);
            switch (type) {
                case EMAIL:
                    // 邮箱，验证码登陆
                    String email = (String) userInfo.get("email");
                    String captcha = (String) userInfo.get("captcha");

                    // 封装验证需要的东西
                    EmailAuthenticationToken authenticationToken = new EmailAuthenticationToken(email);
                    authenticationToken.setVc(captcha);

                    // 执行验证
                    return getAuthenticationManager().authenticate(authenticationToken);
                case CLASSIC:
                    // 用户名，密码登陆
                    String username = (String) userInfo.get(getUsernameParameter());
                    String password = (String) userInfo.get(getPasswordParameter());

                    break;
                default:
                    log.error("登陆方式不存在 type: "+typeParam);
                    throw new IllegalArgumentException("登陆方式不存在");
            }
        }
        return super.attemptAuthentication(request, response);
    }

}
