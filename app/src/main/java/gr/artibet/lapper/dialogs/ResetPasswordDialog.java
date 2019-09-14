package gr.artibet.lapper.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import gr.artibet.lapper.R;

public class ResetPasswordDialog extends AppCompatDialogFragment {

    // class members
    private String mUsername;
    private TextView mTextViewUsername;
    private EditText mEditTextPassword;
    private ResetPasswordListener mListener;

    // Set listener
    public void setResetPasswordListener(ResetPasswordListener mListener) {
        this.mListener = mListener;
    }

    // Constructor takes username
    public ResetPasswordDialog(String username) {
        mUsername = username;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.reset_password_layout, null);

        // Initialize views
        mTextViewUsername = view.findViewById(R.id.textViewUsername);
        mEditTextPassword = view.findViewById(R.id.editTextPassword);

        // Build dialog
        builder.setView(view);
        mTextViewUsername.setText(mUsername);
        builder.setTitle(getString(R.string.reset_password_title));
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

                        // If password is empty do not dismiss dialog
                        String password = mEditTextPassword.getText().toString().trim();
                        if (password.isEmpty()) {
                            mEditTextPassword.setError(getString(R.string.password_required));
                            mEditTextPassword.requestFocus();
                            return;
                        }

                        // Dismiss dialog and call listener
                        dialog.dismiss();
                        mListener.onResetPassword(password);
                    }
                });
            }
        });

        // Return dialog
        return alertDialog;

    }


    // Create interface to communicate with parent
    public interface ResetPasswordListener {
        void onResetPassword(String password);
    }
}
