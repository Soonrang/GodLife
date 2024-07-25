package com.example.rewardservice.user.application.dto;

import com.example.rewardservice.user.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@ToString
public class APIUserDetailDto implements UserDetails {
    private UUID id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private long totalPoint;
    private String profileImage;
    private Collection<? extends GrantedAuthority> authorities;

    public APIUserDetailDto(UUID id, String userEmail, String userPassword, String userName,
                            String userNickname, long userTotalPoint,
                            String profileImage, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = userEmail;
        this.password = userPassword;
        this.name = userName;
        this.nickname = userNickname;
        this.totalPoint = userTotalPoint;
        this.profileImage = profileImage;
        this.authorities = authorities;
    }

    public APIUserDetailDto() {
    }


    public APIUserDetailDto(User user, Collection<GrantedAuthority> authorities) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.totalPoint = user.getTotalPoint();
        this.profileImage = user.getProfileImage();
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
