package com.chasingdns.service;

import com.chasingdns.entity.AppUserDetails;
import com.chasingdns.entity.Player;
import com.chasingdns.entity.User;
import com.chasingdns.repository.AccountRepository;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.Account;
import com.chasingdns.repository.UserRepository;
import com.chasingdns.security.SecuredPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecuredPasswordGenerator pwdGenerator;

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateResetPasswordToken(String token, String email) throws NotFoundException {
        User user = userRepository.findByUsername(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new NotFoundException("Could not find any user with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(pwdGenerator.oneWayHash(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find any user with the username " + username);
        }
        return new AppUserDetails(user);
    }

}
