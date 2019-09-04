package gr.artibet.lapper.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.artibet.lapper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveRacesFragment extends MyFragment {


    public ActiveRacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_races, container, false);
    }

    @Override
    public void refresh() {

    }
}
