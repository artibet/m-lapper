package gr.artibet.lapper.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.App;
import gr.artibet.lapper.R;

public class RaceMode {

    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("descr")
    private String description;

    // Defafult constractor
    public RaceMode() {}

    // Constructor to initialize members
    public RaceMode(int id, String description) {
        this.id = id;
        this.description = description;
    }

    // GETTERS

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    // Create and return a list of available modes
    public static List<RaceMode> getList() {
        RaceMode both = new RaceMode(0, App.context.getString(R.string.race_mode_both));
        RaceMode sensor = new RaceMode(1, App.context.getString(R.string.race_mode_sensor));
        RaceMode vehicle = new RaceMode(2, App.context.getString(R.string.race_mode_vehicle));

        ArrayList<RaceMode> modeList = new ArrayList<>();
        modeList.add(both);
        modeList.add(sensor);
        modeList.add(vehicle);

        return modeList;
    }

    @NonNull
    @Override
    public String toString() {
        return this.description;
    }
}
