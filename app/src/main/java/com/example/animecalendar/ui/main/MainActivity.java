package com.example.animecalendar.ui.main;

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
import com.example.animecalendar.utils.ValidationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
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
        viewModel.getNotificationEnablingPreference().observe(this, this::setAlarmNotification);
        viewModel.getNotificationLiveData().observe(this, new Observer<NotificationItem>() {
            @Override
            public void onChanged(NotificationItem notificationItem) {
                Log.d("POGONECHAMP", String.valueOf(notificationItem.isNotificationAccess()));
                Log.d("POGONECHAMP", String.valueOf(notificationItem.getNotificationTime()));
                Log.d("POGONECHAMP", String.valueOf(notificationItem.getAnimeTitles().size()));
                if (ValidationUtils.getAlarmNotificationWard(notificationItem)) {
                    Toast.makeText(MainActivity.this, "WE GOT IT PLS GOD", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAlarmNotification(Boolean aBoolean) {
        if (aBoolean) {
            try {
                AlertReceiver.setAlarm(MainActivity.this);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            AlertReceiver.cancelAlarm(MainActivity.this);
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

