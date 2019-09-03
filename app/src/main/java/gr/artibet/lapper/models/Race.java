package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Race {

    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("tag")
    private String tag;

    @SerializedName("laps")
    private int laps;

    @SerializedName("state")
    private RaceState state;

    @SerializedName("start")
    private RaceStartMethod startMethod;

    @SerializedName("mode")
    private RaceMode mode;

    @SerializedName("descr")
    private String description;

    @SerializedName("creator")
    private User creator;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("rvs")
    private List<RaceVehicle> rvs;

    @SerializedName("start_dt")
    private String startDt;

    @SerializedName("finish_dt")
    private String finishDt;

    @SerializedName("duration")
    private String duration;

    @SerializedName("total_vehicles")
    private int totalVehicles;

    @SerializedName("running_vehicles")
    private int runningVehicles;

    @SerializedName("finished_vehicles")
    private int finishedVehicles;

    @SerializedName("canceled_vehicles")
    private int canceledVehicles;

    @SerializedName("lap")
    private int lap;

    @SerializedName("ispublic")
    private boolean isPublic;

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

    public RaceState getState() {
        return state;
    }

    public RaceStartMethod getStartMethod() {
        return startMethod;
    }

    public RaceMode getMode() {
        return mode;
    }

    public String getDescription() {
        return description;
    }

    public User getCreator() {
        return creator;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public List<RaceVehicle> getRvs() {
        return rvs;
    }

    public String getStartDt() {
        return startDt;
    }

    public String getFinishDt() {
        return finishDt;
    }

    public String getDuration() {
        return duration;
    }

    public int getTotalVehicles() {
        return totalVehicles;
    }

    public int getRunningVehicles() {
        return runningVehicles;
    }

    public int getFinishedVehicles() {
        return finishedVehicles;
    }

    public int getCanceledVehicles() {
        return canceledVehicles;
    }

    public int getLap() {
        return lap;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
