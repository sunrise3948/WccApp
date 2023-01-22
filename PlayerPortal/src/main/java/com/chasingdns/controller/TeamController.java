package com.chasingdns.controller;

import com.chasingdns.entity.Player;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.Team;
import com.chasingdns.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TeamController {

    @Autowired private TeamService teamService;

    @GetMapping("/teams")
    public String showAllTeams(Model model){
        return findPaginated(1, "id", "desc", model);
    }

    @GetMapping("/teams/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 5;
        Page<Team> page = teamService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List <Team> listTeams = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listTeams", listTeams);
        return "teams";
    }

    @GetMapping("/teams/add")
    public String addNewTeam(Model model){
        model.addAttribute("team",new Team());
        model.addAttribute("pageTitle","Add New Team");
        return "team_form";
    }

    @PostMapping("/teams/save")
    public String saveNewTeam(Team team, RedirectAttributes ra){
        String msg = "";
        if(team.getId()!=0){
            msg = "Team updated successfully";
        } else {
            msg = "Team added successfully";
        }
        teamService.save(team);
        ra.addFlashAttribute("message",msg);
        return "redirect:/teams";
    }

    @GetMapping("/teams/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Team team = teamService.get(id);
            model.addAttribute("team", team);
            model.addAttribute("pageTitle","Edit team (ID:" + id + ")");
            return "team_form";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Team not found");
            return "redirect:/teams";
        }
    }

    @GetMapping("/teams/roster/{id}")
    public String showTeamRoster(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        return findPaginatedTeamRoster(id, 1, "firstName", "asc", model, ra);
    }

    @GetMapping("/teams/roster/page/{pageNo}")
    public String findPaginatedTeamRoster(@RequestParam("teamId")Integer id, @PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model, RedirectAttributes ra) {
        int pageSize = 5;
        Team team = null;
        try {
            team = teamService.get(id);
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Team not found");
            return "redirect:/teams";
        }
        model.addAttribute("team", team);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", (!team.getPlayers().isEmpty() ? team.getPlayers().size() : 5)/pageSize);
        model.addAttribute("totalItems", (!team.getPlayers().isEmpty() ? team.getPlayers().size() : 0));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("id", team.getId());
        model.addAttribute("teamPlayers", team.getPlayers());
        return "team_roster";
    }
}
