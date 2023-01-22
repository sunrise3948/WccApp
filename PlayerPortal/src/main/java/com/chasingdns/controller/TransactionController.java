package com.chasingdns.controller;

import com.chasingdns.entity.Transaction.TXN_MODE;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.*;
import com.chasingdns.service.*;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.chasingdns.entity.Transaction.TXN_TYPE.CREDIT;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService txnService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClubService clubService;
    @Autowired
    private EventService eventService;
    @Autowired
    private SubEventService subEventService;


    @GetMapping("/txns")
    public String showAllTxns(Model model){
        return findPaginated(1,1,1,1, "id", "desc", model);
    }

    @GetMapping("/txns/page")
    public String findPaginated(@RequestParam(value = "pendingPageNo") int pendingPageNo,
                                @RequestParam(value = "approvedPageNo") int approvedPageNo,
                                @RequestParam(value = "settledPageNo") int settledPageNo,
                                @RequestParam(value = "canceledPageNo") int canceledPageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 5;
        Page<Transaction> page = txnService.findPaginated(Transaction.TXN_STATUS.PENDING, pendingPageNo, pageSize, sortField, sortDir);
        List <Transaction> allPendingTxns = page.getContent();
        Page<Transaction> page1 = txnService.findPaginated(Transaction.TXN_STATUS.ACKNOWLEDGED, approvedPageNo, pageSize, sortField, sortDir);
        List <Transaction> allApprovedTxns = page1.getContent();
        Page<Transaction> page2 = txnService.findPaginated(Transaction.TXN_STATUS.SETTLED, settledPageNo, pageSize, sortField, sortDir);
        List <Transaction> allSettledTxns = page2.getContent();
        Page<Transaction> page3 = txnService.findPaginated(Transaction.TXN_STATUS.CANCELED, canceledPageNo, pageSize, sortField, sortDir);
        List <Transaction> allCanceledTxns = page3.getContent();

        model.addAttribute("listPendingTxns",allPendingTxns);
        model.addAttribute("listApprovedTxns",allApprovedTxns);
        model.addAttribute("listSettledTxns",allSettledTxns);
        model.addAttribute("allCanceledTxns",allCanceledTxns);
        return "txns";
    }

    @GetMapping("/pendingTransactions/page/{pageNo}")
    public String showPaginatedPendingTransactions(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<Transaction> page = txnService.findPaginated(Transaction.TXN_STATUS.PENDING, pageNo, pageSize, sortField, sortDir);
        List <Transaction> transactions = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("transactions",transactions);
        return "showPendingTransactions";
    }

    @GetMapping("/acknowledgedTransactions/page/{pageNo}")
    public String showPaginatedAckTransactions(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<Transaction> page = txnService.findPaginated(Transaction.TXN_STATUS.ACKNOWLEDGED, pageNo, pageSize, sortField, sortDir);
        List <Transaction> transactions = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("transactions",transactions);
        return "showAcknowledgedTransactions";
    }

    @GetMapping("/settledTransactions/page/{pageNo}")
    public String showPaginatedSettledTransactions(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<Transaction> page = txnService.findPaginated(Transaction.TXN_STATUS.SETTLED, pageNo, pageSize, sortField, sortDir);
        List <Transaction> transactions = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("transactions",transactions);
        return "showSettledTransactions";
    }

    @GetMapping("/cancelledTransactions/page/{pageNo}")
    public String showPaginatedCancelledTransactions(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<Transaction> page = txnService.findPaginated(Transaction.TXN_STATUS.CANCELED, pageNo, pageSize, sortField, sortDir);
        List <Transaction> transactions = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("transactions",transactions);
        return "showCancelledTransactions";
    }

    @GetMapping("/txns/addCredit")
    public String addCreditTxn(Model model){
        model.addAttribute("txn",new Transaction());
        List<Player> players = playerService.listAllActive();
        model.addAttribute("players",players);
        List<Event> events = eventService.listAllInProgressEvents();
        model.addAttribute("events",events);
        model.addAttribute("pageTitle","Add Credit Transaction");
        return "txn_credit_form";
    }

    @GetMapping("/txns/addDebit")
    public String addDebitTxn(Model model){
        model.addAttribute("txn",new Transaction());
        List<Player> players = playerService.listAllActive();
        model.addAttribute("players",players);
        List<Event> events = eventService.listAllInProgressEvents();
        model.addAttribute("events",events);
        model.addAttribute("pageTitle","Add Debit Transaction");
        return "txn_debit_form";
    }

    @GetMapping("/txns/addExpense")
    public String addExpenseTxn(Model model){
        model.addAttribute("txn",new Transaction());
        List<Player> players = playerService.listAllActive();
        model.addAttribute("players",players);
        List<Event> events = eventService.listAllInProgressEvents();
        model.addAttribute("events",events);
        model.addAttribute("pageTitle","Add Expense Transaction");
        return "txn_expense_form";
    }

    @GetMapping("/txns/addSingleExpense")
    public String addIndividualExpenseTxn(Model model){
        model.addAttribute("txn",new Transaction());
        List<Player> players = playerService.listAllActive();
        model.addAttribute("players",players);
        List<Event> events = eventService.listAllInProgressEvents();
        model.addAttribute("events",events);
        model.addAttribute("pageTitle","Add Individual Expense Transaction");
        return "txn_ind_expense_form";
    }

    @GetMapping("/txn/player")
    public String addPlayerExpenseTxn(Model model, @AuthenticationPrincipal AppUserDetails userDetails){
        Transaction txn = new Transaction();
        model.addAttribute("txn",txn);
        Player player = playerService.findByUsername(userDetails.getUsername());
        model.addAttribute("player",player);
        List<Event> events = eventService.listAllInProgressExternalEvents();
        model.addAttribute("events",events);
        model.addAttribute("pageTitle","Add Individual Expense Transaction");
        return "txn_player_expense_form";
    }

    @PostMapping("/txns/save")
    public String saveTxn(Transaction txn, RedirectAttributes ra){
        String msg = "";
        if(txn.getId()!=0){
            msg = "Txn updated successfully";
        } else {
            msg = "Txn added successfully";
            txn.setSubmittedDate(new Date());
        }
        txn.setUpdatedDate(new Date());
        txn.setStatus(Transaction.TXN_STATUS.PENDING);
        txnService.save(txn);
        ra.addFlashAttribute("message",msg);
        return "redirect:/txns";
    }

    @PostMapping("/txns/saveCredit")
    public String saveNewCreditTxn(Transaction txn, RedirectAttributes ra){
        String msg = "";
        if(txn.getId()!=0){
            msg = "Txn updated successfully";
        } else {
            msg = "Txn added successfully";
            txn.setSubmittedDate(new Date());
        }

        Optional<Player> playerAccount = txn.getPlayers().stream().findFirst();
        if(playerAccount.isPresent()){
            Player player = playerAccount.get();
            player.getTransactions().add(txn);
        }
        Event event = txn.getEvent();
        event.getTransactions().add(txn);
        txn.setUpdatedDate(new Date());
        txn.setStatus(Transaction.TXN_STATUS.PENDING);
        txn.setMode(TXN_MODE.INDIVIDUAL);
        txnService.save(txn);
        ra.addFlashAttribute("message",msg);
        return "redirect:/txns";
    }

    @PostMapping("/txns/saveDebit")
    public String saveNewDebitTxn(Transaction txn, RedirectAttributes ra){
        String msg = "";
        if(txn.getId()!=0){
            msg = "Txn updated successfully";
        } else {
            msg = "Txn added successfully";
            txn.setSubmittedDate(new Date());
        }

        Optional<Player> playerAccount = txn.getPlayers().stream().findFirst();
        if(playerAccount.isPresent()){
            Player player = playerAccount.get();
            player.getTransactions().add(txn);
        }
        Event event = txn.getEvent();
        event.getTransactions().add(txn);
        txn.setUpdatedDate(new Date());
        txn.setStatus(Transaction.TXN_STATUS.PENDING);
        txn.setMode(TXN_MODE.INDIVIDUAL);
        txnService.save(txn);
        ra.addFlashAttribute("message",msg);
        return "redirect:/txns";
    }

    @PostMapping("/txns/saveExpense")
    public String saveNewExpenseTxn(Transaction txn, RedirectAttributes ra){
        String msg = "";
        if(txn.getId()!=0){
            msg = "Txn updated successfully";
        } else {
            msg = "Txn added successfully";
            txn.setSubmittedDate(new Date());
        }
        Event event = txn.getEvent();
        event.getTransactions().add(txn);
        List<SubEvent> subEvents = subEventService.findByEvent(event);
        BigDecimal txnAmount = BigDecimal.ZERO;
        //Add events, calculate txn amount
        for(SubEvent subEvent : subEvents){
            txnAmount = txnAmount.add(subEvent.getAmount());
            //subEvent.setTransaction(txn);
        }
        //Add Players
        Set<Player> players = txn.getPlayers();
        for(Player player : players){
            player.getTransactions().add(txn);
        }
        txn.setAmount(txnAmount);
        txn.setUpdatedDate(new Date());
        txn.setStatus(Transaction.TXN_STATUS.PENDING);
        txn.setMode(TXN_MODE.GROUP);
        txnService.save(txn);
        ra.addFlashAttribute("message",msg);
        return "redirect:/txns";
    }

    @PostMapping("/txns/saveIndExpense")
    public String saveNewIndividualExpenseTxn(Transaction txn, RedirectAttributes ra){
        String msg = "";
        if(txn.getId()!=0){
            msg = "Txn updated successfully";
        } else {
            msg = "Txn added successfully";
            txn.setSubmittedDate(new Date());
        }
        Set<Player> players = txn.getPlayers();
        for(Player player : players){
            player.getTransactions().add(txn);
        }
        Event event = txn.getEvent();
        event.getTransactions().add(txn);
        txn.setUpdatedDate(new Date());
        txn.setStatus(Transaction.TXN_STATUS.PENDING);
        txn.setMode(TXN_MODE.INDIVIDUAL);
        txnService.save(txn);
        ra.addFlashAttribute("message",msg);
        return "redirect:/txns";
    }

    @PostMapping("/txn/savePlayerExpense")
    public String saveNewPlayerExpenseTxn(Transaction txn, RedirectAttributes ra, Model model, @AuthenticationPrincipal AppUserDetails userDetails){
        Player player = playerService.findByUsername(userDetails.getUsername());
        player.getTransactions().add(txn);
        Event event = txn.getEvent();
        event.getTransactions().add(txn);
        txn.setSubmittedDate(new Date());
        txn.setUpdatedDate(new Date());
        txn.setStatus(Transaction.TXN_STATUS.PENDING);
        txn.setMode(TXN_MODE.INDIVIDUAL);
        txnService.save(txn);
        ra.addFlashAttribute("message","Txn added successfully");
        return "redirect:/index";
    }

    private SubEvent createSubEventForThisTxn(Transaction txn) {
        SubEvent subEvent = new SubEvent();
        subEvent.setTransaction(txn);
        subEvent.setStatus(SubEvent.EVENT_STATUS.IN_PROGRESS);
        subEvent.setSubmittedDate(new Date());
        subEvent.setUpdatedDate(new Date());
        subEvent.setType(SubEvent.EVENT_TYPE.GAME_EXPENSE);
        subEvent.setAmount(txn.getAmount());
        subEvent.setEvent(txn.getEvent());
        subEvent.setDescription(txn.getDescription());
        return subEvent;
    }


    @GetMapping("/txns/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Transaction txn = txnService.get(id);
            model.addAttribute("txn", txn);
            model.addAttribute("pageTitle","Edit Txn (ID:" + id + ")");
            List<Event> events = eventService.listAllInProgressEvents();
            model.addAttribute("events",events);
            return "txn_form";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Txn not found");
            return "redirect:/txns";
        }
    }

    @GetMapping("/txns/details/{id}")
    public String showTxnDetails(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Transaction txn = txnService.get(id);
            model.addAttribute("txn", txn);
            Set<Player> players = txn.getPlayers();
            model.addAttribute("players",players);
            Event event = txn.getEvent();
            model.addAttribute("event",event);
            //List<SubEvent> subEvents = subEventService.findByEvent(event);
            model.addAttribute("subEvents", txn.getSubEvent());
            model.addAttribute("pageTitle","Details Txn (ID:" + id + ")");
            return "txn_details";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Txn not found");
            return "redirect:/txns";
        }
    }

    @GetMapping("/txn/details/{id}")
    public String showUserTxnDetails(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Transaction txn = txnService.get(id);
            model.addAttribute("txn", txn);
            Set<Player> players = txn.getPlayers();
            model.addAttribute("players",players);
            Event event = txn.getEvent();
            model.addAttribute("event",event);
            List<SubEvent> subEvents = subEventService.findByEvent(event);
            model.addAttribute("subEvents", subEvents);
            model.addAttribute("pageTitle","Details Txn (ID:" + id + ")");
            return "user_txn_details";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Txn not found");
            return "redirect:/player";
        }
    }

    @GetMapping("/txns/acknowledge/{id}")
    public String approveTxn(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Transaction txn = txnService.get(id);
            if(txn.getStatus().equals(Transaction.TXN_STATUS.PENDING)){
                switch(txn.getType()){
                    case CREDIT: addUnsettledCreditToAccountBalances(txn);
                        break;
                    case DEBIT: addUnsettledDebitToAccountBalances(txn);
                        break;
                    case EXPENSE:
                        if (txn.getMode().equals(TXN_MODE.INDIVIDUAL)) {
                            addUnsettledIndividualExpenseToAccountBalances(txn);
                            SubEvent subEvent = createSubEventForThisTxn(txn);
                            txn.setSubEvent(subEvent);
                            subEventService.save(subEvent);
                        } else {
                            addUnsettledExpenseToAccountBalances(txn);
                        }
                        break;
                }
                txn.setUpdatedDate(new Date());
                txn.setStatus(Transaction.TXN_STATUS.ACKNOWLEDGED);
                txnService.save(txn);
                ra.addFlashAttribute("message","Transaction acknowledged!");
            } else {
                ra.addFlashAttribute("errorMessage","Transaction cannot be acknowledged, invalid status!");
            }
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Txn not found");
            throw new RuntimeException(e);
        }
        return "redirect:/txns";
    }

    @GetMapping("/txns/cancel/{id}")
    public String cancelTxn(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Transaction txn = txnService.get(id);
            if(txn.getStatus().equals(Transaction.TXN_STATUS.PENDING)){
                txn.setUpdatedDate(new Date());
                txn.setStatus(Transaction.TXN_STATUS.CANCELED);
                txnService.save(txn);
                ra.addFlashAttribute("message","Transaction cancelled!");
            } else {
                ra.addFlashAttribute("errorMessage","Transaction cannot be cancelled, invalid status!");
            }

        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Txn not found");
            throw new RuntimeException(e);
        }
        return "redirect:/txns";
    }

    @GetMapping("/txns/cancelAcknowledgedTxn/{id}")
    public String cancelAcknowledgedTxn(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Transaction txn = txnService.get(id);
            if(txn.getStatus().equals(Transaction.TXN_STATUS.ACKNOWLEDGED)){
                txn.setUpdatedDate(new Date());
                txn.setStatus(Transaction.TXN_STATUS.CANCELED);
                Set<Player> players = txn.getPlayers();
                if(!players.isEmpty()){
                    cancelTransactionalBalances(txn);
                }
                if(txn.getSubEvent()!=null){
                    txn.getSubEvent().setStatus(SubEvent.EVENT_STATUS.CANCELLED);
                }
                txnService.save(txn);
                ra.addFlashAttribute("message","Transaction cancelled!");
            } else {
                ra.addFlashAttribute("errorMessage","Transaction cannot be cancelled, invalid status!");
            }
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Txn not found");
            throw new RuntimeException(e);
        }
        return "redirect:/txns";
    }

    @GetMapping("/txns/settle/{id}")
    public String settleTxn(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Transaction txn = txnService.get(id);
            if(txn.getStatus().equals(Transaction.TXN_STATUS.ACKNOWLEDGED)){
                txn.setUpdatedDate(new Date());
                txn.setStatus(Transaction.TXN_STATUS.SETTLED);
                settleTransactionalBalances(txn);
                txnService.save(txn);
                subEventService.completeSubEvent(txn.getSubEvent());
                eventService.completeExternalEvent(txn.getEvent());
                ra.addFlashAttribute("message","Transaction settled!");
            } else {
                ra.addFlashAttribute("errorMessage","Transaction cannot be settled, invalid status!");
            }
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Txn not found");
            throw new RuntimeException(e);
        }
        return "redirect:/txns";
    }

    private void addUnsettledCreditToAccountBalances(Transaction txn) {
        Optional<Player> playerAccount = txn.getPlayers().stream().findFirst();
        if(playerAccount.isPresent()){
            Player player = playerAccount.get();
            try {
                Account account = accountService.getAccountByPlayerId(player.getId());
                account.setAvailableBalance(account.getAvailableBalance().add(txn.getAmount()));
                ClubAccount clubAccount = clubService.listClubAccount();
                clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().add(txn.getAmount()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addUnsettledDebitToAccountBalances(Transaction txn) {
        Optional<Player> playerAccount = txn.getPlayers().stream().findFirst();
        if(playerAccount.isPresent()){
            Player player = playerAccount.get();
            try {
                Account account = accountService.getAccountByPlayerId(player.getId());
                account.setAvailableBalance(account.getAvailableBalance().subtract(txn.getAmount()));
                ClubAccount clubAccount = clubService.listClubAccount();
                clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().subtract(txn.getAmount()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addUnsettledExpenseToAccountBalances(Transaction txn) {
        Set<Player> players = txn.getPlayers();
        //adjust player available balances
        BigDecimal txnIndividualAmount = txn.getAmount().divide(BigDecimal.valueOf(txn.getPlayers().size()), RoundingMode.HALF_UP);
        for(Player player : players){
            try {
                Account account = accountService.getAccountByPlayerId(player.getId());
                account.setAvailableBalance(account.getAvailableBalance().subtract(txnIndividualAmount));
                player.getTransactions().add(txn);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        ClubAccount clubAccount = clubService.listClubAccount();
        clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().subtract(txn.getAmount()));
    }

    private void addUnsettledIndividualExpenseToAccountBalances(Transaction txn) {
        Set<Player> players = txn.getPlayers();
        for(Player player : players){
            try {
                Account account = accountService.getAccountByPlayerId(player.getId());
                account.setAvailableBalance(account.getAvailableBalance().add(txn.getAmount()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void cancelTransactionalBalances(Transaction txn){
        if(txn.getType().equals(CREDIT)){
            for(Player player: txn.getPlayers()){
                try {
                    Account account = accountService.getAccountByPlayerId(player.getId());
                    account.setAvailableBalance(account.getAvailableBalance().subtract(txn.getAmount()));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            ClubAccount clubAccount = clubService.listClubAccount();
            clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().subtract(txn.getAmount()));
        } else if(txn.getType().equals(Transaction.TXN_TYPE.DEBIT)){
            for(Player player: txn.getPlayers()){
                try {
                    Account account = accountService.getAccountByPlayerId(player.getId());
                    account.setAvailableBalance(account.getAvailableBalance().add(txn.getAmount()));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            ClubAccount clubAccount = clubService.listClubAccount();
            clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().add(txn.getAmount()));
        } else {
            // EXPENSE
            BigDecimal txnIndividualAmount = txn.getAmount().divide(BigDecimal.valueOf(txn.getPlayers().size()), RoundingMode.HALF_UP);
            for(Player player: txn.getPlayers()){
                try {
                    Account account = accountService.getAccountByPlayerId(player.getId());
                    account.setAvailableBalance(account.getAvailableBalance().add(txnIndividualAmount));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if(txn.getMode().equals(TXN_MODE.GROUP)){
                ClubAccount clubAccount = clubService.listClubAccount();
                clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().add(txn.getAmount()));
            }
        }
    }

    private void settleTransactionalBalances(Transaction txn){
        if(txn.getType().equals(CREDIT)){
            Optional<Player> playerAccount = txn.getPlayers().stream().findFirst();
            if(playerAccount.isPresent()){
                Player player = playerAccount.get();
                try {
                    Account account = accountService.getAccountByPlayerId(player.getId());
                    account.setCurrentBalance(account.getCurrentBalance().add(txn.getAmount()));
                    ClubAccount clubAccount = clubService.listClubAccount();
                    clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().subtract(txn.getAmount()));
                    clubAccount.setCurrentBalance((clubAccount.getCurrentBalance().add(txn.getAmount())));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if(txn.getType().equals(Transaction.TXN_TYPE.DEBIT)){
            Optional<Player> playerAccount = txn.getPlayers().stream().findFirst();
            if(playerAccount.isPresent()){
                Player player = playerAccount.get();
                try {
                    Account account = accountService.getAccountByPlayerId(player.getId());
                    account.setCurrentBalance(account.getCurrentBalance().subtract(txn.getAmount()));
                    ClubAccount clubAccount = clubService.listClubAccount();
                    clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().add(txn.getAmount()));
                    clubAccount.setCurrentBalance((clubAccount.getCurrentBalance().subtract(txn.getAmount())));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            // EXPENSE
            BigDecimal txnIndividualAmount = txn.getAmount().divide(BigDecimal.valueOf(txn.getPlayers().size()), RoundingMode.HALF_UP);
            for(Player player: txn.getPlayers()){
                try {
                    Account account = accountService.getAccountByPlayerId(player.getId());
                    if(txn.getMode().equals(TXN_MODE.GROUP)){
                        account.setCurrentBalance(account.getCurrentBalance().subtract(txnIndividualAmount));
                    } else {
                        account.setCurrentBalance(account.getCurrentBalance().add(txnIndividualAmount));
                    }
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            ClubAccount clubAccount = clubService.listClubAccount();
            clubAccount.setSuspenseBalance(clubAccount.getSuspenseBalance().add(txn.getAmount()));
            clubAccount.setCurrentBalance(clubAccount.getCurrentBalance().subtract(txn.getAmount()));
        }
    }

}
