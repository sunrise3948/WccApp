package com.chasingdns.repository;

import com.chasingdns.entity.ClubAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends CrudRepository<ClubAccount, Integer> {
}
