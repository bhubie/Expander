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

package com.wanderfar.expander.QuickTileServices

import android.annotation.TargetApi
import android.os.Build
import android.preference.PreferenceManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log


@TargetApi(Build.VERSION_CODES.N)
class OnOffQuickTileService : TileService() {

    /**
     * Called when the tile is added to the Quick Settings.
     * @return TileService constant indicating tile state
     */

    override fun onTileAdded() {
        Log.d("QS", "Tile added")
        println("Tile was added")

    }

    /**
     * Called when this tile begins listening for events.
     */
    override fun onStartListening() {
        Log.d("QS", "Start listening")
        //When it starts listening, monitor permission if it is enable
        val tile = this.qsTile
        val isExpanderEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isExpanderEnabled", true)
        if (isExpanderEnabled){
            tile.state = Tile.STATE_ACTIVE
            tile.label = "Disable Expander"
        } else {
            tile.state = Tile.STATE_INACTIVE
            tile.label = "Enable Expander"
        }
        tile.updateTile()
    }

    override fun onClick() {
        Log.d("QS", "Tile tapped")

        updateTile()
    }

    private fun updateTile(){
        val tile = this.qsTile

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val prefEditor = prefs.edit()

        //Get the current state of the tile
        val state = tile.state

        //Based on the state update shared preferences preferences and the tile
        if (state == Tile.STATE_ACTIVE){
            tile.state = Tile.STATE_INACTIVE
            tile.label = "Enable Expander"
            prefEditor.putBoolean("isExpanderEnabled", false)
        } else {
            tile.state = Tile.STATE_ACTIVE
            tile.label = "Disable Expander"
            prefEditor.putBoolean("isExpanderEnabled", false)
        }

        prefEditor.apply()
        tile.updateTile()
    }


}