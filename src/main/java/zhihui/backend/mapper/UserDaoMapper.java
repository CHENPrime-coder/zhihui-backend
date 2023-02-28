package zhihui.backend.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import zhihui.backend.pojo.User;

/**
 * 用户处理
 * @author CHENPrime-Coder
 */
@Mapper
public interface UserDaoMapper {

    /**
     * 根据用户名返回查询到的用户
     *
     * @param username 用户名
     * @return 查询到的用户
     */
    User loadUserByUsername(String username);

    /**
     * 根据邮箱返回查询到的用户
     * @param email 邮箱
     * @return 查询到的用户
     */
    User loadUserByEmail(String email);

    /**
     * 插入用户表
     * @param user 用户
     * @return 插入结果
     */
    Integer insertUser(User user);
}
