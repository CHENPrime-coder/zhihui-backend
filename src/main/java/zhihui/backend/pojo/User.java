package zhihui.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zhihui.backend.constant.UserStateConstant;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户信息实体类 all_user
 * @author CHENPrime-Coder
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private Long userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userFaceUrl;
    private String userMajor;
    private Integer userGrade;
    private Integer userState;

    private Date userCreateTime;
    private Date userUpdateTime;
    private Date userDeleteTime;

    private Boolean userEnabled;
    private Boolean userAccountNonExpired;
    private Boolean userAccountNonLocked;
    private Boolean userCredentialsNonExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                UserStateConstant.of(userState).getInfo()
        );
        authorities.add(authority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return userAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return userEnabled;
    }
}
