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

package com.wanderfar.expander.AppSettings

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings


class AppSettingsImpl (context: Context) : AppSettings{

    var context = context
    var prefs = PreferenceManager.getDefaultSharedPreferences(context)

    override fun isDynamicValuesEnabled(): Boolean {
        return prefs.getBoolean("isDynamicValuesEnabled", false)
    }

    override fun isFloatingUIEnabled(): Boolean {
        return prefs.getBoolean("IsFloatingUIEnabled", true)
    }

    override fun isSystemAlertPermissionGranted(): Boolean {
        val result = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context)
        return result
    }

    override fun getOpacityValue(): Int {
        return prefs.getInt("Opacity_Value", 75)
    }

    override fun getFloatingUIColor(): Int {
        return prefs.getInt("floatingUIColor", -24832)
    }

}