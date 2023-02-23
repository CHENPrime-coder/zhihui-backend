package zhihui.backend.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户角色枚举类
 * @author CHENPrime-Coder
 */
@Slf4j
public enum UserStateConstant {

    /**
     * 普通用户
     */
    USER(0, "user"),
    /**
     * 管理员
     */
    MANAGER(1, "manager"),
    /**
     * 超级管理员
     */
    ADMIN(2, "admin");

    UserStateConstant(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static UserStateConstant of(Integer code) {
        for (UserStateConstant userState : UserStateConstant.values()) {
            if (userState.code.equals(code)) {
                return userState;
            }
        }

        log.error("未找到用户角色，默认为普通用户");
        return UserStateConstant.USER;
    }

    private Integer code;
    private String info;

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
