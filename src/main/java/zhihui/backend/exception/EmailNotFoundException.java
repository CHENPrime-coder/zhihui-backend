package zhihui.backend.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 邮箱未注册异常
 * @author CHENPrime-Coder
 */
public class EmailNotFoundException extends AuthenticationException {
    public EmailNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EmailNotFoundException(String msg) {
        super(msg);
    }
}
