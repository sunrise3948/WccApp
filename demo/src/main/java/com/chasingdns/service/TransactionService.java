package com.chasingdns.service;

import com.chasingdns.entity.Player;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.repository.TransactionRepository;
import com.chasingdns.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired private TransactionRepository txnRepository;
    @Autowired private AccountService accountService;

    public void save(Transaction txn) {
        txnRepository.save(txn);
    }

    public List<Transaction> listAll(){
        return (List<Transaction>) txnRepository.findAll();
    }

    public Page<Transaction> findPaginated(Transaction.TXN_STATUS status, int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return txnRepository.findAllByStatus(pageable, status);
    }
    public List<Transaction> listAllPendingTxns(){
        return (List<Transaction>) txnRepository.findByStatus(Transaction.TXN_STATUS.PENDING);
    }

    public List<Transaction> listAllApprovedTxns(){
        return (List<Transaction>) txnRepository.findByStatus(Transaction.TXN_STATUS.ACKNOWLEDGED);
    }

    public List<Transaction> listAllSettledTxns(){
        return (List<Transaction>) txnRepository.findByStatus(Transaction.TXN_STATUS.SETTLED);
    }

    public List<Transaction> listAllCanceledTxns(){
        return (List<Transaction>) txnRepository.findByStatus(Transaction.TXN_STATUS.CANCELED);
    }

    public Transaction get(Integer id) throws NotFoundException {
        Optional<Transaction> txn = txnRepository.findById(id);
        if(txn.isPresent()){
            return txn.get();
        }
        throw new NotFoundException("Could not find txn with id" + id);
    }
}
