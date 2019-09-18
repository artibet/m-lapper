package gr.artibet.lapper;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import gr.artibet.lapper.api.SocketIO;
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.storage.SharedPrefManager;


public class App extends Application {

    public static final String CHANNEL_RACE_ID = "Race channel";

    // Global context
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        App.context = getApplicationContext();
        createNotificationChannels();

        // Subscribe on "checkpoint" socket message
        SocketIO.getInstance().getSocket().on("race_started", onRaceStart);
        SocketIO.getInstance().getSocket().on("race_activated", onRaceActivated);
        SocketIO.getInstance().getSocket().on("race_deactivated", onRaceDeactivated);


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SocketIO.getInstance().getSocket().off("race_started", onRaceStart);
        SocketIO.getInstance().getSocket().off("race_activated", onRaceActivated);
        SocketIO.getInstance().getSocket().off("race_deactivated", onRaceDeactivated);
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   // SDK >= OREO

            // Race events
            NotificationChannel raceChannel = new NotificationChannel(
                    CHANNEL_RACE_ID,
                    "Race events",
                    NotificationManager.IMPORTANCE_HIGH
            );
            raceChannel.setDescription("Race events");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(raceChannel);
        }
    }

    // SOCKET MESSAGE: Race Activated
    private Emitter.Listener onRaceActivated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            // Send notification
            Gson gson = new Gson();
            Race race = gson.fromJson(args[0].toString(), Race.class);

            // Check if connected user has permission to receive notification
            long connectedUserΙd = SharedPrefManager.getInstance(App.context).getLoggedInUser().getId();
            if (SharedPrefManager.getInstance(App.context).isAdmin() || race.isPublic() || race.userHasVehicleIntoRace(connectedUserΙd)) {
                String title = getString(R.string.activate_race_title);
                String message = getString(R.string.activate_race_text, race.getTag());
                Util.sendNotification(App.context, App.CHANNEL_RACE_ID, race.getId(), title, message);
            }
         }
    };

    // SOCKET MESSAGE: Race deactivated
    private Emitter.Listener onRaceDeactivated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            // Send notification
            Gson gson = new Gson();
            Race race = gson.fromJson(args[0].toString(), Race.class);

            // Check if connected user has permission to receive notification
            long connectedUserΙd = SharedPrefManager.getInstance(App.context).getLoggedInUser().getId();
            if (SharedPrefManager.getInstance(App.context).isAdmin() || race.isPublic() || race.userHasVehicleIntoRace(connectedUserΙd)) {
                String title = getString(R.string.deactivate_race_title);
                String message = getString(R.string.deactivate_race_text, race.getTag());
                Util.sendNotification(App.context, App.CHANNEL_RACE_ID, race.getId(), title, message);
            }
        }
    };

    // SOCKET MESSAGE: Race started
    private Emitter.Listener onRaceStart = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            // Send notification
            Gson gson = new Gson();
            Race race = gson.fromJson(args[0].toString(), Race.class);

            long connectedUserΙd = SharedPrefManager.getInstance(App.context).getLoggedInUser().getId();
            if (SharedPrefManager.getInstance(App.context).isAdmin() || race.isPublic() || race.userHasVehicleIntoRace(connectedUserΙd)) {
                String title = getString(R.string.start_race_title);
                String message = getString(R.string.start_race_text, race.getTag());
                Util.sendNotification(App.context, App.CHANNEL_RACE_ID, race.getId(), title, message);
            }

        }
    };


}
