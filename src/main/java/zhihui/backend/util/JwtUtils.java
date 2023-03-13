package zhihui.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import zhihui.backend.pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt 工具类
 * @author CHENPrime-Coder
 */
@ConfigurationProperties("jwt-utils")
@Component
@Slf4j
public class JwtUtils {

    public Integer expireTime;
    public String key;

    public String create(User user) {
        Date date = new Date(System.currentTimeMillis() + expireTime * 1000 * 60 * 60);

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getUserId());
        payload.put("userEmail", user.getUserEmail());
        payload.put("userNameId", user.getUserNameId());
        payload.put("userName", user.getUsername());
        payload.put("userFaceUrl", user.getUserFaceUrl());
        payload.put("userMajor", user.getUserMajor());
        payload.put("userGrade", user.getUserGrade());

        return JWT.create().withPayload(payload)
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(key));
    }

    public Boolean verify(String token) {
        try {
            JWT.decode(token);
        } catch (JWTDecodeException e) {
            log.warn("jwt token 验证失败 token=" + token);
            return false;
        }

        return true;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
