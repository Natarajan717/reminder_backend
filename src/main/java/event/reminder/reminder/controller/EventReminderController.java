package event.reminder.reminder.controller;

import event.reminder.reminder.dto.CompletionRequest;
import event.reminder.reminder.entity.AppUser;
import event.reminder.reminder.entity.EventReminder;
import event.reminder.reminder.enums.CompletionType;
import event.reminder.reminder.repository.EventReminderRepository;
import event.reminder.reminder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventReminderController {
    private final EventReminderRepository repository;
    private final UserRepository userRepo;
    private static final Logger logger = LoggerFactory.getLogger(EventReminderController.class);
    @GetMapping("/upcoming")
    public List<EventReminder> getUpcoming() {
        logger.info("getUpcoming Called");
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication1.getPrincipal();
        Long id = appUser.getId();
        logger.info("getUpcoming Called for {}", id);
        return repository.findByAppUserIdAndCompletedFalse(id);
    }

    @GetMapping("/history")
    public List<EventReminder> getCompleted() {
        logger.info("getCompleted Called");
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication1.getPrincipal();
        Long id = appUser.getId();
        return repository.findByAppUserIdAndCompletedTrue(id);
    }

    @GetMapping("/history/{type}")
    public List<EventReminder> getByType(@PathVariable CompletionType type) {
        logger.info("getByType Called for type {}", type);
        return repository.findByCompletedTrueAndCompletionType(type);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventReminder create(@RequestBody EventReminder event) {
        logger.info("getCompleted Called {}", event);
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication1.getPrincipal();

        event.setCompleted(false);
        event.setNotified(false);
        event.setLastReminderSent(null);
        event.setAppUser(appUser);
        return repository.save(event);
    }

    @PutMapping("/{id}")
    public EventReminder update(@PathVariable Long id, @RequestBody EventReminder updated) {
        logger.info("update Called for {} with {}", id, updated);
        EventReminder event = repository.findById(id).orElseThrow();
        if (LocalDateTime.now().isAfter(updated.getEventTime())) {
            event.setNote(updated.getNote());
        } else {
            event.setTitle(updated.getTitle());
            event.setNote(updated.getNote());
            event.setEventTime(updated.getEventTime());
            event.setRemindBeforeMinutes(updated.getRemindBeforeMinutes());
            event.setRepeatAfterMinutes(updated.getRepeatAfterMinutes());
            event.setEmail(updated.getEmail());
            event.setCompleted(false);
            event.setNotified(false);
            event.setCompletionType(null);
            event.setCompletedAt(null);
            event.setLastReminderSent(null);
        }
        return repository.save(event);
    }

    @PostMapping("/{id}/complete")
    public EventReminder markComplete(@PathVariable Long id, @RequestBody CompletionRequest request) {
        logger.info("markComplete Called for {} with {}", id, request);

        EventReminder event = repository.findById(id).orElseThrow();
        event.setCompleted(true);
        event.setCompletionType(CompletionType.valueOf(request.getCompletionType()));
        event.setCompletedAt(LocalDateTime.now());

        if (request.getNote() != null && !request.getNote().isEmpty()) {
            event.setNote(request.getNote());
        }

        return repository.save(event);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void delete(@PathVariable Long id) {
        logger.info("delete Called for {}", id);
        repository.deleteById(id);
    }
}
