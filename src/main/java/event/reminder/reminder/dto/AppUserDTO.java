package event.reminder.reminder.dto;

import lombok.Data;
import java.util.List;

@Data
public class AppUserDTO {
    private Long id;
    private String name;
    private String email;
    private List<EventReminderDTO> reminders;
}