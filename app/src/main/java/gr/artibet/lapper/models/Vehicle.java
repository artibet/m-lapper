package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class Vehicle {

    // MEMBERS

    @SerializedName("id")
    private Long id;

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

    @SerializedName("updated_at_formated")
    private String updatedAtFormated;

    // GETTERS

    public Long getId() {
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

    public String getUpdatedAtFormated() {
        return updatedAtFormated;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAtFormated(String updatedAtFormated) {
        this.updatedAtFormated = updatedAtFormated;
    }
}
