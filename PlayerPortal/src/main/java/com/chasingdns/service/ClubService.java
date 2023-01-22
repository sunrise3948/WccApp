package com.chasingdns.service;

import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.ClubAccount;
import com.chasingdns.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService {

    @Autowired private ClubRepository clubRepository;
    @Autowired private TransactionService txnService;

    public void save(ClubAccount ca) {
        clubRepository.save(ca);
    }

    public ClubAccount listClubAccount(){
        List<ClubAccount> account = (List<ClubAccount>) clubRepository.findAll();
        if(account!=null && !account.isEmpty())
            return account.get(0);
        return null;
    }

    public ClubAccount get(Integer id) throws NotFoundException {
        Optional<ClubAccount> account = clubRepository.findById(id);
        if(account.isPresent()){
            return account.get();
        }
        throw new NotFoundException("Could not find club with id" + id);
    }

}
