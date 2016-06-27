package com.tekenable.tdsec2.auth;

import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

/**
 * Created by nbarrett on 21/06/2016.
 * Write loadUserByUsername method and return a new UserDetails user = new User("hectorg87", "123456",
 * Collections.singletonList(new GrantedAuthorityImpl("ROLE_USER")));
 */
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        //Write loadUserByUsername method and return a new UserDetails user = new User("hectorg87", "123456",
        //Collections.singletonList(new GrantedAuthorityImpl("ROLE_USER")));

        UserDetails user = new User("hectorg87", "123456", Collections.singletonList(new GrantedAuthorityImpl("ROLE_USER")));

        return user;
    }
}
