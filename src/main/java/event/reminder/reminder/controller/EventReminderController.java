package event.reminder.reminder.controller;

import event.reminder.reminder.dto.CompletionRequest;
import event.reminder.reminder.entity.AppUser;
import event.reminder.reminder.entity.EventReminder;
import event.reminder.reminder.enums.CompletionType;
import event.reminder.reminder.repository.EventReminderRepository;
import event.reminder.reminder.repository.UserRepository;
import event.reminder.reminder.scheduler.ReminderScheduler;
import event.reminder.reminder.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventReminderController {
    private final EventReminderRepository repository;
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final ReminderScheduler reminderScheduler;
    private static final Logger logger = LoggerFactory.getLogger(EventReminderController.class);

    @PostMapping("test")
    public void sendnotification(@RequestBody Map<String, String> request){
        reminderScheduler.sendNotification(request.get("token"),
                request.get("title"),
                request.get("body"));
    }

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

    @PostMapping("/fcm-token")
    public ResponseEntity<Object> saveFcmToken(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody Map<String, String> request) {
        logger.info("fcm token received");
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication1.getPrincipal();
        Long id = appUser.getId();

        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFcmToken(request.get("fcmToken"));
        userRepo.save(user);

        return ResponseEntity.ok().build();
    }
}
