package com.example.rewardservice.user.dto;

import com.example.rewardservice.user.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class UserDTO implements UserDetails {
    private UUID id;
    private String emailId;
    private String password;
    private String name;
    private String nickName;
    private long totalPoint;
    private String profileImageUrl;
    private LocalDateTime lastUpdateDate;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDTO(UUID id,String userEmail,String userPassword, String userName,
                   String userNickname, long userTotalPoint,
                   String profileImageUrl, LocalDateTime lastUpdateDate, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.emailId = userEmail;
        this.password = userPassword;
        this.name = userName;
        this.nickName = userNickname;
        this.totalPoint = userTotalPoint;
        this.profileImageUrl = profileImageUrl;
        this.lastUpdateDate = lastUpdateDate;
        this.authorities = authorities;
    }

    public UserDTO() {
    }


    public UserDTO(User user, Collection<GrantedAuthority> authorities) {
        this.id = user.getId();
        this.emailId = user.getEmailId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.nickName = user.getNickName();
        this.totalPoint = user.getTotalPoint();
        this.profileImageUrl = user.getProfileImageUrl();
        this.lastUpdateDate = user.getLastUpdateDate();
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
        return emailId;
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
