package com.chasingdns.repository;

import com.chasingdns.entity.Team;
import com.chasingdns.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends PagingAndSortingRepository<Team, Integer>, CrudRepository<Team, Integer> {

    Iterable <Team> findAll(Sort sort);

    Page<Team> findAll(Pageable pageable);
}
