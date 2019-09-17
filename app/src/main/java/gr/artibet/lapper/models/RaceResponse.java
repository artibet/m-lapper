package gr.artibet.lapper.models;

import java.util.List;

public class RaceResponse {

    private Integer count;
    private String next;
    private String previous;
    private List<Race> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Race> getResults() {
        return results;
    }

    public void setResults(List<Race> results) {
        this.results = results;
    }
}
