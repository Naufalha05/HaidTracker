package com.example.haidtracker.ui.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.haidtracker.R;
import com.example.haidtracker.ui.siklus.SiklusActivity;


public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "SiklusReminderChannel";
    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String KEY_NOTIFICATION_TYPE = "notification_type";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String notificationType = sharedPreferences.getString(KEY_NOTIFICATION_TYPE, "Suara & Getar");

        showNotification(context, notificationType);
    }

    private void showNotification(Context context, String notificationType) {
        Intent intent = new Intent(context, SiklusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Pengingat Siklus Menstruasi")
                .setContentText("Siklus menstruasi Anda akan dimulai dalam beberapa hari")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Halo! Berdasarkan prediksi, siklus menstruasi Anda akan dimulai dalam beberapa hari. Pastikan Anda sudah mempersiapkan kebutuhan yang diperlukan."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Handle notification type
        handleNotificationType(context, builder, notificationType);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void handleNotificationType(Context context, NotificationCompat.Builder builder, String notificationType) {
        switch (notificationType) {
            case "Hanya Suara":
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(soundUri);
                break;

            case "Hanya Getar":
                builder.setVibrate(new long[]{0, 1000, 500, 1000});
                break;

            case "Suara & Getar":
                Uri soundUri2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(soundUri2);
                builder.setVibrate(new long[]{0, 1000, 500, 1000});
                break;

            case "Hening":
                // No sound or vibration
                builder.setSilent(true);
                break;
        }
    }
}