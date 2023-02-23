package zhihui.backend.constant;


/**
 * 响应状态码
 * @author CHENPrime-Coder
 */
public enum ResponseStateConstant {

    /**
     * 请求处理成功
     */
    STATE_OK(200, "请求处理成功"),
    /**
     * 登陆失败或未登陆
     */
    STATE_UNAUTHORIZED(401, "登陆失败或未登陆"),
    /**
     * 服务器发生异常
     */
    STATE_SERVER_ERROR(500, "服务器发生异常");

    ResponseStateConstant(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

}
