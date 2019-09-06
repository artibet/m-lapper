package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NestedRace {

    // MEMBERS

    @SerializedName("id")
    private long id;

    @SerializedName("tag")
    private String tag;

    @SerializedName("laps")
    private int laps;

    @SerializedName("ispublic")
    private boolean isPublic;

    @SerializedName("vehicle_users")
    private List<Integer> vehicleUsers;


    // GETTERS

    public long getId() {
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

    // Check if given user is into list of vehicleUsers
    public boolean hasVehicle(long userId) {
        for (long id : vehicleUsers) {
            if (id == userId) return true;
        }
        return false;
    }
}
