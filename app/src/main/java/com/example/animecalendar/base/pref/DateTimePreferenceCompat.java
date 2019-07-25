package com.example.animecalendar.base.pref;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;
import android.util.AttributeSet;

import com.example.animecalendar.R;


public class DateTimePreferenceCompat extends DialogPreference {

    private int mTime;

    public DateTimePreferenceCompat(Context context) {
        this(context, null);
    }

    public DateTimePreferenceCompat(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public DateTimePreferenceCompat(Context context, AttributeSet attrs,
                                    int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public DateTimePreferenceCompat(Context context, AttributeSet attrs,
                                    int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // Do custom stuff here
        // ...
        // read attributes etc.
    }

    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        mTime = time;
        // Save to Shared Preferences
        persistInt(time);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // Default value from attribute. Fallback value is set to 0.
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
                                     Object defaultValue) {
        // Read the value. Use the default value if it is not possible.
        setTime(restorePersistedValue ?
                getPersistedInt(mTime) : (int) defaultValue);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.pref_dialog_time;
    }
}
