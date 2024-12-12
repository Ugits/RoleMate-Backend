package org.jonas.rolemate_backend.user.authorites;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jonas.rolemate_backend.user.authorites.UserPermission.*;

public enum UserRole {

    USER(GET, POST),
    ADMIN(GET,POST,DELETE);

    private final List<String> permissions;

    UserRole(UserPermission... permissionList) {
        this.permissions = Arrays.stream(permissionList)
                .map(UserPermission::getPermission)
                .toList();
    }

    public List<String> getPermissions() { return permissions; }

    public List<SimpleGrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();

        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        simpleGrantedAuthorityList.addAll(getPermissions().stream().map(SimpleGrantedAuthority::new)
                .toList());

        return simpleGrantedAuthorityList;
    }
}