package event.reminder.reminder.entity;

import event.reminder.reminder.enums.CompletionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class EventReminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String note;
    private LocalDateTime eventTime;
    private int remindBeforeMinutes;
    private int repeatAfterMinutes;
    private boolean completed;

    @Enumerated(EnumType.STRING)
    private CompletionType completionType;

    private LocalDateTime completedAt;
    private String email;
    private boolean notified;
    private LocalDateTime lastReminderSent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public int getRemindBeforeMinutes() {
        return remindBeforeMinutes;
    }

    public void setRemindBeforeMinutes(int remindBeforeMinutes) {
        this.remindBeforeMinutes = remindBeforeMinutes;
    }

    public int getRepeatAfterMinutes() {
        return repeatAfterMinutes;
    }

    public void setRepeatAfterMinutes(int repeatAfterMinutes) {
        this.repeatAfterMinutes = repeatAfterMinutes;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public CompletionType getCompletionType() {
        return completionType;
    }

    public void setCompletionType(CompletionType completionType) {
        this.completionType = completionType;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public LocalDateTime getLastReminderSent() {
        return lastReminderSent;
    }

    public void setLastReminderSent(LocalDateTime lastReminderSent) {
        this.lastReminderSent = lastReminderSent;
    }

    @Override
    public String toString() {
        return "EventReminder{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", eventTime=" + eventTime +
                ", remindBeforeMinutes=" + remindBeforeMinutes +
                ", repeatAfterMinutes=" + repeatAfterMinutes +
                ", completed=" + completed +
                ", completionType=" + completionType +
                ", completedAt=" + completedAt +
                ", email='" + email + '\'' +
                ", notified=" + notified +
                ", lastReminderSent=" + lastReminderSent +
                '}';
    }
}
