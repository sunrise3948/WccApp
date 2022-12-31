package com.chasingdns.service;

import com.chasingdns.entity.Player;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.repository.TeamRepository;
import com.chasingdns.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired private TeamRepository teamRepository;

    public void save(Team team) {
        teamRepository.save(team);
    }

    public List<Team> listAll(){
        return (List<Team>) teamRepository.findAll();
    }

    public Page<Team> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return teamRepository.findAll(pageable);
    }

    public Team get(Integer id) throws NotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if(team.isPresent()){
            return team.get();
        }
        throw new NotFoundException("Could not find team with id" + id);
    }

}
