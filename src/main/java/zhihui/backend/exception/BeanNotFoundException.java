package zhihui.backend.exception;

/**
 * Bean 未找到异常
 * @author CHENPrime-Coder
 */
public class BeanNotFoundException extends Exception {

    public BeanNotFoundException(String message) {
        super(message);
    }
}
