package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.request.EventModifyRequest;
import ru.itmo.hungergames.model.request.EventRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextOrderResponse;
import ru.itmo.hungergames.model.response.EventResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.service.TributeService;

import java.time.Instant;
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
    public AdvertisingTextOrderResponse sendAdvertisingText(@RequestBody AdvertisingTextRequest advertisingTextRequest) {
        return tributeService.sendAdvertisingText(advertisingTextRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{tributeId}/advertisement")
    public List<String> getAllAdvertisingTexts(@PathVariable UUID tributeId) {
        return tributeService.getApprovedAndPaidAdvertisingTexts(tributeId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{tributeId}")
    public TributeResponse getTributeById(@PathVariable UUID tributeId) {
        return tributeService.getTributeById(tributeId);
    }

    @DeleteMapping("/event/{eventId}")
    public void deleteEventById(@PathVariable UUID eventId) {
        tributeService.deleteEventById(eventId);
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
    public List<EventResponse> getOwnEvents() {
        return tributeService.getOwnEvents();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{tributeId}/events")
    public List<EventResponse> getEvents(@PathVariable UUID tributeId) {
        return tributeService.getEvents(tributeId);
    }

    @GetMapping("/time") // for debugging
    public String getCurrentTime() {
        return Instant.now().toString();
    }
}
