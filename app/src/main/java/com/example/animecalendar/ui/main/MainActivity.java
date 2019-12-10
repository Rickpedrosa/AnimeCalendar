package com.example.animecalendar.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.animecalendar.R;
import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.utils.ValidationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    public static final String TITLE_EXTRA = "TITLE_EXTRA";
    public static final String CONTENT_EXTRA = "CONTENT_EXTRA";
    public static final String BIG_CONTENT_EXTRA = "BIG_CONTENT_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();
        setupBottomNavigationView();
        setupProgressBarVisibility();
        viewModel.getNotificationLiveData().observe(this, notificationItem -> {
            if (ValidationUtils.getAlarmNotificationWard(notificationItem)) {
                try {
                    AlertReceiver.setAlarm(MainActivity.this,
                            animesForNotification(notificationItem.getAnimeTitles()),
                            notificationItem.getNotificationTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (!notificationItem.isNotificationAccess()) {
                AlertReceiver.cancelAlarm(MainActivity.this);
            }
        });
    }

    private String animesForNotification(List<String> animes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < animes.size(); i++) {
            if (i == animes.size() - 1) {
                builder.append(animes.get(i));
            } else {
                builder.append(animes.get(i).concat("\n"));
            }
        }
        return builder.toString();
    }

    private void setupProgressBarVisibility() {
        ProgressBar progressBar = ActivityCompat.requireViewById(this, R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        viewModel.getProgressBarController().observe(this, aBoolean ->
                progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE));
    }

    private void setupBottomNavigationView() {
        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);

        BottomNavigationView bnv = ActivityCompat.requireViewById(this, R.id.bottom_navigation);

        bnv.setOnNavigationItemReselectedListener(menuItem -> {
            //listener vacío para evitar que el jetpack de navigation recree el fragmento
            //al darle a un tab activo
        });
        NavigationUI.setupWithNavController(bnv,
                Objects.requireNonNull(navHost).getNavController());
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this, new MainActivityViewModelFactory(
                getApplication(), AppDatabase.getInstance(this)
        )).get(MainActivityViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(getSupportFragmentManager()
                            .getBackStackEntryAt(0).getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }
}

