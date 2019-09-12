package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

public class Sensor {

    // MEMBERS

    @SerializedName("id")
    private Long id;

    @SerializedName("aa")
    private Long aa;

    @SerializedName("tag")
    private String tag;

    @SerializedName("threshold")
    private Integer threshold;

    @SerializedName("isactive")
    private boolean isActive;

    @SerializedName("isstart")
    private boolean isStart;

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

    public Long getAa() {
        return aa;
    }

    public String getTag() {
        return tag;
    }

    public Integer getThreshold() {
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

    public String getUpdatedAtFormated() {return updatedAtFormated; }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setAa(Long aa) {
        this.aa = aa;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setStart(boolean start) {
        isStart = start;
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
