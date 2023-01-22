package com.chasingdns.service;

import com.chasingdns.entity.User;
import com.chasingdns.repository.AccountRepository;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.repository.PlayerRepository;
import com.chasingdns.entity.Account;
import com.chasingdns.entity.Player;
import com.chasingdns.repository.UserRepository;
import com.chasingdns.security.SecuredPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired private PlayerRepository playerRepo;
    @Autowired private AccountRepository accountRepository;
    @Autowired
    private SecuredPasswordGenerator pwdGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment env;

    public List<Player> listAll(){
        return (List<Player>) playerRepo.findAll();
    }

    public List<Player> listAllActive(){
        return (List<Player>) playerRepo.findByStatus(Player.PLAYER_STATUS.ACTIVE);
    }

    public Page<Player> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return playerRepo.findAll(pageable);
    }

    public void save(Player player) {
        Player savedPlayer = playerRepo.save(player);
        Optional<Account> existingAccount = accountRepository.findByPlayerId(savedPlayer.getId());
        if(!existingAccount.isPresent()){
            Account account = new Account();
            account.setPlayer(savedPlayer);
            account.setStartingBalance(BigDecimal.ZERO);
            account.setAvailableBalance(BigDecimal.ZERO);
            account.setCurrentBalance(BigDecimal.ZERO);
            accountRepository.save(account);
        }
        Optional<User> existingUser = userRepository.findByPlayerId(savedPlayer.getId());
        User user;
        if(!existingUser.isPresent()){
            user = new User();
            user.setPlayer(savedPlayer);
            user.setCreatedDate(new Date());
            user.setUpdatedDate(new Date());
            user.setType(User.USER_TYPE.EXTERNAL);
            user.setRole(User.USER_ROLE.USER);
            user.setUsername(savedPlayer.getEmail());
            user.setPassword(pwdGenerator.oneWayHash(env.getProperty("default.password")));
        } else {
            user = existingUser.get();
            user.setUsername(savedPlayer.getEmail());
        }
        userRepository.save(user);
    }

    public Player get(Integer id) throws NotFoundException {
        Optional<Player> player = playerRepo.findById(id);
        if(player.isPresent()){
            return player.get();
        }
        throw new NotFoundException("Could not find player with id" + id);
    }

    public Player findByUsername(String username)  {
        return playerRepo.findByEmail(username);
    }

    public void delete(Integer id) throws NotFoundException {
        Long count = playerRepo.countById(id);
        if(count>0){
            playerRepo.deleteById(id);
        } else {
            throw new NotFoundException("Could not find player with id" + id);
        }
    }
}
