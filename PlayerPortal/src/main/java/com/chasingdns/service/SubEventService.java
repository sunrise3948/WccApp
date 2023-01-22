package com.chasingdns.service;

import com.chasingdns.repository.EventRepository;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.*;
import com.chasingdns.repository.SubEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubEventService {

    @Autowired private SubEventRepository subEventRepository;
    @Autowired private TransactionService txnService;

    public void save(SubEvent subEvent) { subEventRepository.save(subEvent); }

    public List<SubEvent> listAll(){
        return (List<SubEvent>) subEventRepository.findAll();
    }

    public List<SubEvent> findByEvent(Event event){
        return (List<SubEvent>) subEventRepository.findByEvent(event);
    }
    public Page<SubEvent> findPaginated(SubEvent.EVENT_STATUS status, int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return subEventRepository.findAllByStatus(pageable, status);
    }

    public SubEvent get(Integer id) throws NotFoundException {
        Optional<SubEvent> subEvent = subEventRepository.findById(id);
        if(subEvent.isPresent()){
            return subEvent.get();
        }
        throw new NotFoundException("Could not find sub event with id" + id);
    }

    public void completeSubEvent(SubEvent subEvent){
        if(subEvent!=null && subEvent.getStatus() == SubEvent.EVENT_STATUS.IN_PROGRESS){
            subEvent.setStatus(SubEvent.EVENT_STATUS.COMPLETED);
            save(subEvent);
        }
    }
}
