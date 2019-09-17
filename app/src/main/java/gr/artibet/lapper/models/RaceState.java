package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class RaceState {

    // Static states ids
    public static final int STATE_PENDING = 0;
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_INPROGRESS = 2;
    public static final int STATE_COMPLETED = 3;
    public static final int STATE_CANCELED = 4;

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
