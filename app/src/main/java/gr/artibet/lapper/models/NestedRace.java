package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NestedRace {

    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("tag")
    private String tag;

    @SerializedName("laps")
    private int laps;

    @SerializedName("ispublic")
    private boolean isPublic;

    @SerializedName("vehicle_users")
    private List<Integer> vehicleUsers;


    // GETTERS

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public int getLaps() {
        return laps;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public List<Integer> getVehicleUsers() {
        return vehicleUsers;
    }
}
