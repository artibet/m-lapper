package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class Sensor {

    // MEMBERS

    @SerializedName("id")
    private long id;

    @SerializedName("aa")
    private long aa;

    @SerializedName("tag")
    private String tag;

    @SerializedName("threshold")
    private int threshold;

    @SerializedName("isactive")
    private boolean isActive;

    @SerializedName("isstart")
    private boolean isStart;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // GETTERS

    public long getId() {
        return id;
    }

    public long getAa() {
        return aa;
    }

    public String getTag() {
        return tag;
    }

    public int getThreshold() {
        return threshold;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isStart() {
        return isStart;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
