package com.chasingdns.repository;

import com.chasingdns.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
    public Long countById(Integer id);
    public Optional<Account> findByPlayerId(Integer playerId);
}
