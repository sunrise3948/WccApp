package com.chasingdns.controller;

import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.ClubAccount;
import com.chasingdns.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ClubController {

    @Autowired
    private ClubService clubService;


    @GetMapping("/club")
    public String showClubDetails(Model model){
        ClubAccount clubAccount = clubService.listClubAccount();
        if(clubAccount==null){
            //first time, show create club screen
            model.addAttribute("clubAccount",new ClubAccount());
            model.addAttribute("pageTitle","Add Club");
            return "create_club";
        }
        model.addAttribute("clubAccount",clubAccount);
        return "club";
    }

    @PostMapping("/club/save")
    public String saveClub(ClubAccount account, RedirectAttributes ra){
        String msg = "";
        if(account.getId()!=0){
            msg = "Club Account updated successfully";
        } else {
            msg = "Club Account added successfully";
        }
        account.setCurrentBalance(account.getCurrentBalance().add(account.getStartingBalance()));
        clubService.save(account);
        ra.addFlashAttribute("message",msg);
        return "redirect:/club";
    }


    @GetMapping("/club/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            ClubAccount account = clubService.get(id);
            model.addAttribute("clubAccount", account);
            model.addAttribute("pageTitle","Edit Club");
            return "create_club";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Club not found");
            return "redirect:/club";
        }
    }
}
