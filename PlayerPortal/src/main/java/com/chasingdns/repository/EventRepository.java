package com.chasingdns.repository;

import com.chasingdns.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Integer>, CrudRepository<Event, Integer> {

    List<Event> findByStatus(Event.EVENT_STATUS status);

    List<Event> findByStatusAndMode(Event.EVENT_STATUS status, Event.EVENT_MODE mode);

    Iterable <Event> findAll(Sort sort);

    Page<Event> findAll(Pageable pageable);

    Page<Event> findAllByStatus(Pageable pageable, Event.EVENT_STATUS status);
}
