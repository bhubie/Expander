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


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.github.danielnilsson9.colorpickerview.preference.ColorPreference;
import com.wanderfar.expander.R;
import com.wanderfar.expander.SyncSettingsActivity.SyncSettingsActivity;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    private AppCompatDelegate mDelegate;
    private Preference floatingUIPref;
    private SeekBarPreference seekBarPref;
    private AppListMultiSelectListPreference appListPref;
    private SwitchPreference appListSwitch;
    private static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private CheckBoxPreference undoButtonCheckBoxPref;
    private CheckBoxPreference redoButtonCheckBoxPref;
    private ColorPreference floatingUIColor;
    private Preference syncSettingsPref;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            }
            else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }


            return true;
        }
    };



    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_general);

        floatingUIPref = findPreference(getResources().getString(R.string.settings_activity_floating_UI_key));
        seekBarPref = (SeekBarPreference) this.findPreference("Opacity_Value");
        seekBarPref.setSummary(R.string.setting_activity_floatingUI_opacity_level_summary_text);
        appListSwitch = (SwitchPreference) this.findPreference("Application_Filter_Type");
        appListPref = (AppListMultiSelectListPreference) this.findPreference(getResources().getString(R.string.key_application_list));
        undoButtonCheckBoxPref = (CheckBoxPreference) this.findPreference("ShowUndoButton");
        redoButtonCheckBoxPref = (CheckBoxPreference) this.findPreference("ShowRedoButton");
        floatingUIColor = (ColorPreference) this.findPreference("floatingUIColor");
        syncSettingsPref = this.findPreference("syncSettings");

        //Set the title and summary for the app list based on how the app list filter type
        setAppListTitleAndSummary();

        //if "Draw over other apps" permission is granted, set text to be disable permission
        setFloatingUITitleAndSummary();
        setFloatingUIClickListener();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        //set click listener for syncSettingsPref
        setSyncSettingsPrefClickListener();
    }

    private void setSyncSettingsPrefClickListener() {
        syncSettingsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                Intent intent = new Intent(preference.getContext(), SyncSettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFloatingUITitleAndSummary();
    }

    private void setFloatingUITitleAndSummary() {
        //if our build is less than marshmallow, we only check if the stored pref is set to true for drawing over other apps
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if (checkIfDrawOverOtherAppsStoredPrefIsTrue()){
                floatingUIPref.setTitle(getResources().getString(R.string.settings_activity_draw_over_other_apps_permission_enabled));
                floatingUIPref.setSummary(R.string.settings_activity_draw_over_other_apps_permission_enabled_summary);
                disableOrEnableFloatingUIChildPrefernces(true);

            } else
            {
                floatingUIPref.setTitle(getResources().getString(R.string.settings_activity_draw_over_other_apps_permission_disabled));
                floatingUIPref.setSummary(R.string.settings_activity_draw_over_other_apps_permission_disabled_summary);
                disableOrEnableFloatingUIChildPrefernces(true);
            }
        }
        //If our build is marshmallow or greater, check that the permission is enabled
        else {
            if(isSystemAlertPermissionGranted(this)){
                floatingUIPref.setTitle(getResources().getString(R.string.settings_activity_draw_over_other_apps_permission_enabled));
                floatingUIPref.setSummary(R.string.settings_activity_draw_over_other_apps_permission_enabled_summary);
                disableOrEnableFloatingUIChildPrefernces(true);
            }
            else {
                floatingUIPref.setTitle(getResources().getString(R.string.settings_activity_draw_over_other_apps_permission_disabled));
                floatingUIPref.setSummary(R.string.settings_activity_draw_over_other_apps_permission_disabled_summary);
                disableOrEnableFloatingUIChildPrefernces(false);
            }
        }

    }

    private boolean checkIfDrawOverOtherAppsStoredPrefIsTrue() {

        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("IsFloatingUIEnabled", true);
    }

    private void setFloatingUIClickListener() {
        floatingUIPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                requestSystemAlertPermission(SettingsActivity.this, null, OVERLAY_PERMISSION_REQ_CODE);
                return false;
            }
        });
    }


    private void requestSystemAlertPermission(Activity context, Fragment fragment, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            setFloatingUIEnabledorDisabled();
        }
        else {
            final String packageName = context == null ? fragment.getActivity().getPackageName() : context.getPackageName();
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName));
            if (fragment != null)
                fragment.startActivityForResult(intent, requestCode);
            else
                context.startActivityForResult(intent, requestCode);
        }

    }

    private void setFloatingUIEnabledorDisabled() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        if (prefs.getBoolean("IsFloatingUIEnabled", true)){
            //if its true set it to false
            editor.putBoolean("IsFloatingUIEnabled", false);
        } else {
            //set it to be true
            editor.putBoolean("IsFloatingUIEnabled", true);
        }

        editor.apply();
        setFloatingUITitleAndSummary();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            setFloatingUIEnabledorDisabled();
            setFloatingUITitleAndSummary();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private static boolean isSystemAlertPermissionGranted(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
    }


    private void setupActionBar() {
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);


        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("Application_Filter_Type")){
            setAppListTitleAndSummary();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    private void setAppListTitleAndSummary(){
        if (appListSwitch.isChecked()){
            //If true all apps are allowed by default
            //Set the title and summary to exclude apps
            appListPref.setTitle("Excluded Applications");
            appListPref.setSummary("Tap to manage which apps are excluded.");
        } else {
            //if it is off all apps are blocked by default set title and summary to be allow apps
            appListPref.setTitle("White Listed Applications");
            appListPref.setSummary("Tap to manage which apps are allowed.");
        }
    }

    private void disableOrEnableFloatingUIChildPrefernces(boolean setting){
        undoButtonCheckBoxPref.setEnabled(setting);
        redoButtonCheckBoxPref.setEnabled(setting);
        seekBarPref.setEnabled(setting);
        floatingUIColor.setEnabled(setting);
    }




}
