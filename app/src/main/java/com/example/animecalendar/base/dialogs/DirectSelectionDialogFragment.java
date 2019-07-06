package com.example.animecalendar.base.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class DirectSelectionDialogFragment extends DialogFragment {
    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_ITEMS = "ARG_ITEMS";

    @SuppressWarnings("UnusedParameters")
    public interface Listener {
        void onItemSelected(DialogFragment dialog, int which);
    }

    private Listener listener = null;
    private String title;
    private CharSequence[] items;

    public static DirectSelectionDialogFragment newInstance(String title, CharSequence[] items,
                                                            Fragment target, int requestCode) {
        DirectSelectionDialogFragment frg = new DirectSelectionDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putCharSequenceArray(ARG_ITEMS, items);
        frg.setArguments(arguments);
        frg.setTargetFragment(target, requestCode);
        return frg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtainArguments();
        setCancelable(false);
    }


    private void obtainArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(ARG_TITLE);
            items = arguments.getCharSequenceArray(ARG_ITEMS);
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(requireActivity());
        b.setTitle(title);
        b.setItems(items,
                (dialog, which) -> listener.onItemSelected(DirectSelectionDialogFragment.this,
                        which));
        return b.create();
    }

    @Override
    public void onAttach(@NotNull Context activity) {
        super.onAttach(activity);
        try {
            if (getTargetFragment() != null) {
                listener = (Listener) getTargetFragment();
            } else {
                listener = (Listener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Listener must implement DirectSelectionDialogFragment.Listener");
        }
    }

}
