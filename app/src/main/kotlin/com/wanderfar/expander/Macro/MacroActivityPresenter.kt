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


package com.wanderfar.expander.Macro

import com.wanderfar.expander.Base.BaseActivityPresenter
import com.wanderfar.expander.Base.View


interface MacroActivityPresenter<T : View> : BaseActivityPresenter<T> {



    fun onCreate(macroToLoad: String)


    //fun onResume()

    //Destroy the view
    //fun onDestroy()

    //Saves the  macro based on input
    fun saveMacro(originalName: String,name: String, phrase: String, description: String, expandWhenSetting: Int,
                  isCaseSensitive: Boolean, isNewMacro : Boolean, expandWithinWords: Boolean)

    //Deletes the Macro
    fun deleteMacro(name: String)

    fun checkIfMacroIsChanged(originalName: String, newName: String, phrase: String, description: String, expandWhenSetting: Int,
                              isCaseSensitive: Boolean, isNewMacro : Boolean, expandWithinWords: Boolean)
}