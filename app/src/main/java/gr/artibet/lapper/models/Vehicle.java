package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class Vehicle {

    // MEMBERS

    @SerializedName("id")
    private long id;

    @SerializedName("tag")
    private String tag;

    @SerializedName("owner")
    private User owner;

    @SerializedName("driver")
    private String driver;

    @SerializedName("descr")
    private String description;

    @SerializedName("isactive")
    private boolean isActive;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // GETTERS

    public long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public User getOwner() {
        return owner;
    }

    public String getDriver() {
        return driver;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
