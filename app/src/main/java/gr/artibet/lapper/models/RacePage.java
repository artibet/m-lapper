package gr.artibet.lapper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RacePage {

    // MEMBERS

    @SerializedName("count")
    private int count;

    @SerializedName("next")
    private String next;

    @SerializedName("previous")
    private String previous;

    @SerializedName("results")
    private List<Race> races;

    // GETTERS

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<Race> getRaces() {
        return races;
    }
}
