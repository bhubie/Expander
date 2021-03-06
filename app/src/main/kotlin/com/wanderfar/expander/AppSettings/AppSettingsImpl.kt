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

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import com.wanderfar.expander.Models.MacroConstants


class AppSettingsImpl (var context: Context) : AppSettings{

    var prefs = PreferenceManager.getDefaultSharedPreferences(context)
    var accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

    override fun isDynamicValuesEnabled(): Boolean {
        return prefs.getBoolean("isDynamicValuesEnabled", false)
    }

    override fun isFloatingUIEnabled(): Boolean {
        return prefs.getBoolean("IsFloatingUIEnabled", true)
    }

    override fun isSystemAlertPermissionGranted(): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ){
            result = isFloatingUIEnabled()
        } else {
            result = Settings.canDrawOverlays(context)
        }
        return result
    }

    override fun getOpacityValue(): Int {
        return prefs.getInt("Opacity_Value", 75)
    }

    override fun getFloatingUIColor(): Int {
        return prefs.getInt("floatingUIColor", -24832)
    }

    override fun isAccessibilityServiceEnabled(): Boolean {
        val runningServices: List<AccessibilityServiceInfo>  = accessibilityManager
                .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK)

        return runningServices.any {
            "com.wanderfar.expander/.MacroAccessibilityService.MacroAccessibilityService" == it.id
        }
    }

    override fun setMacroListSortByPreference(sortBy: Int) {
        val editor = prefs.edit()
        editor.putInt("SortByMethod", sortBy)
        editor.apply()
    }

    override fun getMacroListSortByMethod(): Int {
        return prefs.getInt("SortByMethod", MacroConstants.SORT_BY_NAME)
    }

    override fun isRedoButtonEnabled(): Boolean {
        return prefs.getBoolean("ShowRedoButton", true)
    }

    override fun isUndoButtonEnabled(): Boolean {
        return prefs.getBoolean("ShowUndoButton", true)
    }

    override fun isApplicationFirstStart(): Boolean {
        return prefs.getBoolean("FirstStart", true)
    }

    override fun setApplicationFirstStart(firstStart: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean("FirstStart", firstStart)
        editor.apply()
    }
}