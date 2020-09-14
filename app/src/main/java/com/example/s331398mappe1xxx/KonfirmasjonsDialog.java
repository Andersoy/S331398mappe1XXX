package com.example.s331398mappe1xxx;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class KonfirmasjonsDialog extends DialogFragment {

    private DialogClickListener callback;

    public interface DialogClickListener {
        public void onYesClick();
        public void onNoClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (DialogClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Manglende implimentasjon");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.er_du_sikker)
                .setPositiveButton(R.string.ja, (DialogInterface.OnClickListener) (dialog, whichButton) -> callback.onYesClick())
                .setNegativeButton(R.string.nei, (DialogInterface.OnClickListener) (dialog, whichButton) -> callback.onNoClick())
                .create();
    }
}
