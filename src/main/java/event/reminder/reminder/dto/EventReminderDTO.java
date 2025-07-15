package event.reminder.reminder.dto;

import event.reminder.reminder.enums.CompletionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventReminderDTO {
    private Long id;
    private String title;
    private String note;
    private LocalDateTime eventTime;
    private int remindBeforeMinutes;
    private int repeatAfterMinutes;
    private boolean completed;
    private CompletionType completionType;
    private LocalDateTime completedAt;
    private boolean notified;
    private LocalDateTime lastReminderSent;
    private Long userId; // for reference only
}
