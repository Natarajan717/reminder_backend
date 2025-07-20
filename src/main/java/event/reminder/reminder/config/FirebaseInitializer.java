package event.reminder.reminder.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import event.reminder.reminder.controller.EventReminderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class FirebaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseInitializer.class);

    @PostConstruct
    public void initialize() throws IOException {
       InputStream serviceAccount =
    getClass().getClassLoader().getResourceAsStream("event-reminder-74c72-firebase-adminsdk-fbsvc-228a4fb8d3.json");

if (serviceAccount == null) {
    throw new RuntimeException("Firebase credentials file not found in resources!");
}

FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

FirebaseApp.initializeApp(options);
    }
}

