package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class LiveData {

    // MEMBERS

    @SerializedName("")
    private int id;

    @SerializedName("")
    private int rvId;

    @SerializedName("")
    private NestedRace race;

    @SerializedName("")
    private Vehicle vehicle;

    @SerializedName("")
    private RaceVehicleState rvState;

    @SerializedName("")
    private Sensor prevSensor;

    @SerializedName("")
    private Sensor lastSensor;

    @SerializedName("")
    private double prevTs;

    @SerializedName("")
    private double lastTs;

    @SerializedName("")
    private String lastDt;

    @SerializedName("")
    private int lap;

    @SerializedName("")
    private double interval;

    @SerializedName("")
    private String intervalString;

    @SerializedName("")
    private String bestIntervalString;

    @SerializedName("")
    private double prevInterval;

    @SerializedName("")
    private double lapInterval;

    @SerializedName("")
    private String lapIntervalString;

    @SerializedName("")
    private String bestLapIntervalString;

    @SerializedName("")
    private double prevLapInterval;

    @SerializedName("")
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
