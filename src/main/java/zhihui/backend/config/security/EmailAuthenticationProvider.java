package zhihui.backend.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import zhihui.backend.constant.CommonConstant;
import zhihui.backend.pojo.User;
import zhihui.backend.service.UserServiceImpl;

/**
 * 邮件认证提供者
 * @author CHENPrime-Coder
 */
@Slf4j
public class EmailAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 执行认证
     * @param authentication the authentication request object.
     * @return 认证结果
     * @throws AuthenticationException 认证异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("EmailCodeAuthentication authentication request: +"+authentication);
        EmailAuthenticationToken token = (EmailAuthenticationToken) authentication;

        // 验证码验证
        if (! checkCode((String) token.getPrincipal(), token.getVc())) {
            // 验证失败
            throw new AuthenticationServiceException("验证码错误");
        }

        // 认证成功，获取用户信息
        User user = (User) userService.loadUserByEmail((String) token.getPrincipal());

        if (user == null) {
            throw new AuthenticationServiceException("无法获取用户信息");
        }
        if (user.getUserDeleteTime() != null) {
            throw new AuthenticationServiceException("用户已删除");
        }

        EmailAuthenticationToken result =
                new EmailAuthenticationToken(user, user.getAuthorities());

        // Details 中包含了 ip地址、 sessionId 等等属性 也可以存储一些自己想要放进去的内容
        result.setDetails(token.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 检查验证码
     * @param email 邮件
     * @param captcha 验证码
     * @return 检测结果
     */
    private Boolean checkCode(String email, String captcha) {
        Object code = redisTemplate.opsForValue().get(CommonConstant.EMAIL_VC_PREFIX + email);
        if (code != null) {
            return code.toString().equals(captcha);
        }

        return false;
    }
}
