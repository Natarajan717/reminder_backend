package event.reminder.reminder.repository;

import event.reminder.reminder.entity.EventReminder;
import event.reminder.reminder.enums.CompletionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventReminderRepository extends JpaRepository<EventReminder, Long> {
    List<EventReminder> findByCompletedFalse();
    List<EventReminder> findByCompletedTrue();
    List<EventReminder> findByCompletedTrueAndCompletionType(CompletionType type);
    List<EventReminder> findByEventTimeBeforeAndCompletedFalse(LocalDateTime now);
}
