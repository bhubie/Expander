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
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;


public class AppListMultiSelectListPreference extends MultiSelectListPreference {

    private List<String> entries = new ArrayList<>();
    private List<String> entriesValues = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppListMultiSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setItems(context);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppListMultiSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setItems(context);
    }

    public AppListMultiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setItems(context);
    }

    public AppListMultiSelectListPreference(Context context) {
        super(context);

        setItems(context);

    }

    private void setItems(Context context){

        final PackageManager pm = context.getPackageManager();
        //get a list of installed apps.

        Runnable getInstalledApps = new Runnable()
        {
            @Override
            public void run()
            {

                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                for (ApplicationInfo app: packages
                        ) {
                    entries.add(app.loadLabel(pm).toString());
                    entriesValues.add(app.packageName);

                    setEntries(entries.toArray(new CharSequence[]{}));
                    setEntryValues(entriesValues.toArray(new CharSequence[]{}));
                }

                    /*this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setEntries(entries.toArray(new CharSequence[]{}));
                            setEntryValues(entriesValues.toArray(new CharSequence[]{}));
                        }
                    });*/
            }

        };

        Thread loadThread = new Thread(getInstalledApps);
        loadThread.start();



    }


}