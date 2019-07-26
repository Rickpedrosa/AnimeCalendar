package com.example.animecalendar.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.animecalendar.R;
import com.example.animecalendar.data.local.AppDatabase;
import com.example.animecalendar.model.NotificationItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
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
        //viewModel.getTodaysWatching().observe(this, this::triggerAlarm);
        viewModel.getPoggu().observe(this, new Observer<NotificationItem>() {
            @Override
            public void onChanged(NotificationItem notificationItem) {
                for (int i = 0; i < notificationItem.getAnimeTitles().size(); i++) {
                    Log.d("ITEM", notificationItem.getAnimeTitles().get(i));
                }
                Log.d("ITEM", String.valueOf(notificationItem.getNotificationTime()));
            }
        });
    }

    private void triggerAlarm(List<String> strings) {
        if (strings.size() > 0) {
            goAlarm(strings);
        }
    }

    private void setupProgressBarVisibility() {
        ProgressBar progressBar = ActivityCompat.requireViewById(this, R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        viewModel.getProgressBarController().observe(this, aBoolean -> progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE));
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

    private void goAlarm(List<String> strings) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra(TITLE_EXTRA, getResources().getString(R.string.app_name)); //title
        intent.putExtra(CONTENT_EXTRA, "Today animes!"); //content
        intent.putExtra(BIG_CONTENT_EXTRA, getAnimesBuilt(strings)); //big content
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        Objects.requireNonNull(alarmManager).setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 60000L, pendingIntent);
    }

    private String getAnimesBuilt(List<String> strings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            builder.append(strings.get(i).concat("\n"));
        }
        return builder.toString();
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

