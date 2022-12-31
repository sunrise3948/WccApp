package com.chasingdns.repository;

import com.chasingdns.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends PagingAndSortingRepository<Player, Integer>, CrudRepository<Player, Integer> {

    public Long countById(Integer id);
    public List<Player> findByStatus(Player.PLAYER_STATUS status);

    public Player findByEmail(String email);

    Iterable <Player> findAll(Sort sort);

    Page<Player> findAll(Pageable pageable);
}
