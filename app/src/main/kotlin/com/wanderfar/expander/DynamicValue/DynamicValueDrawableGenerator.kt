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
import com.wanderfar.expander.Application.Expander
import com.wanderfar.expander.DynamicPhraseGenerator.DynamicPhraseGenerator
import com.wanderfar.expander.R


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

    @JvmStatic fun getFriendlyName(phrase: String): String{

        val resources = Expander.context.resources
        when(phrase){
            "!d"            -> return resources.getString(R.string.dynamic_value_friendly_name_day_of_week)
            "!ds"           -> return resources.getString(R.string.dynamic_value_friendly_name_day_of_week_short)
            "!dm"           -> return resources.getString(R.string.dynamic_value_friendly_name_day_of_month)
            "!ms"           -> return resources.getString(R.string.dynamic_value_friendly_name_month_short)
            "!m"            -> return resources.getString(R.string.dynamic_value_friendly_name_month)
            "!y"            -> return resources.getString(R.string.dynamic_value_friendly_name_year)
            "!ys"           -> return resources.getString(R.string.dynamic_value_friendly_name_year_short)
            "!t12h"         -> return resources.getString(R.string.dynamic_value_friendly_name_time_12_hours)
            "!t24h"         -> return resources.getString(R.string.dynamic_value_friendly_name_time_24_hours)
            "!date"         -> return resources.getString(R.string.dynamic_value_friendly_name_date)
            "!clipboard"    -> return resources.getString(R.string.dynamic_value_friendly_name_clipboard)
            "!phonemm"      -> return resources.getString(R.string.dynamic_value_friendly_name_phone)
            else -> {
                return "Unknown"
            }
        }
    }
}