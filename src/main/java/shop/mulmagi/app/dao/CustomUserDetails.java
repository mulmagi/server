package shop.mulmagi.app.dao;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shop.mulmagi.app.domain.enums.UserStatus;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Getter
@Builder
public class CustomUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = 174726374856727L;

    private Long id;	// DB에서 PK 값
    private String phoneNumber;

    private boolean isAdmin;

    private Integer level;

    private Double experience;

    private Integer point;

    private String profileUrl;

    private Boolean isRental;

    private UserStatus status;

    private Boolean isComplaining;

    private boolean locked;	//계정 잠김 여부
    private String nickname;	//닉네임
    private Collection<GrantedAuthority> authorities;	//권한 목록

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();

        if (this.isAdmin) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorityList;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (this.status == UserStatus.ACTIVE)
            return true;
        else
            return false;
    }

}