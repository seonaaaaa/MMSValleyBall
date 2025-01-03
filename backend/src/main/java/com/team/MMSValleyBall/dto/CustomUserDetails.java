package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final Users users;

    public CustomUserDetails(Users users) {
        this.users = users;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        String role = users.getUserRole().toString();
        collection.add(new SimpleGrantedAuthority("ROLE_" +role));

        return collection;
    }

    @Override
    public String getPassword() {
        return users.getUserPassword();
    }

    @Override
    public String getUsername() {
        return users.getUserEmail();
    }

    public String getEmail() {
        return users.getUserEmail();
    }

    public String getName() {
        return users.getUserName();
    } // 사용자 이름 반환하는 메서드 추가

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
