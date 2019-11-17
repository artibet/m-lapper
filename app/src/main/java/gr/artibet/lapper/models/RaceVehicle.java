package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class RaceVehicle {

    // MEMBERS

    @SerializedName("id")
    private long id;

    @SerializedName("race_id")
    private long raceId;

    @SerializedName("race_creator_id")
    private long raceCreatorId;

    @SerializedName("race_ispublic")
    private boolean raceIsPublic;

    @SerializedName("race_tag")
    private String raceTag;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    @SerializedName("state")
    private RaceVehicleState state;

    @SerializedName("prev_sensor")
    private Sensor prevSensor;

    @SerializedName("last_sensor")
    private Sensor lastSensor;

    @SerializedName("start_ts")
    private Double startTs;

    @SerializedName("finish_ts")
    private Double finishTs;

    @SerializedName("prev_ts")
    private Double prevTs;

    @SerializedName("last_ts")
    private Double lastTs;

    @SerializedName("interval_str")
    private String IntervalString;

    @SerializedName("best_interval_str")
    private String bestIntervalString;

    @SerializedName("lap")
    private int lap;

    @SerializedName("lap_interval_str")
    private String lapIntervalString;

    @SerializedName("best_lap_interval_str")
    private String bestLapIntervalString;

    @SerializedName("uflag")
    private boolean uflag;

    @SerializedName("duration")
    private String duration;

    @SerializedName("driver")
    private String driver;


    // GETTERS

    public long getId() {
        return id;
    }

    public long getRaceId() {
        return raceId;
    }

    public long getRaceCreatorId() {
        return raceCreatorId;
    }

    public boolean isRaceIsPublic() {
        return raceIsPublic;
    }

    public String getRaceTag() {
        return raceTag;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public RaceVehicleState getState() {
        return state;
    }

    public Sensor getPrevSensor() {
        return prevSensor;
    }

    public Sensor getLastSensor() {
        return lastSensor;
    }

    public Double getStartTs() {
        return startTs;
    }

    public Double getFinishTs() {
        return finishTs;
    }

    public Double getPrevTs() {
        return prevTs;
    }

    public Double getLastTs() {
        return lastTs;
    }

    public String getIntervalString() {
        return IntervalString;
    }

    public String getBestIntervalString() {
        return bestIntervalString;
    }

    public int getLap() {
        return lap;
    }

    public String getLapIntervalString() {
        return lapIntervalString;
    }

    public String getBestLapIntervalString() {
        return bestLapIntervalString;
    }

    public boolean isUflag() {
        return uflag;
    }

    public String getDuration() {
        return duration;
    }

    public String getDriver() {
        return driver;
    }

    // SETTERS

    public void setId(long id) {
        this.id = id;
    }

    public void setRaceId(long raceId) {
        this.raceId = raceId;
    }

    public void setRaceCreatorId(long raceCreatorId) {
        this.raceCreatorId = raceCreatorId;
    }

    public void setRaceIsPublic(boolean raceIsPublic) {
        this.raceIsPublic = raceIsPublic;
    }

    public void setRaceTag(String raceTag) {
        this.raceTag = raceTag;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setState(RaceVehicleState state) {
        this.state = state;
    }

    public void setPrevSensor(Sensor prevSensor) {
        this.prevSensor = prevSensor;
    }

    public void setLastSensor(Sensor lastSensor) {
        this.lastSensor = lastSensor;
    }

    public void setStartTs(Double startTs) {
        this.startTs = startTs;
    }

    public void setFinishTs(Double finishTs) {
        this.finishTs = finishTs;
    }

    public void setPrevTs(Double prevTs) {
        this.prevTs = prevTs;
    }

    public void setLastTs(Double lastTs) {
        this.lastTs = lastTs;
    }

    public void setIntervalString(String intervalString) {
        IntervalString = intervalString;
    }

    public void setBestIntervalString(String bestIntervalString) {
        this.bestIntervalString = bestIntervalString;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public void setLapIntervalString(String lapIntervalString) {
        this.lapIntervalString = lapIntervalString;
    }

    public void setBestLapIntervalString(String bestLapIntervalString) {
        this.bestLapIntervalString = bestLapIntervalString;
    }

    public void setUflag(boolean uflag) {
        this.uflag = uflag;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
