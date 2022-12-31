package com.chasingdns.controller;

import com.chasingdns.entity.SubEvent;
import com.chasingdns.exception.NotFoundException;
import com.chasingdns.entity.Event;
import com.chasingdns.service.ClubService;
import com.chasingdns.service.EventService;
import com.chasingdns.service.SubEventService;
import com.chasingdns.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private TransactionService txnService;
    @Autowired
    private EventService eventService;
    @Autowired
    private SubEventService subEventService;
    @Autowired
    private ClubService clubService;

    @GetMapping("/events")
    public String showAllEvents(Model model){
        return findPaginated(1, "id", "desc", model);
    }

    @GetMapping("/events/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 5;
        Page<Event> page = eventService.findPaginated(Event.EVENT_STATUS.IN_PROGRESS, pageNo, pageSize, sortField, sortDir);
        List <Event> ipEvents = page.getContent();
        Page<Event> page1 = eventService.findPaginated(Event.EVENT_STATUS.COMPLETED, pageNo, pageSize, sortField, sortDir);
        List <Event> cEvents = page1.getContent();
        Page<SubEvent> page2 = subEventService.findPaginated(SubEvent.EVENT_STATUS.IN_PROGRESS, pageNo, pageSize, sortField, sortDir);
        List <SubEvent> ipSubEvents = page2.getContent();
        Page<SubEvent> page3 = subEventService.findPaginated(SubEvent.EVENT_STATUS.COMPLETED, pageNo, pageSize, sortField, sortDir);
        List <SubEvent> cSubEvents = page3.getContent();
        model.addAttribute("listInProgressEvents",ipEvents);
        model.addAttribute("listCompletedEvents",cEvents);
        model.addAttribute("listInProgressSubEvents",ipSubEvents);
        model.addAttribute("listCompletedSubEvents",cSubEvents);
        return "events";
    }

    @GetMapping("/inProgressEvents/page/{pageNo}")
    public String findPaginatedInProgress(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<Event> page = eventService.findPaginated(Event.EVENT_STATUS.IN_PROGRESS, pageNo, pageSize, sortField, sortDir);
        List <Event> ipEvents = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listInProgressEvents",ipEvents);
        return "showInProgressEvents";
    }

    @GetMapping("/completedEvents/page/{pageNo}")
    public String findPaginatedCompletedEvents(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<Event> page = eventService.findPaginated(Event.EVENT_STATUS.COMPLETED, pageNo, pageSize, sortField, sortDir);
        List <Event> ipEvents = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listInProgressEvents",ipEvents);
        return "showCompletedEvents";
    }

    @GetMapping("/inProgressSubEvents/page/{pageNo}")
    public String findPaginatedInProgressSubEvents(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<SubEvent> page = subEventService.findPaginated(SubEvent.EVENT_STATUS.IN_PROGRESS, pageNo, pageSize, sortField, sortDir);
        List <SubEvent> ipSubEvents = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listInProgressSubEvents",ipSubEvents);
        return "showInProgressSubEvents";
    }

    @GetMapping("/completedSubEvents/page/{pageNo}")
    public String findPaginatedCompletedSubEvents(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                          @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 10;
        Page<SubEvent> page = subEventService.findPaginated(SubEvent.EVENT_STATUS.COMPLETED, pageNo, pageSize, sortField, sortDir);
        List <SubEvent> cSubEvents = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listCompletedSubEvents",cSubEvents);
        return "showCompletedSubEvents";
    }



    @GetMapping("/events/add")
    public String addEvent(Model model){
        Event event = new Event();
        model.addAttribute("event",event);
        model.addAttribute("pageTitle","Add Event");
        return "event_form";
    }

    @GetMapping("/subEvents/add")
    public String addSubEvent(Model model){
        SubEvent subEvent = new SubEvent();
        model.addAttribute("events",eventService.listAllInProgressEvents());
        model.addAttribute("subEvent",subEvent);
        model.addAttribute("pageTitle","Add Sub Event");
        return "subEvent_form";
    }

    @PostMapping("/subEvents/save")
    public String saveSubEvent(SubEvent subEvent, RedirectAttributes ra){
        String msg = "";
        if(subEvent.getId()!=0){
            msg = "Sub Event updated successfully";
        } else {
            msg = "Sub Event added successfully";
            subEvent.setSubmittedDate(new Date());
        }
        subEvent.setUpdatedDate(new Date());
        if(subEvent.getStatus()==null){
            subEvent.setStatus(SubEvent.EVENT_STATUS.IN_PROGRESS);
        }
        subEventService.save(subEvent);
        ra.addFlashAttribute("message",msg);
        return "redirect:/events";
    }

    @PostMapping("/events/save")
    public String saveEvent(Event event, RedirectAttributes ra){
        String msg = "";
        if(event.getId()!=0){
            msg = "Event updated successfully";
        } else {
            msg = "Event added successfully";
            event.setSubmittedDate(new Date());
        }
        event.setUpdatedDate(new Date());
        if(event.getStatus()==null){
            event.setStatus(Event.EVENT_STATUS.IN_PROGRESS);
        }
        eventService.save(event);
        ra.addFlashAttribute("message",msg);
        return "redirect:/events";
    }

    @GetMapping("/events/edit/{id}")
    public String showEditForm(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Event event = eventService.get(id);
            model.addAttribute("event", event);
            model.addAttribute("pageTitle","Edit Event (ID:" + id + ")");
            return "event_form";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Event not found");
            return "redirect:/events";
        }
    }

    @GetMapping("/subEvents/edit/{id}")
    public String showSubEventEditForm(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            SubEvent subEvent = subEventService.get(id);
            model.addAttribute("subEvent", subEvent);
            model.addAttribute("events",eventService.listAllInProgressEvents());
            model.addAttribute("pageTitle","Edit Sub Event (ID:" + id + ")");
            return "subEvent_form";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Sub Event not found");
            return "redirect:/events";
        }
    }

    @GetMapping("/events/details/{id}")
    public String showDetails(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            Event event = eventService.get(id);
            model.addAttribute("event", event);
            model.addAttribute("subEvents", subEventService.findByEvent(event));
            model.addAttribute("transactions", event.getTransactions());
            model.addAttribute("pageTitle","Event Details (ID:" + id + ")");
            return "event_details";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Event not found");
            return "redirect:/events";
        }
    }

    @GetMapping("/subEvents/details/{id}")
    public String showSubEventDetails(@PathVariable("id")Integer id, Model model, RedirectAttributes ra){
        try {
            SubEvent subEvent = subEventService.get(id);
            model.addAttribute("subEvent", subEvent);
            model.addAttribute("event", subEvent.getEvent());
            model.addAttribute("transactions", subEvent.getTransaction());
            model.addAttribute("pageTitle","Sub Event Details (ID:" + id + ")");
            return "subEvent_details";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("errorMessage","Sub Event not found");
            return "redirect:/events";
        }
    }




}
