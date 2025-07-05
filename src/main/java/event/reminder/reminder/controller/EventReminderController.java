package event.reminder.reminder.controller;

import event.reminder.reminder.dto.CompletionRequest;
import event.reminder.reminder.entity.EventReminder;
import event.reminder.reminder.enums.CompletionType;
import event.reminder.reminder.repository.EventReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventReminderController {
    private final EventReminderRepository repository;

    @GetMapping("/upcoming")
    public List<EventReminder> getUpcoming() {
        System.out.println("called");
        return repository.findByCompletedFalse();
    }

    @GetMapping("/history")
    public List<EventReminder> getCompleted() {
        System.out.println("called");
        return repository.findByCompletedTrue();
    }

    @GetMapping("/history/{type}")
    public List<EventReminder> getByType(@PathVariable CompletionType type) {
        System.out.println("called getByType "+ type);
        return repository.findByCompletedTrueAndCompletionType(type);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventReminder create(@RequestBody EventReminder event) {
        System.out.println("create called "+ event);
        event.setCompleted(false);
        event.setNotified(false);
        event.setLastReminderSent(null);
        return repository.save(event);
    }

    @PutMapping("/{id}")
    public EventReminder update(@PathVariable Long id, @RequestBody EventReminder updated) {
        System.out.println("called update" + updated);
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
        System.out.println("called markComplete" + request);

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
        System.out.println("called");
        repository.deleteById(id);
    }
}
