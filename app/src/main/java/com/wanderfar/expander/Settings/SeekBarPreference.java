/*
 * Expander: Text Expansion Application
 * Copyright (C) 2016 Brett Huber
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.wanderfar.expander.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.DialogPreference;

import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.wanderfar.expander.R;


public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener, View.OnClickListener
{
    // ------------------------------------------------------------------------------------------
    // Private attributes :
    private static final String androidns="http://schemas.android.com/apk/res/android";

    private SeekBar mSeekBar;
    private FloatingActionButton mFloatingActionButton;
    private Context mContext;
    private int mColor;

    private final int FLOAT_CONVERSION = 100;

    private String mSuffix;
    private int mDefault, mMax, mValue = 0;


    // ------------------------------------------------------------------------------------------



    // ------------------------------------------------------------------------------------------
    // Constructor :
    public SeekBarPreference(Context context, AttributeSet attrs) {

        super(context,attrs);
        mContext = context;

        // Get string value for suffix (text attribute in xml file) :
        int mSuffixId = attrs.getAttributeResourceValue(androidns, "text", 0);
        if(mSuffixId == 0) mSuffix = attrs.getAttributeValue(androidns, "text");
        else mSuffix = mContext.getString(mSuffixId);

        // Get default and max seekbar values :
        mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
        mMax = attrs.getAttributeIntValue(androidns, "max", 100);
    }
    // ------------------------------------------------------------------------------------------



    // ------------------------------------------------------------------------------------------
    // DialogPreference methods :
    @Override
    protected View onCreateDialogView() {

        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6,6,6,6);



        mFloatingActionButton = new FloatingActionButton(mContext);
        mFloatingActionButton.setImageResource(R.drawable.ic_undo_24dp);
        float alpha = (float) mValue / FLOAT_CONVERSION;


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mColor = prefs.getInt("floatingUIColor", -24832);
        mFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(mColor));
        mFloatingActionButton.setAlpha(alpha);


        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(30, 30, 30, 30);


        layout.addView(mFloatingActionButton, params);


        mSeekBar = new SeekBar(mContext);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (shouldPersist())
            mValue = getPersistedInt(mDefault);

        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);

        return layout;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        mSeekBar.setMax(mMax);

        mSeekBar.setProgress(mValue);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue)
    {
        super.onSetInitialValue(restore, defaultValue);
        if (restore)
            mValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
        else
            mValue = (Integer)defaultValue;
    }
    // ------------------------------------------------------------------------------------------



    // ------------------------------------------------------------------------------------------
    // OnSeekBarChangeListener methods :
    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch)
    {

        float floatValue = (float) value / FLOAT_CONVERSION;
        mFloatingActionButton.setAlpha(floatValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seek) {}
    @Override
    public void onStopTrackingTouch(SeekBar seek) {}

    public void setMax(int max) { mMax = max; }
    public float getMax() { return mMax; }

    public void setProgress(int progress) {
        mValue = progress;
        if (mSeekBar != null)
            mSeekBar.setProgress(progress);
    }
    public float getProgress() { return mValue; }
    // ------------------------------------------------------------------------------------------



    // ------------------------------------------------------------------------------------------
    // Set the positive button listener and onClick action :
    @Override
    public void showDialog(Bundle state) {

        super.showDialog(state);

        Button positiveButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (shouldPersist()) {

            mValue = mSeekBar.getProgress();
            persistInt(mSeekBar.getProgress());
            callChangeListener(mSeekBar.getProgress());
        }

        (getDialog()).dismiss();
    }
    // ------------------------------------------------------------------------------------------
}