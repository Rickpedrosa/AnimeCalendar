package com.example.animecalendar.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class BottomNavigationBehaviour
        extends CoordinatorLayout.Behavior<BottomNavigationView> {

    private static final String TAG = "CustomBehavior";

    private boolean isSnackbarShowing = false;
    private Snackbar.SnackbarLayout snackbar;

    public BottomNavigationBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   BottomNavigationView child, View dependency) {
        return dependency instanceof AppBarLayout ||
                dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       BottomNavigationView child,
                                       View directTargetChild, View target,
                                       int nestedScrollAxes, int type) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  BottomNavigationView child, View target,
                                  int dx, int dy, int[] consumed, int type) {
        if (isSnackbarShowing) {
            if (snackbar != null) {
                updateSnackbarPaddingByBottomNavigationView(child);
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent,
                                          BottomNavigationView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            AppBarLayout appbar = (AppBarLayout) dependency;
            float bottom = appbar.getBottom();
            float height = appbar.getHeight();
            float hidingRate = (height - bottom) / height;
            child.setTranslationY(child.getHeight() * hidingRate);
            return true;
        }
        if (dependency instanceof Snackbar.SnackbarLayout) {
            if (isSnackbarShowing) return true;
            isSnackbarShowing = true;
            snackbar = (Snackbar.SnackbarLayout) dependency;
            updateSnackbarPaddingByBottomNavigationView(child);
            return true;
        }
        return false;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent,
                                       BottomNavigationView child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            isSnackbarShowing = false;
            snackbar = null;
        }
        super.onDependentViewRemoved(parent, child, dependency);
    }

    private void updateSnackbarPaddingByBottomNavigationView(BottomNavigationView view) {
        if (snackbar != null) {
            int bottomTranslate = (int) (view.getHeight() - view.getTranslationY());
            snackbar.setPadding(snackbar.getPaddingLeft(), snackbar.getPaddingTop(),
                    snackbar.getPaddingRight(), bottomTranslate);
            snackbar.requestLayout();
        }
    }

}