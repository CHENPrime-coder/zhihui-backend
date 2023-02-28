package zhihui.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zhihui.backend.filter.LoginFilter;
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

    @Autowired
    public SecurityConfig(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 处理需要认证的请求
        http.authorizeRequests(authz -> {
           authz.mvcMatchers("/email/send","/email/verify","/login","/reg").permitAll();

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
