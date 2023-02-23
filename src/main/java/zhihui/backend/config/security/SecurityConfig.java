package zhihui.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zhihui.backend.filter.LoginFilter;
import zhihui.backend.handler.security.CustomAuthFailureHandler;
import zhihui.backend.handler.security.CustomAuthSuccessHandler;
import zhihui.backend.handler.security.CustomLogoutSuccessHandler;

/**
 * SpringSecurity 自定义配置
 * @author CHENPrime-Coder
 */
@Configuration
public class SecurityConfig {

    private final LoginFilter loginFilter;
    private final EmailAuthenticationProvider emailAuthenticationProvider;

    @Autowired
    public SecurityConfig(LoginFilter loginFilter, EmailAuthenticationProvider emailAuthenticationProvider) {
        this.loginFilter = loginFilter;
        this.emailAuthenticationProvider = emailAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 处理需要认证的请求
        http.authorizeRequests(authz -> {
           authz.mvcMatchers("/email/send","/login").permitAll();

           authz.anyRequest().authenticated();
        });

        // 登出请求配置
        http.logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(new CustomLogoutSuccessHandler());

        // 登陆配置
        http.formLogin();

        // 添加过滤器
        http.addFilterBefore(loginFilter,
                UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(emailAuthenticationProvider);

        // csrf 防御
        http.csrf().disable();

        return http.build();
    }

}
