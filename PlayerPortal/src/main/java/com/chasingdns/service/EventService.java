package com.chasingdns.service;

import com.chasingdns.repository.EventRepository;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired private EventRepository eventRepository;
    @Autowired private TransactionService txnService;
    @Autowired private SubEventService subEventService;

    public void save(Event event) { eventRepository.save(event); }

    public List<Event> listAll(){
        return (List<Event>) eventRepository.findAll();
    }

    public List<Event> listAllInProgressEvents(){
        return (List<Event>) eventRepository.findByStatus(Event.EVENT_STATUS.IN_PROGRESS);
    }

    public List<Event> listAllInProgressExternalEvents(){
        return (List<Event>) eventRepository.findByStatusAndMode(Event.EVENT_STATUS.IN_PROGRESS, Event.EVENT_MODE.EXTERNAL);
    }
    public Page<Event> findPaginated(Event.EVENT_STATUS status, int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return eventRepository.findAllByStatus(pageable, status);
    }

    public void completeExternalEvent(Event event){
        if(event.getMode().equals(Event.EVENT_MODE.EXTERNAL)
                && event.hasNoPendingTransactions()
                && event.getStatus() == Event.EVENT_STATUS.IN_PROGRESS){
            event.setStatus(Event.EVENT_STATUS.COMPLETED);
            for(SubEvent subEvent : subEventService.findByEvent(event)){
                if(subEvent.getStatus() == SubEvent.EVENT_STATUS.IN_PROGRESS){
                    subEvent.setStatus(SubEvent.EVENT_STATUS.COMPLETED);
                    subEventService.save(subEvent);
                }
            }
            save(event);
        }
    }


    public Event get(Integer id) throws NotFoundException {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isPresent()){
            return event.get();
        }
        throw new NotFoundException("Could not find event with id" + id);
    }
}
