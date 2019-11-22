package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class Checkpoint {

    // MEMBERS

    @SerializedName("id")
    private long id;

    @SerializedName("rv")
    private long rvId;

    @SerializedName("prev_sensor")
    private Sensor prevSensor;

    @SerializedName("last_sensor")
    private Sensor lastSensor;

    @SerializedName("prev_ts")
    private Double prevTs;

    @SerializedName("last_ts")
    private Double lastTs;

    @SerializedName("interval")
    private Double interval;

    @SerializedName("prev_interval")
    private Double prevInterval;

    @SerializedName("best_interval")
    private Double bestInterval;

    @SerializedName("lap")
    private int lap;

    @SerializedName("lap_start_ts")
    private Double lapStartTs;

    @SerializedName("lap_interval")
    private Double lapInterval;

    @SerializedName("prev_lap_interval")
    private Double prevLapInterval;

    @SerializedName("best_lap_interval")
    private Double bestLapInterval;

    @SerializedName("last_dt")
    private String lastDt;

    @SerializedName("interval_str")
    private String IntervalString;

    @SerializedName("lap_interval_str")
    private String lapIntervalString;


    // GETTERS & SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRvId() {
        return rvId;
    }

    public void setRvId(long rvId) {
        this.rvId = rvId;
    }

    public Sensor getPrevSensor() {
        return prevSensor;
    }

    public void setPrevSensor(Sensor prevSensor) {
        this.prevSensor = prevSensor;
    }

    public Sensor getLastSensor() {
        return lastSensor;
    }

    public void setLastSensor(Sensor lastSensor) {
        this.lastSensor = lastSensor;
    }

    public Double getPrevTs() {
        return prevTs;
    }

    public void setPrevTs(Double prevTs) {
        this.prevTs = prevTs;
    }

    public Double getLastTs() {
        return lastTs;
    }

    public void setLastTs(Double lastTs) {
        this.lastTs = lastTs;
    }

    public Double getInterval() {
        return interval;
    }

    public void setInterval(Double interval) {
        this.interval = interval;
    }

    public Double getPrevInterval() {
        return prevInterval;
    }

    public void setPrevInterval(Double prevInterval) {
        this.prevInterval = prevInterval;
    }

    public Double getBestInterval() {
        return bestInterval;
    }

    public void setBestInterval(Double bestInterval) {
        this.bestInterval = bestInterval;
    }

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public Double getLapStartTs() {
        return lapStartTs;
    }

    public void setLapStartTs(Double lapStartTs) {
        this.lapStartTs = lapStartTs;
    }

    public Double getLapInterval() {
        return lapInterval;
    }

    public void setLapInterval(Double lapInterval) {
        this.lapInterval = lapInterval;
    }

    public Double getPrevLapInterval() {
        return prevLapInterval;
    }

    public void setPrevLapInterval(Double prevLapInterval) {
        this.prevLapInterval = prevLapInterval;
    }

    public Double getBestLapInterval() {
        return bestLapInterval;
    }

    public void setBestLapInterval(Double bestLapInterval) {
        this.bestLapInterval = bestLapInterval;
    }

    public String getLastDt() {
        return lastDt;
    }

    public void setLastDt(String lastDt) {
        this.lastDt = lastDt;
    }

    public String getIntervalString() {
        return IntervalString;
    }

    public void setIntervalString(String intervalString) {
        IntervalString = intervalString;
    }

    public String getLapIntervalString() {
        return lapIntervalString;
    }

    public void setLapIntervalString(String lapIntervalString) {
        this.lapIntervalString = lapIntervalString;
    }
}
