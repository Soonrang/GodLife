package com.example.rewardservice.user.application.dto;

import com.example.rewardservice.user.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@ToString
public class UserDto implements UserDetails {
    private UUID id;
    private String email;
    private String password;
    private String name;
    private String nickName;
    private long totalPoint;
    private String ImageFile;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDto(UUID id, String userEmail, String userPassword, String userName,
                   String userNickname, long userTotalPoint,
                   String ImageFile, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = userEmail;
        this.password = userPassword;
        this.name = userName;
        this.nickName = userNickname;
        this.totalPoint = userTotalPoint;
        this.ImageFile = ImageFile;
    }

    public UserDto() {
    }


    public UserDto(User user, Collection<GrantedAuthority> authorities) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.nickName = user.getNickname();
        this.totalPoint = user.getTotalPoint();
        this.ImageFile = user.getImageFile();
        this.authorities = authorities;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
