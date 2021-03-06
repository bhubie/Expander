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


package com.wanderfar.expander.MainActivity

import com.wanderfar.expander.Base.View
import com.wanderfar.expander.Models.Macro


interface MainActivityView : View {

    // shows the loading bar
    fun showProgress()

    //Hide progress bar
    fun hideProgress()

    //Set's the note to be displayed
    fun setData(macros: MutableList<Macro>, sortBy: Int?)

    //load's the note
    fun loadData(macros: MutableList<Macro>)

    //shows no macro message
    fun showNoMacroFoundMessage()

    fun sortMacroListAdapter(sortBy: Int)

    fun refreshMenu()

    fun showAccessibilityServiceNotEnabledMessage()

    fun launchApplicationIntroductionActivity()
}