package zhihui.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 随机生成验证码的工具类
 * @author CHENPrime-Coder
 */
@Component
@ConfigurationProperties(prefix = "random-utils")
public class RandomUtils {

    private String sed;

    private Random random = new Random();

    private Short length;

    public String getSed() {
        return sed;
    }

    public void setSed(String sed) {
        this.sed = sed;
    }

    public Short getLength() {
        return length;
    }

    public void setLength(Short length) {
        this.length = length;
    }

    public String randomVerifyCode() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(sed.length());
            result.append(sed.charAt(index));
        }

        return result.toString();
    }

}
