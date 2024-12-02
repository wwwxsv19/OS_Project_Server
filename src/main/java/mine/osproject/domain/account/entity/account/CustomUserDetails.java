package mine.osproject.domain.account.entity.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    public String getUserName() {
        return user.getUserName();
    }

    public String getUserEmail() {
        return user.getUserEmail();
    }

    public String getUserImage() {
        return user.getUserImage();
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    // 사용 안 함

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
}
