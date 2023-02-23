package zhihui.backend.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码校验异常处理
 * @author CHENPrime
 */
public class CaptchaNotMatchException extends AuthenticationException {
    public CaptchaNotMatchException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaNotMatchException(String msg) {
        super(msg);
    }
}