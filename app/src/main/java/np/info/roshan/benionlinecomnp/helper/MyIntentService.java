package np.info.roshan.benionlinecomnp.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.activities.NewsDetails;


public class MyIntentService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        if (getSharedPreferences("settings", MODE_PRIVATE).getBoolean("notifications", true)) {

            NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this);
            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.setBigContentTitle(data.getString("message"));

            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);

            notificationCompat.setStyle(bigText);

            notificationCompat.setAutoCancel(true)
                    .setTicker("Benionline.com.np")
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Benionline.com.np")
                    .setSmallIcon(R.mipmap.ic_launcher).setVibrate(new long[]{100, 100})
                    .setLights(Color.BLUE, 3000, 3000)
                    .setSound(sound)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentText(data.getString("message"));


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), NewsDetails.class).putExtra("from", "notification").putExtra("post_id", Integer.parseInt(data.getString("id"))), PendingIntent.FLAG_UPDATE_CURRENT);

            notificationCompat.setContentIntent(pendingIntent);
            NotificationManager notificationManagerCompat = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManagerCompat.notify(new Random(3000).nextInt(), notificationCompat.build());
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getClass().getName());
            wl.acquire();
            try {
                Thread.sleep(7000);
            } catch (InterruptedException ignored) {
            }
            wl.release();
        }
    }
}
