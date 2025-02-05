package com.example.animecalendar.base.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class YesNoDialogFragment extends DialogFragment {
    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";
    private static final String ARG_YES_TEXT = "ARG_YES_TEXT";
    private static final String ARG_NO_TEXT = "ARG_NO_TEXT";

    private Listener listener = null;
    private String title;
    private String message;
    private String yesText;
    private String noText;

    public interface Listener {
        void onPositiveButtonClick(DialogInterface dialog);

        void onNegativeButtonClick(DialogInterface dialog);
    }

    public static YesNoDialogFragment newInstance(String title, String message, String yesText,
                                                  String noText, Fragment target, int requestCode) {
        YesNoDialogFragment frg = new YesNoDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putString(ARG_MESSAGE, message);
        arguments.putString(ARG_YES_TEXT, yesText);
        arguments.putString(ARG_NO_TEXT, noText);
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
            message = arguments.getString(ARG_MESSAGE);
            yesText = arguments.getString(ARG_YES_TEXT);
            noText = arguments.getString(ARG_NO_TEXT);
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(requireActivity());
        b.setTitle(title);
        b.setMessage(message);
        b.setPositiveButton(yesText, (dialog, which) -> listener.onPositiveButtonClick(dialog));
        b.setNegativeButton(noText, (dialog, which) -> listener.onNegativeButtonClick(dialog));
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
            throw new ClassCastException("Listener must implement YesNoDialogFragment.Listener");
        }
    }
}