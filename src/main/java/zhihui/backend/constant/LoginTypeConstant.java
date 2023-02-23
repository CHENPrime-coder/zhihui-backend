package zhihui.backend.constant;

/**
 * 登陆方式
 * @author CHENPrime-Coder
 */
public enum LoginTypeConstant {

    /**
     * 用户名，密码登陆
     */
    CLASSIC("classic", "用户名，密码登陆"),
    /**
     * 邮箱，验证码登陆
     */
    EMAIL("email", "邮箱，验证码登陆");

    LoginTypeConstant(String name, String info) {
        this.name = name;
        this.info = info;
    }

    private final String name;
    private final String info;

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }
}
