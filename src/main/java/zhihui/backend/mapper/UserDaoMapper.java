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
     * @param usernameId 用户名 zhihui_xxxxxxxxxxxxxxxxxxxxx
     * @return 查询到的用户
     */
    User loadUserByUsernameId(String usernameId);

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

    /**
     * 检测邮箱重复
     * @param addr 邮箱
     * @return 重复的数量
     */
    Integer selectEmail(String addr);
}
