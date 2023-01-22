package com.chasingdns.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecuredPasswordGenerator {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String oneWayHash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    /*public static void main(String args[]){
        System.out.println(encoder.encode("password1"));
    }*/

}
