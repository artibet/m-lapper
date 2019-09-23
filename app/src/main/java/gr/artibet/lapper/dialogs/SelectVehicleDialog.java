package gr.artibet.lapper.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.List;

import gr.artibet.lapper.App;
import gr.artibet.lapper.R;
import gr.artibet.lapper.Util;
import gr.artibet.lapper.activities.VehicleFormActivity;
import gr.artibet.lapper.api.RetrofitClient;
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.models.User;
import gr.artibet.lapper.models.Vehicle;
import gr.artibet.lapper.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectVehicleDialog extends AppCompatDialogFragment {

    // class members
    private LinearLayout mMainLayout;
    private ProgressBar mProgressBar;
    private Spinner mSpinnerVehicle;
    private EditText mEditTextDriver;
    private SelectVehicleListener mListener;

    private int mRaceId;
    private List<Vehicle> mAvailableVehicles;

    // Set listener
    public void setSelectVehicleListener(SelectVehicleListener mListener) {
        this.mListener = mListener;
    }

    // Constructor takes Race object
    public SelectVehicleDialog(int raceId) {
        mRaceId = raceId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_vehicle, null);

        // Initialize views
        mMainLayout = view.findViewById(R.id.main_layout);
        mProgressBar = view.findViewById(R.id.progressBar);
        mSpinnerVehicle = view.findViewById(R.id.spVehicle);
        mEditTextDriver = view.findViewById(R.id.etVehicleDriver);

        // Fetch available vehicles and initialize spinner
        fetchAvailableVehicles();

        // Build dialog
        builder.setView(view);
        builder.setTitle(getString(R.string.add_vehicle));
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.add), null);

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button okButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // If no vehicle  selected do not dismiss dialog
                        Vehicle vehicle = (Vehicle)mSpinnerVehicle.getSelectedItem();
                        if (vehicle == null) {
                            // TODO: Set error message
                            return;
                        }

                        // Dismiss dialog and call listener
                        dialog.dismiss();
                        mListener.onSelectVehicle(vehicle, mEditTextDriver.getText().toString());
                    }
                });
            }
        });

        // Return dialog
        return alertDialog;

    }

    // Fetch available vehicles for given race
    private void fetchAvailableVehicles() {

        // Hide main layout and display progress
        mMainLayout.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        // Fetch available vehicles and initialize spinner
        Call<List<Vehicle>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAvailableVehicles(SharedPrefManager.getInstance(getActivity()).getToken(), mRaceId);

        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {

                if (!response.isSuccessful()) {
                    Util.errorToast(getActivity(), response.message());
                }
                else {

                    // Init spinner
                    mAvailableVehicles = response.body();
                    ArrayAdapter<Vehicle> adapter = new ArrayAdapter<Vehicle>(App.context, android.R.layout.simple_spinner_item, mAvailableVehicles);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinnerVehicle.setAdapter(adapter);

                    // Set spinner on item selected listener
                    mSpinnerVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Vehicle vehicle = (Vehicle)mSpinnerVehicle.getSelectedItem();
                            mEditTextDriver.setText(vehicle.getDriver());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    // Display main layout
                    mMainLayout.setVisibility(View.VISIBLE);
                }

                // Hide progress bar
                mProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                Util.errorToast(getActivity(), t.getMessage());
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    // Create interface to communicate with parent
    public interface SelectVehicleListener {
        void onSelectVehicle(Vehicle vehicle, String driver);
    }
}
