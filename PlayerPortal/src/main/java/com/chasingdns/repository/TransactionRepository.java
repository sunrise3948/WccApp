package com.chasingdns.repository;

import com.chasingdns.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Integer>, CrudRepository<Transaction, Integer> {

    List<Transaction> findByStatus(Transaction.TXN_STATUS status);

    Iterable <Transaction> findAll(Sort sort);

    Page<Transaction> findAll(Pageable pageable);
    Page<Transaction> findAllByStatus(Pageable pageable, Transaction.TXN_STATUS status);

}
