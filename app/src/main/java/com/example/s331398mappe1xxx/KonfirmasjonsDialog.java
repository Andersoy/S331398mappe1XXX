package com.example.s331398mappe1xxx;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class KonfirmasjonsDialog extends DialogFragment {

    private DialogClickListener callback;
    String origin;
    String tittel;

    public KonfirmasjonsDialog() {

    }
    public KonfirmasjonsDialog(String origin) {
        this.origin = origin;
    }

    public interface DialogClickListener {
        void onYesClick();
        void onNoClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        try {
            callback = (DialogClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Manglende implimentasjon");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(origin.equals("statistikk")){
            tittel = getResources().getString(R.string.er_du_sikker);
        }else{
            tittel = getResources().getString(R.string.er_du_sikker2);
        }
        return new AlertDialog.Builder(getActivity()).setTitle(tittel)
                .setPositiveButton(R.string.ja, (DialogInterface.OnClickListener) (dialog, whichButton) -> callback.onYesClick())
                .setNegativeButton(R.string.nei, (DialogInterface.OnClickListener) (dialog, whichButton) -> callback.onNoClick())
                .create();
    }

    @Override
    /**for å unngå crash ved rotasjon*/
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
