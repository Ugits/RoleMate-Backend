package org.jonas.rolemate_backend.config.security;

import org.jonas.rolemate_backend.user.model.entity.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    public CustomUserDetails(CustomUser customUser) {
        this.username = customUser.getUsername();
        this.password = customUser.getPassword();
        this.authorities = customUser.getAuthorities();
        this.isAccountNonExpired = customUser.isAccountNonExpired();
        this.isAccountNonLocked = customUser.isAccountNonLocked();
        this.isCredentialsNonExpired = customUser.isCredentialsNonExpired();
        this.isEnabled = customUser.isEnabled();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
