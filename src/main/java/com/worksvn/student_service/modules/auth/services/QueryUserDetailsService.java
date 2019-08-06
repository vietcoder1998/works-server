package com.worksvn.student_service.modules.auth.services;

import com.worksvn.student_service.modules.auth.models.dtos.CustomUserDetail;
import com.worksvn.student_service.modules.auth.models.entities.User;
import com.worksvn.student_service.modules.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QueryUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public List<GrantedAuthority> getGrantedAuthorities(User user) {
        return Collections.emptyList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserFetchRole(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetail(user.getId(), user.getUsername(), user.getPassword(),
                getGrantedAuthorities(user));
    }
}
