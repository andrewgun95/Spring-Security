package com.learn.security.service.impl;

import com.learn.security.entity.User;
import com.learn.security.repository.UserRepository;
import com.learn.security.security.UserLazyDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final String apiKey;
    private final String apiSecret;

    public UserServiceImpl(UserRepository userRepository, @Value("${api.key}") String apiKey, @Value("${api.secret}") String apiSecret) {
        this.userRepository = userRepository;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    // Invoke a method in the repository will automatically open session in hibernate (as a transaction) and after invocation done will close immediately
    // On fetch type Lazy the query only triggered when the field is accessed but the session was closed
    // Adding @Transactional and wrapped the UserDetails will prevent this behavior
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load Service  Account User Details
        if (username.equals(apiKey)) {
            return new org.springframework.security.core.userdetails.User(apiKey, //
                    String.format("{bcrypt}%s", apiSecret), //
                    Collections.singleton(new SimpleGrantedAuthority("SERVICE")) //
            );
        }
        // Load Database Account User Details
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        return new UserLazyDetails(user);
    }
}
