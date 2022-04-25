package com.amigos.activitytracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent newIntent = new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =  PendingIntent.getActivity(context,100,newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context,"Remainder");
        notification.setSmallIcon(R.drawable.ic_baseline_keyboard_voice_24);
        notification.setContentTitle("Time to record Activity");
        notification.setContentText("Please open the app and record your current activity");
        notification.setAutoCancel(true);
        notification.setContentIntent(pendingIntent);
//        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        notificationManager.notify(200,notification.build());

    }
}
