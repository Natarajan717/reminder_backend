package event.reminder.reminder.scheduler;

import event.reminder.reminder.entity.EventReminder;
import event.reminder.reminder.enums.CompletionType;
import event.reminder.reminder.repository.EventReminderRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final EventReminderRepository repository;
    private final JavaMailSender mailSender;

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        List<EventReminder> upcoming = repository.findByCompletedFalse();
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        for (EventReminder event : upcoming) {
            LocalDateTime eventTime = event.getEventTime();
            LocalDateTime firstReminderTime = eventTime.minusMinutes(event.getRemindBeforeMinutes());

            // 1. First reminder
            if (!event.isNotified() && now.isAfter(firstReminderTime) && now.isBefore(eventTime)) {
                sendEmail(event, "Upcoming Event Reminder: " + event.getTitle(), "Event at: " + eventTime + "\n" + event.getNote());
                event.setNotified(true);
                event.setLastReminderSent(now);
                repository.save(event);
            }

            // 2. Repeating reminders before event time
            if (event.isNotified() && now.isBefore(eventTime)) {
                if (event.getLastReminderSent() == null || ChronoUnit.MINUTES.between(event.getLastReminderSent(), now) >= event.getRepeatAfterMinutes()) {
                    sendEmail(event, "Repeat Reminder: " + event.getTitle(), "Reminder: " + event.getNote());
                    event.setLastReminderSent(now);
                    repository.save(event);
                }
            }

            // 3. Notify at exact event time
            if (now.truncatedTo(ChronoUnit.MINUTES).equals(eventTime.truncatedTo(ChronoUnit.MINUTES))) {
                sendEmail(event, "Event Started: " + event.getTitle(), "Your event has started now. Don't forget to update it afterward.");
            }

            // 4. Mark as completed (move to history) after event time, without setting type
            if (now.isAfter(eventTime) && !event.isCompleted()) {
                event.setCompleted(true);
                repository.save(event);
            }
        }
    }

    private void sendEmail(EventReminder event, String subject, String content) {
        try {
            var msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, true);
            helper.setTo(event.getEmail());
            helper.setSubject(subject);
            helper.setText(content);
            mailSender.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
