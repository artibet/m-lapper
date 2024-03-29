package gr.artibet.lapper;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import gr.artibet.lapper.models.RaceVehicle;
import gr.artibet.lapper.models.RaceVehicleState;

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
    public static String timestampToTime(Double timestamp) {
        if (timestamp == null) return "";
        long timeMills = (long)(timestamp * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMills);
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(tz);
        return sdf.format(timeMills);
    }

    // ---------------------------------------------------------------------------------------
    // Convert timestamp to DD/MM/YYYY, HH:mm:ss.mmm
    // IF seconds is true show HH:mm:ss else HH:mm (defalute is false)
    // ---------------------------------------------------------------------------------------
    public static String timestampToDatetime(Double timestamp, boolean seconds) {
        if (timestamp == null) return "";
        long timeMills = (long)(timestamp * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMills);
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = null;
        if (seconds) {
            sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
        }
        else {
            sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        }
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

    // ---------------------------------------------------------------------------------------
    // Enable popup menu icons via reflection
    // ---------------------------------------------------------------------------------------
    public static void enablePopupIcons(PopupMenu popup) {
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ---------------------------------------------------------------------------------------
    // Sort a list of RaceVehicles that are in progress
    // ---------------------------------------------------------------------------------------
    public static void sortVehicleList(List<RaceVehicle> rvList) {
        Collections.sort(rvList, new Comparator<RaceVehicle>() {
            @Override
            public int compare(RaceVehicle lhs, RaceVehicle rhs) {
                int lhsState = lhs.getState().getId();
                int rhsState = rhs.getState().getId();
                if (lhsState == RaceVehicleState.STATE_FINISHED && rhsState != RaceVehicleState.STATE_FINISHED) return -1;
                if (lhsState != RaceVehicleState.STATE_FINISHED && rhsState == RaceVehicleState.STATE_FINISHED) return 1;
                if (lhsState == RaceVehicleState.STATE_CANCELED && rhsState != RaceVehicleState.STATE_CANCELED) return 1;
                if (lhsState != RaceVehicleState.STATE_CANCELED && rhsState == RaceVehicleState.STATE_CANCELED) return -1;
                if (lhs.getLap() > rhs.getLap()) return -1;
                if (lhs.getLap() < rhs.getLap()) return 1;
                if (lhs.getLastSensor().getAa() > rhs.getLastSensor().getAa()) return -1;
                if (lhs.getLastSensor().getAa() < rhs.getLastSensor().getAa()) return 1;
                return (int)(lhs.getLastTs() - rhs.getLastTs());
            }
        });
    }

    // ---------------------------------------------------------------------------------------
    // Sort a list of completed or canceled RaceVehicles
    // ---------------------------------------------------------------------------------------
    public static void sortCompletedVehicleList(List<RaceVehicle> rvList) {
        Collections.sort(rvList, new Comparator<RaceVehicle>() {
            @Override
            public int compare(RaceVehicle lhs, RaceVehicle rhs) {
                int lhsState = lhs.getState().getId();
                int rhsState = rhs.getState().getId();

                // If both finished, first is the vehicle with less duration
                if (lhsState == RaceVehicleState.STATE_FINISHED && rhsState == RaceVehicleState.STATE_FINISHED) return (int)(lhs.getFinishTs() - rhs.getFinishTs());

                // If both canceled, first is the vehicle with greater duration (canceled later)
                if (lhsState == RaceVehicleState.STATE_CANCELED && rhsState == RaceVehicleState.STATE_CANCELED) return (int)(rhs.getFinishTs() - lhs.getFinishTs());

                // Finished vehicles before canceled
                if (lhsState == RaceVehicleState.STATE_FINISHED && rhsState != RaceVehicleState.STATE_FINISHED) return -1;
                if (lhsState != RaceVehicleState.STATE_FINISHED && rhsState == RaceVehicleState.STATE_FINISHED) return 1;

                return 0;
            }
        });
    }
}
