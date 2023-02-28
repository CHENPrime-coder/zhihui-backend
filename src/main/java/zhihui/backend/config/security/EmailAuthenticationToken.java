package zhihui.backend.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义AuthenticationToken实现邮件验证
 * @author CHENPrime-Coder
 */
public class EmailAuthenticationToken extends AbstractAuthenticationToken {
    // 邮箱
    private final Object principal;

    private String vc = "";

    public EmailAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        super.setAuthenticated(false);
    }

    public EmailAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public String getVc() {
        return vc;
    }

    public void setVc(String vc) {
        this.vc = vc;
    }

    @Override
    public Object getCredentials() {
        return vc;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
