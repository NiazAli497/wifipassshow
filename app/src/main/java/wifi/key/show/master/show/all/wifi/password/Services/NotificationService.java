package wifi.key.show.master.show.all.wifi.password.Services;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}