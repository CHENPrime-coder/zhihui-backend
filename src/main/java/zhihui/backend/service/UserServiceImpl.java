package zhihui.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zhihui.backend.exception.EmailNotFoundException;
import zhihui.backend.mapper.UserDaoMapper;
import zhihui.backend.pojo.ResultData;
import zhihui.backend.pojo.User;
import zhihui.backend.util.RsaUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

/**
 * 用户服务
 * @author CHENPrime-Coder
 */
@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    private final UserDaoMapper userDaoMapper;
    private final RsaUtils rsaUtils;

    @Autowired
    public UserServiceImpl(UserDaoMapper userDaoMapper, RsaUtils rsaUtils) {
        this.userDaoMapper = userDaoMapper;
        this.rsaUtils = rsaUtils;
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

    public ResultData<String> insertUser(User user) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
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
            // 邮箱重复
            log.error("用户注册邮箱重复 email: "+user.getUserEmail());

            return ResultData.success("用户注册邮箱重复");
        }

        // 用户注册失败
        if (integer != 1) {
            log.error("用户注册失败 email: "+user.getUserEmail());
            return ResultData.success("注册失败");
        }
        // 注册成功
        return ResultData.success("注册成功");
    }
}
