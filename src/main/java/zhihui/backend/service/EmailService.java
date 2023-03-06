package zhihui.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zhihui.backend.constant.CommonConstant;
import zhihui.backend.util.RandomUtils;

import java.util.concurrent.TimeUnit;

/**
 * 邮件服务
 * @author CHENPrime-Coder
 */
@Service
@Slf4j
@ConfigurationProperties(prefix = "email")
public class EmailService {

    private Boolean enable;
    private final JavaMailSender sender;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RandomUtils randomUtils;
    private Integer expireTime = 5;
    private String sendAs = "";

    @Autowired
    public EmailService(RedisTemplate redisTemplate, RandomUtils randomUtils, JavaMailSender javaMailSender) {
        this.redisTemplate = redisTemplate;
        this.randomUtils = randomUtils;
        this.sender = javaMailSender;
    }

    /**
     * 执行邮件的发送，并且把验证码保存到 redis
     * @param to 邮件发送给谁
     */
    public void sendVerifyEmail(String to) {
        // 如果没有配置以什么身份发送邮箱
        if (! StringUtils.hasText(sendAs)) {
            log.error("未配置邮件发送者账号");
            throw new IllegalArgumentException("未配置邮件发送者账号");
        }

        String code = randomUtils.randomVerifyCode();
        redisTemplate.opsForValue().set(CommonConstant.EMAIL_VC_PREFIX+to, code, expireTime, TimeUnit.MINUTES);

        if (enable) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sendAs);
            mailMessage.setTo(to);
            mailMessage.setSubject("知汇-邮箱验证码");
            mailMessage.setText("您好！\n" +
                    "注册验证码："+code+"。\n" +
                    "转给他人将导致ZhiHui帐号被盗和个人信息泄露，谨防诈骗，如非您操作请忽略。\n" +
                    "此致\n" +
                    "@ZhiHui团队");

            sender.send(mailMessage);
            log.info("邮件发送成功 to: "+to);
        } else {
            log.info("开发环境下，邮件未发送 code: "+code);
        }
    }

    /**
     * 邮件验证码验证，用于注册前验证 邮件验证码
     * @param email 邮件
     * @param code 前端传的验证码
     * @return 验证结果
     */
    public Boolean verifyCode(String email, String code) {
        Object realCode = redisTemplate.opsForValue().get(CommonConstant.EMAIL_VC_PREFIX + email);

        if (realCode == null) {
            return false;
        }

        return realCode.toString().equals(code);
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public void setSendAs(String sendAs) {
        this.sendAs = sendAs;
    }
}
