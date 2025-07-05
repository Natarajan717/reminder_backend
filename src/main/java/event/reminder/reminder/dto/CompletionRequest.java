package event.reminder.reminder.dto;

public class CompletionRequest {
    private String completionType;
    private String note;

    public String getCompletionType() {
        return completionType;
    }

    public void setCompletionType(String completionType) {
        this.completionType = completionType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "CompletionRequest{" +
                "completionType='" + completionType + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}

