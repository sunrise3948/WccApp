package com.chasingdns.controller;

import com.chasingdns.entity.*;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.service.AccountService;
import com.chasingdns.service.PlayerService;
import com.chasingdns.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class PlayerController {

    @Autowired private PlayerService playerService;
    @Autowired private AccountService accountService;
    @Autowired private TeamService teamService;

    @GetMapping("/players")
    public String showAllPlayers(Model model){
        return findPaginated(1, "id", "desc", model);
    }

    @GetMapping("/players/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<Player> page = playerService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List <Player> listPlayers = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listPlayers", listPlayers);
        return "players";
    }

    @GetMapping("/player")
    public String showPlayerDetails(Model model, @AuthenticationPrincipal AppUserDetails userDetails) throws NotFoundException {
        Player player = playerService.findByUsername(userDetails.getUsername());
        if(player==null){
            model.addAttribute("errorMessage","Player not found");
        }
        Account account = accountService.getAccountByPlayerId(player.getId());
        model.addAttribute("listTxns", player.getTransactions());
        model.addAttribute("listTeams", player.getTeams());
        model.addAttribute("account", account);
        return "showPlayerDetails";
    }

    @GetMapping("/player/performance")
    public void showPlayerPerformance(HttpServletResponse httpServletResponse, @AuthenticationPrincipal AppUserDetails userDetails) throws NotFoundException {
        Player player = playerService.findByUsername(userDetails.getUsername());
        if(player.getUrl()!=null){
            httpServletResponse.setHeader("Location", player.getUrl().toString());
            httpServletResponse.setStatus(302);
        } else {
            httpServletResponse.setHeader("Location", "index");
            httpServletResponse.setStatus(302);
        }
    }

    @GetMapping("/player/addFunds")
    public String addPlayerFunds(Model model, @AuthenticationPrincipal AppUserDetails userDetails) throws NotFoundException {
        Player player = playerService.findByUsername(userDetails.getUsername());
        if(player==null){
            model.addAttribute("errorMessage","Player not found");
        }
        Account account = accountService.getAccountByPlayerId(player.getId());
        model.addAttribute("account", account);
        model.addAttribute("player", player);
        return "showAddFunds";
    }

    @GetMapping("/players/add")
    public String addNewPlayer(Model model){
        model.addAttribute("player",new Player());
        model.addAttribute("pageTitle","Add New Player");
        return "player_form";
    }

    @PostMapping("/players/save")
    public String saveNewPlayer(Player player, RedirectAttributes ra){
        String msg = "";
        if(player.getId()!=0){
            msg = "Player updated successfully";
        } else {
            msg = "Player added successfully";
        }
        playerService.save(player);
        ra.addFlashAttribute("message",msg);
        return "redirect:/players";
    }

    @GetMapping("/players/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Player player = playerService.get(id);
            model.addAttribute("player", player);
            model.addAttribute("transactions", player.getTransactions());
            model.addAttribute("teams", player.getTeams());
            model.addAttribute("pageTitle","Edit Player (ID:" + id + ")");
            return "player_edit_form";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Player not found");
            return "redirect:/players";
        }
    }

    @GetMapping("/players/delete/{id}")
    public String deletePlayer(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            playerService.delete(id);
            ra.addFlashAttribute("message","Player with ID: "+ id + " deleted successfully");
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Player not found");
            return "redirect:/players";
        }
        return "redirect:/players";
    }

    @GetMapping("/players/account/{id}")
    public String getPlayerAccountDetails(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Account account = accountService.getAccountByPlayerId(id);
            Player player = playerService.get(id);
            model.addAttribute("listTxns", player.getTransactions());
            model.addAttribute("account", account);
            return "player_account";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Account not found");
            return "redirect:/players";
        }
    }

    @GetMapping("/players/team/{id}")
    public String getTeamByPlayerId(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Player player = playerService.get(id);
            Set<Team> teams = player.getTeams();
            model.addAttribute("listTeams", teams);
            model.addAttribute("playerId", id);
            List<Team> allTeams = teamService.listAll();
            List<Team> allActiveTeams = new ArrayList<Team>();
            for(Team team: allTeams){
                if(team.isEnabled()) allActiveTeams.add(team);
            }
            model.addAttribute("listAllTeams", allActiveTeams);
            return "player_team";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Player not found");
            return "redirect:/players";
        }
    }

    @GetMapping("/players/addTeam/{id}/{playerId}")
    public String addTeamToPlayer(@PathVariable("id")Integer id, @PathVariable("playerId")Integer playerId, Model model, RedirectAttributes ra){
        try {
            Team team = teamService.get(id);
            Player player = playerService.get(playerId);
            player.getTeams().add(team);
            playerService.save(player);
            model.addAttribute("listTeams", player.getTeams());
            model.addAttribute("playerId", id);
            List<Team> allTeams = teamService.listAll();
            List<Team> allActiveTeams = new ArrayList<Team>();
            for(Team team1: allTeams){
                if(team1.isEnabled()) allActiveTeams.add(team1);
            }
            model.addAttribute("listAllTeams", allActiveTeams);
            ra.addFlashAttribute("message","Team assignment added to Player");
            return "redirect:/players";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Player not found");
            return "redirect:/players";
        }
    }

    @GetMapping("/players/removeTeam/{id}/{playerId}")
    public String removeTeamFromPlayer(@PathVariable("id")Integer id, @PathVariable("playerId")Integer playerId, Model model, RedirectAttributes ra){
        try {
            Team team = teamService.get(id);
            Player player = playerService.get(playerId);
            player.getTeams().remove(team);
            playerService.save(player);
            model.addAttribute("listTeams", player.getTeams());
            model.addAttribute("playerId", id);
            List<Team> allTeams = teamService.listAll();
            List<Team> allActiveTeams = new ArrayList<Team>();
            for(Team team1: allTeams){
                if(team1.isEnabled()) allActiveTeams.add(team1);
            }
            model.addAttribute("listAllTeams", allActiveTeams);
            ra.addFlashAttribute("message","Team assignment removed from Player");
            return "redirect:/players";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Player not found");
            return "redirect:/players";
        }
    }


}
