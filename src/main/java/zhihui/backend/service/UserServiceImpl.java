package zhihui.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zhihui.backend.exception.EmailNotFoundException;
import zhihui.backend.mapper.UserDaoMapper;
import zhihui.backend.pojo.User;
import zhihui.backend.util.JwtUtils;
import zhihui.backend.util.RsaUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务
 * @author CHENPrime-Coder
 */
@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    private final UserDaoMapper userDaoMapper;
    private final RsaUtils rsaUtils;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserDaoMapper userDaoMapper, RsaUtils rsaUtils, RedisTemplate redisTemplate, JwtUtils jwtUtils) {
        this.userDaoMapper = userDaoMapper;
        this.rsaUtils = rsaUtils;
        this.redisTemplate = redisTemplate;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameId) throws UsernameNotFoundException {
        User user = userDaoMapper.loadUserByUsernameId(usernameId);

        // 1. 查不到用户
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 2. 返回
        return user;
    }

    public UserDetails loadUserByEmail(String email) {
        User user = userDaoMapper.loadUserByEmail(email);

        // 1. 查不到用户
        if (ObjectUtils.isEmpty(user)) {
            throw new EmailNotFoundException("邮箱未注册");
        }

        // 2. 返回
        return user;
    }

    public String insertUser(User user) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        user.setUserNameId("zhihui_"+uuid);

        String decodedPassword = rsaUtils.decrypt(user.getUserPassword());

        // 密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode(decodedPassword);
        user.setUserPassword("{bcrypt}"+encodedPassword);

        // 执行插入用户操作
        Integer integer;
        try {
            integer = userDaoMapper.insertUser(user);
        } catch (DuplicateKeyException e) {
            return "注册失败, 邮件或用户名已重复";
        }


        // 用户注册失败
        if (integer != 1) {
            log.error("用户注册失败 email: "+user.getUserEmail());
            return "注册失败";
        }
        // 注册成功
        return "注册成功";
    }

    /**
     * 验证邮箱是否重复
     * @param addr 邮箱
     * @return 返回 true 就是没重复，反之就重复了
     */
    public Boolean checkEmail(String addr) {
        Boolean result = userDaoMapper.selectEmail(addr) != 1;

        return result;
    }

    /**
     * 把 token 存放到 redis 的 token 黑名单中，实现销毁token
     * @param token 要销毁的 token
     */
    public void destroyToken(String token) {
        redisTemplate.opsForValue().set("token-blacklist:"+token.hashCode(), token, jwtUtils.expireTime, TimeUnit.HOURS);
    }

    /**
     * 验证 token 是否有效
     * @param token 要验证的 token
     * @return 验证结果 false: 验证失败 true: 验证成功
     */
    public Boolean checkToken(String token) {
        String redisToken = (String) redisTemplate.opsForValue().get("token-blacklist:" + token.hashCode());

        if (redisToken != null) {
            return ! token.equals(redisToken);
        }
        return jwtUtils.verify(token);
    }
}
