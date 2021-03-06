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


package com.wanderfar.expander.MacroAccessibilityService

import com.wanderfar.expander.Base.View




interface MacroAccessibilityServiceView : View {


    fun updateText(updatedText : String, newCursorPosition : Int)

    fun hideFloatingUI()

    fun showFloatingUI(opacityLevel: Int, color: Int, buttonType: String)

    fun startUpdateMacroStatisticsService(matchedMacro: String,increaseOrDecrease : String)
}

