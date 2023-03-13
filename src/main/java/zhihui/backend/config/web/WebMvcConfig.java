package zhihui.backend.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zhihui.backend.interceptor.LoginInterceptor;

/**
 * mvc 配置
 * @author CHENPrime-Coder
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    @Autowired
    public WebMvcConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 验证是否登陆
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/email/send","/email/verify","/email/reg/send",
                "/login","/reg");
    }
}
