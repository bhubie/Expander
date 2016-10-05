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

package com.wanderfar.expander.DynamicPhrases

import java.text.SimpleDateFormat
import java.util.*


object DynamicPhraseGenerator {

    lateinit var mDynamicPhrasesFound : MutableList<DynamicPhraseFoundModel>

    val dynamicPhraseOptions = arrayOf(
            DynamicPhrase("PhoneMakeModel", "Returns the make and model of the phone", "!phone"),
            DynamicPhrase("DayOfWeek", "Returns the day of the week", "!d"),
            DynamicPhrase("DayOfWeekShort", "Returns the short name of the day of the week", "!ds")
        )


    //Check based on passed in string, what dynamic phrases are in the string and return them
    fun checkTextForDynamicPhrases(textToCheck : String) : MutableList<DynamicPhraseFoundModel> {


        //For each phrase option set, see if we have a match for it in the passed in string
        //if we have a match add it to a list

        mDynamicPhrasesFound = mutableListOf()

        for ((name, description, phrase) in dynamicPhraseOptions){

            val regexMatcher = Regex(phrase + "(\\W|$)")

            val results = regexMatcher.findAll(textToCheck).forEach {
                val item = DynamicPhraseFoundModel(
                        phrase, it.range.start, it.range.endInclusive, phrase.length)
                mDynamicPhrasesFound.add(item)
            }

        }



        return mDynamicPhrasesFound
    }


    @JvmStatic fun setDynamicPhraseValue (phrase : String, locale : Locale ) : String? {
        when (phrase) {
            "!phone" -> return "Phone Make and Model"
            "!d" -> return SimpleDateFormat("EEEE", locale).format(Calendar.getInstance().time)
            "!ds" -> return SimpleDateFormat("EE", locale).format(Calendar.getInstance().time)
            else -> {
                return null
            }
        }
    }
}