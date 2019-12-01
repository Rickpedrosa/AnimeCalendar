package com.example.animecalendar.base.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.animecalendar.R;
import com.example.animecalendar.data.local.LocalRepository;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class DirectSelectionDialogFragmentMaterial extends BottomSheetDialogFragment {

    private static final int SHEET_PEAK_HEIGHT = 900;
    private static final String ARG_ANIME_STATUS = "ARG_ANIME_STATUS";
    private String status;
    private Listener listener = null;

    public interface Listener {
        boolean onNavItemSelected(BottomSheetDialogFragment dialog, MenuItem item);
    }

    public static DirectSelectionDialogFragmentMaterial newInstance(String status, Fragment fragment, int requestCode) {
        DirectSelectionDialogFragmentMaterial dir = new DirectSelectionDialogFragmentMaterial();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_ANIME_STATUS, status);
        dir.setArguments(arguments);
        dir.setTargetFragment(fragment, requestCode);
        return dir;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_bottomsheet, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        status = requireArguments().getString(ARG_ANIME_STATUS);
        setupViews(requireView());
    }

    private void setupViews(View view) {
        setupBottomSheet(view);
    }

    private void setupBottomSheet(View view) {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) ((View) view.getParent())
                        .getLayoutParams();
        BottomSheetBehavior bottomSheetBehavior =
                (BottomSheetBehavior) params.getBehavior();
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN
                            || newState == BottomSheetBehavior.STATE_SETTLING) {
                        dismiss();
                    }

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }

            });
            // To assure sheet is completely shown.
            bottomSheetBehavior.setPeekHeight(SHEET_PEAK_HEIGHT);
        }
        setupNavigationView(view);
    }

    private void setupNavigationView(View view) {
        NavigationView navigationView = ViewCompat.requireViewById(view, R.id.navigationView);
        switch (status) {
            case LocalRepository.STATUS_COMPLETED:
                navigationView.inflateMenu(R.menu.dialog_navigation_completed);
                navigationView.setNavigationItemSelectedListener(menuItem ->
                        listener.onNavItemSelected(
                                DirectSelectionDialogFragmentMaterial.this,
                                menuItem));
                break;
            case LocalRepository.STATUS_FINISHED:
                navigationView.inflateMenu(R.menu.dialog_navigation_finished);
                navigationView.setNavigationItemSelectedListener(menuItem ->
                        listener.onNavItemSelected(
                                DirectSelectionDialogFragmentMaterial.this,
                                menuItem));
                break;
            case LocalRepository.STATUS_CURRENT:
                navigationView.inflateMenu(R.menu.dialog_navigation_current);
                navigationView.setNavigationItemSelectedListener(menuItem ->
                        listener.onNavItemSelected(
                                DirectSelectionDialogFragmentMaterial.this,
                                menuItem));
                break;
            case LocalRepository.STATUS_FOLLOWING:
                navigationView.inflateMenu(R.menu.dialog_navigation_following);
                navigationView.setNavigationItemSelectedListener(menuItem ->
                        listener.onNavItemSelected(
                                DirectSelectionDialogFragmentMaterial.this,
                                menuItem));
                break;
        }

    }

    @Override
    public void onAttach(@NotNull Context activity) {
        super.onAttach(activity);
        try {
            if (getTargetFragment() != null) {
                listener = (DirectSelectionDialogFragmentMaterial.Listener) getTargetFragment();
            } else {
                listener = (DirectSelectionDialogFragmentMaterial.Listener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Listener must implement DirectSelectionDialogFragment.Listener");
        }
    }
}
