package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleData {

    @SerializedName("race")
    private Race race;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    @SerializedName("driver")
    private String driver;

    @SerializedName("state")
    private RaceVehicleState state;

    @SerializedName("duration")
    private String duration;

    @SerializedName("rank")
    private Integer rank;

    @SerializedName("checkpoints")
    private List<Checkpoint> checkpoints;

    // GETTERS & SETTERS

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public RaceVehicleState getState() {
        return state;
    }

    public void setState(RaceVehicleState state) {
        this.state = state;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}



