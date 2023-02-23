package zhihui.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zhihui.backend.exception.EmailNotFoundException;
import zhihui.backend.mapper.UserDaoMapper;
import zhihui.backend.pojo.User;

/**
 * 用户服务
 * @author CHENPrime-Coder
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserDaoMapper userDaoMapper;

    @Autowired
    public UserServiceImpl(UserDaoMapper userDaoMapper) {
        this.userDaoMapper = userDaoMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDaoMapper.loadUserByUsername(username);

        // 1. 查不到用户
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户名不存在");
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

}
