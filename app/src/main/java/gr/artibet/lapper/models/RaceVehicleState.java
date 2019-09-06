package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class RaceVehicleState {

    // STATES
    public static final int STATE_WAITING = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_CANCELED = 3;


    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("descr")
    private String description;

    // GETTERS

    public int getId() {

        return id;
    }

    public String getDescription() {

        return description;
    }
}
