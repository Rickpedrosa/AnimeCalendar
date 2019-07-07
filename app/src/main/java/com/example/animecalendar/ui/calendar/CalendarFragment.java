package com.example.animecalendar.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.example.animecalendar.R;

import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment implements OnSelectDateListener {

    private CalendarView calendarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        calendarView = ViewCompat.requireViewById(requireView(), R.id.calendarView);
        DatePickerBuilder builder = new DatePickerBuilder(requireContext(), this)
                .setPickerType(CalendarView.ONE_DAY_PICKER);

        DatePicker datePicker = builder.build();
        datePicker.show();
    }

    @Override
    public void onSelect(List<Calendar> calendar) {

    }
}
