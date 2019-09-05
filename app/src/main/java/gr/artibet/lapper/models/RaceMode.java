package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class RaceMode {

    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("descr")
    private int description;

    // GETTERS

    public int getId() {
        return id;
    }

    public int getDescription() {
        return description;
    }
}