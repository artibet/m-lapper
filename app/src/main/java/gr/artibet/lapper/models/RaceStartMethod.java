package gr.artibet.lapper.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import gr.artibet.lapper.App;
import gr.artibet.lapper.R;

public class RaceStartMethod {

    // MEMBERS

    @SerializedName("id")
    private int id;

    @SerializedName("descr")
    private String description;

    // Defafult constractor
    public RaceStartMethod() {}

    // Constructor to initialize members
    public RaceStartMethod(int id, String description) {
        this.id = id;
        this.description = description;
    }

    // GETTERS

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    // Create and return a list of available start methods
    public static List<RaceStartMethod> getList() {
        RaceStartMethod auto = new RaceStartMethod(0, App.context.getString(R.string.start_method_auto));
        RaceStartMethod manual = new RaceStartMethod(1, App.context.getString(R.string.start_method_manual));

        ArrayList<RaceStartMethod> startMethodList = new ArrayList<>();
        startMethodList.add(auto);
        startMethodList.add(manual);

        return startMethodList;
    }

    @NonNull
    @Override
    public String toString() {
        return this.description;
    }
}
