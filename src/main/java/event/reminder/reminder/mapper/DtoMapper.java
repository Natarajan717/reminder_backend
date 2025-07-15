package event.reminder.reminder.mapper;

import event.reminder.reminder.dto.*;
import event.reminder.reminder.entity.*;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static AppUserDTO toUserDTO(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        if (user.getReminderList() != null) {
            dto.setReminders(user.getReminderList().stream()
                    .map(DtoMapper::toReminderDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static EventReminderDTO toReminderDTO(EventReminder r) {
        EventReminderDTO dto = new EventReminderDTO();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setNote(r.getNote());
        dto.setEventTime(r.getEventTime());
        dto.setRemindBeforeMinutes(r.getRemindBeforeMinutes());
        dto.setRepeatAfterMinutes(r.getRepeatAfterMinutes());
        dto.setCompleted(r.isCompleted());
        dto.setCompletionType(r.getCompletionType());
        dto.setCompletedAt(r.getCompletedAt());
        dto.setNotified(r.isNotified());
        dto.setLastReminderSent(r.getLastReminderSent());
        dto.setUserId(r.getAppUser().getId());
        return dto;
    }

    public static AppUser toEntity(AppUserDTO dto) {
        return AppUser.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static EventReminder toEntity(EventReminderDTO dto, AppUser user) {
        return EventReminder.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .note(dto.getNote())
                .eventTime(dto.getEventTime())
                .remindBeforeMinutes(dto.getRemindBeforeMinutes())
                .repeatAfterMinutes(dto.getRepeatAfterMinutes())
                .completed(dto.isCompleted())
                .completionType(dto.getCompletionType())
                .completedAt(dto.getCompletedAt())
                .notified(dto.isNotified())
                .lastReminderSent(dto.getLastReminderSent())
                .appUser(user)
                .build();
    }
}
