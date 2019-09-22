package gr.artibet.lapper.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.List;

import gr.artibet.lapper.R;
import gr.artibet.lapper.models.Race;
import gr.artibet.lapper.models.Vehicle;

public class SelectVehicleDialog extends AppCompatDialogFragment {

    // class members
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
                        mListener.onSelectVehicle(vehicle);
                    }
                });
            }
        });

        // Return dialog
        return alertDialog;

    }

    // Fetch available vehicles for given race
    private void fetchAvailableVehicles() {
        // TODO: Fetch available vehicles for mRaceId and initialize spinner
    }


    // Create interface to communicate with parent
    public interface SelectVehicleListener {
        void onSelectVehicle(Vehicle vehicle);
    }
}
