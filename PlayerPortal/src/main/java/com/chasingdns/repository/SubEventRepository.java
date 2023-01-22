package com.chasingdns.repository;

import com.chasingdns.entity.Event;
import com.chasingdns.entity.SubEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubEventRepository extends PagingAndSortingRepository<SubEvent, Integer>, CrudRepository<SubEvent, Integer> {

    List<SubEvent> findByStatus(SubEvent.EVENT_STATUS status);

    List<SubEvent> findByEvent(Event event);

    Iterable <SubEvent> findAll(Sort sort);

    Page<SubEvent> findAll(Pageable pageable);

    Page<SubEvent> findAllByStatus(Pageable pageable, SubEvent.EVENT_STATUS status);
}
