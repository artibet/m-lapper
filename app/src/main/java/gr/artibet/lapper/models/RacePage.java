package gr.artibet.lapper.models;

import java.util.List;

public class RacePage {

    // MEMBERS

    private int count;
    private String next;
    private String previous;
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
