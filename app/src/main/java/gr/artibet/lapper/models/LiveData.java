package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class LiveData {

    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("rv")
    private int rvId;

    @SerializedName("race")
    private NestedRace race;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    @SerializedName("state")
    private RaceVehicleState rvState;

    @SerializedName("prev_sensor")
    private Sensor prevSensor;

    @SerializedName("last_sensor")
    private Sensor lastSensor;

    @SerializedName("prev_ts")
    private double prevTs;

    @SerializedName("last_ts")
    private double lastTs;

    @SerializedName("last_dt")
    private String lastDt;

    @SerializedName("lap")
    private int lap;

    @SerializedName("interval")
    private double interval;

    @SerializedName("interval_str")
    private String intervalString;

    @SerializedName("best_interval_str")
    private String bestIntervalString;

    @SerializedName("prev_interval")
    private double prevInterval;

    @SerializedName("lap_interval")
    private double lapInterval;

    @SerializedName("lap_interval_str")
    private String lapIntervalString;

    @SerializedName("best_lap_interval_str")
    private String bestLapIntervalString;

    @SerializedName("prev_lap_interval")
    private double prevLapInterval;

    @SerializedName("uflag")
    private boolean uflag;

    // GETTERS

    public int getId() {
        return id;
    }

    public int getRvId() {
        return rvId;
    }

    public NestedRace getRace() {
        return race;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public RaceVehicleState getRvState() {
        return rvState;
    }

    public Sensor getPrevSensor() {
        return prevSensor;
    }

    public Sensor getLastSensor() {
        return lastSensor;
    }

    public double getPrevTs() {
        return prevTs;
    }

    public double getLastTs() {
        return lastTs;
    }

    public String getLastDt() {
        return lastDt;
    }

    public int getLap() {
        return lap;
    }

    public double getInterval() {
        return interval;
    }

    public String getIntervalString() {
        return intervalString;
    }

    public String getBestIntervalString() {
        return bestIntervalString;
    }

    public double getPrevInterval() {
        return prevInterval;
    }

    public double getLapInterval() {
        return lapInterval;
    }

    public String getLapIntervalString() {
        return lapIntervalString;
    }

    public String getBestLapIntervalString() {
        return bestLapIntervalString;
    }

    public double getPrevLapInterval() {
        return prevLapInterval;
    }

    public boolean isUflag() {
        return uflag;
    }
}
