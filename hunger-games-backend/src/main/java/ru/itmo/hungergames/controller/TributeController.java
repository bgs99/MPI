package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.request.EventModifyRequest;
import ru.itmo.hungergames.model.request.EventRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.model.response.EventResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.service.TributeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@PreAuthorize("hasRole('TRIBUTE')")
@RequestMapping("/api/tribute")
public class TributeController {
    private final TributeService tributeService;

    @Autowired
    public TributeController(TributeService tributeService) {
        this.tributeService = tributeService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public List<TributeResponse> getAllTributes() {
        return tributeService.getAllTributes();
    }

    @PostMapping("/advertisement")
    public AdvertisingTextResponse sendAdvertisingText(@RequestBody AdvertisingTextRequest advertisingTextRequest) {
        return tributeService.sendAdvertisingText(advertisingTextRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/advertisement")
    public List<String> getAllAdvertisingTexts() {
        return tributeService.getApprovedAndPaidAdvertisingTexts();
    }

    @GetMapping("/{tributeId}")
    public TributeResponse getTributeById(@PathVariable UUID tributeId) {
        return tributeService.getTributeById(tributeId);
    }

    @PostMapping("/event")
    public EventResponse addEvent(@RequestBody EventRequest eventRequest) {
        return tributeService.addEvent(eventRequest);
    }

    @PostMapping("/event/change")
    public EventResponse modifyEvent(@RequestBody EventModifyRequest eventModifyRequest) {
        return tributeService.modifyEvent(eventModifyRequest);
    }

    @GetMapping("/event/type")
    public List<String> getEventTypes() {
        return tributeService.getEventTypes();
    }

    @GetMapping("/events")
    public List<EventResponse> getEvents() {
        return tributeService.getEvents();
    }

    @GetMapping("/time") // for debugging
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }
}
