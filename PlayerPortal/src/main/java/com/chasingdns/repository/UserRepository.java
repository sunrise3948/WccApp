package com.chasingdns.repository;

import com.chasingdns.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    public Long countById(Integer id);

    public User findByUsername(String email);

    public User findByResetPasswordToken(String token);
    public Optional<User> findByPlayerId(Integer playerId);

}
