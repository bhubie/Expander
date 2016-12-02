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

package com.wanderfar.expander.MacroStatisticsService

import android.app.IntentService
import android.content.Intent
import com.wanderfar.expander.Models.MacroStore
import io.paperdb.Paper


class MacroStatisticsService : IntentService("UpdateMacroStatsService") {

    override fun onHandleIntent(intent: Intent?) {

        //Get the macro we need to update
        val macroToLoad = intent?.getStringExtra("MACRO")

        //Init the db
        Paper.init(this)

        //Get the macro
        val macro = MacroStore.getMacro(macroToLoad as String)

        //Update usage count
        var currentCount = macro?.usageCount

        macro?.usageCount =  1 + currentCount as Int

        macro?.description = "Test"

        println("Macro is:"  + macroToLoad)
        println("Usage count is: " + macro?.usageCount)
        println("Macro phrase " + macro?.phrase )
        println("Macro description " + macro?.description)
        //Save the macro
        if (macro != null) {
            MacroStore.saveMacro(macro)
        }
    }
}


