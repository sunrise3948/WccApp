package com.chasingdns.service;

import com.chasingdns.repository.AccountRepository;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired private AccountRepository accountRepository;
    @Autowired private PlayerService playerService;

    public void save(Account account) {
        accountRepository.save(account);
    }

    public Account getAccountByPlayerId(Integer playerId) throws NotFoundException {
        Optional<Account> account = accountRepository.findByPlayerId(playerId);
        if(account.isPresent()){
            return account.get();
        }
        throw new NotFoundException("Could not find account with player id" + playerId);
    }
}
