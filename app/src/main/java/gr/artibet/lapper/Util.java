package gr.artibet.lapper;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Util {

    // ---------------------------------------------------------------------------------------
    // Toast error message
    // ---------------------------------------------------------------------------------------
    public static void errorToast(Activity activity, String message) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_error, (ViewGroup) activity.findViewById(R.id.error_toast_root));

        TextView text = layout.findViewById(R.id.error_toast_text);
        text.setText(message);
        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    // ---------------------------------------------------------------------------------------
    // Toast success message
    // ---------------------------------------------------------------------------------------
    public static void successToast(Activity activity, String message) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_success, (ViewGroup) activity.findViewById(R.id.success_toast_root));

        TextView text = layout.findViewById(R.id.success_toast_text);
        text.setText(message);
        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    // ---------------------------------------------------------------------------------------
    // Convert timestamp to HH:mm:ss.mmm
    // ---------------------------------------------------------------------------------------
    public static String TimestampToTime(double timestamp) {
        long timeMills = (long)(timestamp * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMills);
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(tz);
        return sdf.format(timeMills);
    }

    // ---------------------------------------------------------------------------------------
    // Convert datetime ISO into dd/mm/yyyy, HH:mm
    // ---------------------------------------------------------------------------------------
    public static String isoDateToString(String isoDate) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        sdf.setTimeZone(tz);
        return sdf.format(isoDate);
    }

    // ---------------------------------------------------------------------------------------
    // Send Notification message
    // ---------------------------------------------------------------------------------------
    public static void sendNotification(Context context, String channelId, int id, String title, String message) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_active_races)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(id, notification);

    }
}
