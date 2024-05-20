package com.ngt.cuoiky.principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ngt.cuoiky.model.Role;
import com.ngt.cuoiky.model.User;

public class UserPrincipal implements UserDetails{
    private User user;

    public UserPrincipal(User user) {
        super();
        this.user = user;
    }
    public Integer getId() {
        return user.getId();
    }
    @Override
    public String getUsername() {
        return user.getEmail();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public boolean isEnabled() {
        return user.getIsActive();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName().toUpperCase())));
        }

        return authorities;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(user.getId(), that.getId());
    }


    @Override
    public int hashCode() {
        return Objects.hash(user.getId());
    }

    public String getFirstName() {return this.user.getFirstName();}

    public String getLastName() {return this.user.getLastName();}

    public String getFullName() {
        return this.user.getFirstName() + " " + this.user.getLastName();
    }

    
}
