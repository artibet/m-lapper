package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class Sensor {

    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("aa")
    private int aa;

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

    public int getId() {
        return id;
    }

    public int getAa() {
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
