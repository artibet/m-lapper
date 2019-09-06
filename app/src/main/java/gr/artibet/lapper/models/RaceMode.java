package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class RaceMode {

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
