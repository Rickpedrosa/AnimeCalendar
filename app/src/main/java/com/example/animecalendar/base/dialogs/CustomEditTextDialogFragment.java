package com.example.animecalendar.base.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.animecalendar.R;

import org.jetbrains.annotations.NotNull;

public class CustomEditTextDialogFragment extends DialogFragment {

    private Listener listener;

    public interface Listener {
        void onOkayClick(DialogFragment dialog);
        void onCancelClick(DialogFragment dialog);
    }

    public static CustomEditTextDialogFragment newInstance(Fragment fragment, int requestCode){
        CustomEditTextDialogFragment customEditTextDialogFragment = new CustomEditTextDialogFragment();
        customEditTextDialogFragment.setTargetFragment(fragment, requestCode);
        return customEditTextDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @SuppressLint("InflateParams")
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(requireActivity());
        b.setTitle("");
        b.setView(LayoutInflater.from(requireActivity()).inflate(R.layout.custom_fragment_dialog, null));
        b.setPositiveButton("OKAY", (dialog, which) -> listener.onOkayClick(CustomEditTextDialogFragment.this));
        b.setNegativeButton("CANCEL", (dialog, which) -> listener.onCancelClick(CustomEditTextDialogFragment.this));
        return b.create();
    }

    @Override
    public void onAttach(@NotNull Context activity) {
        super.onAttach(activity);
        try {
            if (getTargetFragment() != null) {
                listener = (CustomEditTextDialogFragment.Listener) getTargetFragment();
            } else {
                listener = (CustomEditTextDialogFragment.Listener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Listener must implement YesNoDialogFragment.Listener");
        }
    }
}
