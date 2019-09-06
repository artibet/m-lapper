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
}
