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

package com.wanderfar.expander.DynamicValue


import android.content.Context
import android.text.Spannable
import com.wanderfar.expander.DynamicPhraseGenerator.DynamicPhraseGenerator
import com.wanderfar.expander.DynamicValue.DynamicValueDrawableSpan


object DynamicValueDrawableGenerator {

    fun addDynamicDrawables(context: Context, text: Spannable,
                            iconSize: Int, iconAlignment: Int, textSize: Int,
                            xPos: Float, yPos: Float){

        val textLength = text.length

        //see if we have a dynamic value in the passed in text
        val dynamicPhrases = DynamicPhraseGenerator.checkTextForDynamicPhrases(text.toString())

        if (dynamicPhrases.isNotEmpty()){

            // clear old spans

            val oldSpans = text.getSpans(0, textLength, DynamicValueDrawableSpan::class.java)

            for (i in oldSpans.indices) {
                text.removeSpan(oldSpans[i])

            }

            //Cycle through each dynamic phrase and set the span
            for ((phrase, startPosition, endPosition) in dynamicPhrases){

                text.setSpan(
                        DynamicValueDrawableSpan(context, 1, iconSize, iconAlignment, textSize, getFriendlyName(phrase),
                        xPos, yPos),
                        startPosition, endPosition + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            }

        }


    }

    fun getFriendlyName(phrase: String): String{

        when(phrase){
            "!d"    -> return "Day of Week"
            "!ds"   -> return "Day of Week (Short)"
            "!dm"   -> return "Day of Month"
            "!ms"   -> return "Month (Short Name)"
            "!m"    -> return "Month"
            "!y"    -> return "Year"
            "!ys"   -> return "Year (Short)"
            "!t12h" -> return "Time (12 Hours)"
            "!t24h" -> return "Time (24 Hours)"

            else -> {
                return "Unknown"
            }
        }
    }
}