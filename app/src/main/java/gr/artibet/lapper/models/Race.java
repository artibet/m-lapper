package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Race {

    // MEMBERS

    @SerializedName("id")
    private Integer id;

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

    @SerializedName("updated_at_ts")
    private Double updatedAtTs;

    // GETTERS

    public Integer getId() {
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

    public Double getUpdatedAtTs() {return updatedAtTs; }

    // SETTERS

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setLaps(int laps) {
        this.laps = laps;
    }

    public void setState(RaceState state) {
        this.state = state;
    }

    public void setStartMethod(RaceStartMethod startMethod) {
        this.startMethod = startMethod;
    }

    public void setMode(RaceMode mode) {
        this.mode = mode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setRvs(List<RaceVehicle> rvs) {
        this.rvs = rvs;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public void setFinishDt(String finishDt) {
        this.finishDt = finishDt;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setTotalVehicles(int totalVehicles) {
        this.totalVehicles = totalVehicles;
    }

    public void setRunningVehicles(int runningVehicles) {
        this.runningVehicles = runningVehicles;
    }

    public void setFinishedVehicles(int finishedVehicles) {
        this.finishedVehicles = finishedVehicles;
    }

    public void setCanceledVehicles(int canceledVehicles) {
        this.canceledVehicles = canceledVehicles;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setUpdatedAtTs(Double updatedAtTs) {
        this.updatedAtTs = updatedAtTs;
    }

    // Check if given userId has vehicle into race
    public boolean userHasVehicleIntoRace(long userId) {
        for (RaceVehicle rv : rvs) {
            if (rv.getVehicle().getOwner().getId() == userId) return true;
        }
        return false;
    }


}
