package zhihui.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zhihui.backend.filter.LoginFilter;
import zhihui.backend.handler.security.CustomAuthFailureHandler;
import zhihui.backend.handler.security.CustomAuthSuccessHandler;
import zhihui.backend.handler.security.CustomLogoutSuccessHandler;
import zhihui.backend.service.UserServiceImpl;

import java.util.Arrays;

/**
 * SpringSecurity 自定义配置
 * @author CHENPrime-Coder
 */
@Configuration
public class SecurityConfig {

    private final UserServiceImpl userService;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    public SecurityConfig(UserServiceImpl userService, CustomAuthSuccessHandler customAuthSuccessHandler, CustomAuthFailureHandler customAuthFailureHandler, CustomLogoutSuccessHandler logoutSuccessHandler) {
        this.userService = userService;
        this.customAuthSuccessHandler = customAuthSuccessHandler;
        this.customAuthFailureHandler = customAuthFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 处理需要认证的请求
        http.authorizeRequests(authz -> {
           authz.mvcMatchers("/email/send","/email/verify","/email/reg/send",
                   "/login","/reg").permitAll();

           authz.anyRequest().authenticated();
        });

        // 登出请求配置
        http.logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(logoutSuccessHandler);

        // 登陆配置
        http.formLogin();

        // 添加过滤器
        http.addFilterAt(loginFilter(),
                UsernamePasswordAuthenticationFilter.class);

        // 全局认证管理器
        http.authenticationManager(providerManager());

        // csrf 防御
        http.csrf().disable();

        return http.build();
    }

    @Bean
    public LoginFilter loginFilter() {
        LoginFilter filter = new LoginFilter();

        // 登陆成功处理器
        filter.setAuthenticationSuccessHandler(customAuthSuccessHandler);
        // 登陆失败处理器
        filter.setAuthenticationFailureHandler(customAuthFailureHandler);

        filter.setAuthenticationManager(providerManager());
        return filter;
    }

    @Bean
    public ProviderManager providerManager() {
        return new ProviderManager(
                Arrays.asList(emailAuthenticationProvider(), daoAuthenticationProvider())
        );
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public EmailAuthenticationProvider emailAuthenticationProvider() {
        return new EmailAuthenticationProvider();
    }

}
