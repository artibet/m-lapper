package gr.artibet.lapper.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


import gr.artibet.lapper.R;

public class ConfirmDialog extends AppCompatDialogFragment {

    // class members
    private String mTitle;
    private String mMessage;
    private ConfirmListener mListener;


    // Create interface to communicate with parent
    public interface ConfirmListener {
        void onConfirm();
    }

    // Set listener
    public void setConfirmListener(ConfirmListener listener) {
        this.mListener = listener;
    }

    // Constructor takes title and message
    public ConfirmDialog(String title, String message) {
        mTitle = title;
        mMessage = message;
    }

    // Create dialog
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.confirm_layout, null);

        // Initialize views
        TextView tvMessage = view.findViewById(R.id.confirm_message);

        // Build dialog
        builder.setView(view);
        tvMessage.setText(mMessage);
        builder.setTitle(mTitle);
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.ok), null);

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button okButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Dismiss dialog and call listener
                        dialog.dismiss();
                        mListener.onConfirm();
                    }
                });
            }
        });

        return alertDialog;

    }


}



