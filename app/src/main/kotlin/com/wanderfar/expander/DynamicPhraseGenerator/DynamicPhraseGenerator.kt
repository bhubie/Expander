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

package com.wanderfar.expander.DynamicPhraseGenerator

import java.text.SimpleDateFormat
import java.util.*


object DynamicPhraseGenerator {

    lateinit var mDynamicPhrasesFound : MutableList<DynamicPhraseFoundModel>

    val dynamicPhraseOptions = arrayOf(
            DynamicPhrase("Day of Week", "!d"),
            DynamicPhrase("Day of Week (Short)", "!ds"),
            DynamicPhrase("Day of Month", "!dm"),
            DynamicPhrase("Month", "!m"),
            DynamicPhrase("Month (Short Name)", "!ms"),
            DynamicPhrase("Year", "!y"),
            DynamicPhrase("Year (Short)", "!ys"),
            DynamicPhrase("Time (12 Hours)", "!t12h"),
            DynamicPhrase("Time (24 Hours)", "!t24h")
        )

    @JvmStatic fun getDynamicPhrases() : Array<DynamicPhrase> {
        return dynamicPhraseOptions
    }


    //Check based on passed in string, what dynamic phrases are in the string and return them
    fun checkTextForDynamicPhrases(textToCheck : String) : MutableList<DynamicPhraseFoundModel> {


        //For each phrase option set, see if we have a match for it in the passed in string
        //if we have a match add it to a list

        mDynamicPhrasesFound = mutableListOf()

        for ((name, phrase) in dynamicPhraseOptions){

            val regexMatcher = Regex("$phrase(\\W|$)")

            regexMatcher.findAll(textToCheck).forEach {
                val item = DynamicPhraseFoundModel(
                        phrase, it.range.start, it.range.endInclusive, phrase.length)
                mDynamicPhrasesFound.add(item)
            }

        }



        return mDynamicPhrasesFound
    }


    @JvmStatic fun setDynamicPhraseValue (phrase : String, locale : Locale) : String? {
        when (phrase) {
            "!d" -> return SimpleDateFormat("EEEE", locale).format(Calendar.getInstance().time)
            "!ds" -> return SimpleDateFormat("EE", locale).format(Calendar.getInstance().time)
            "!dm" -> return SimpleDateFormat("d", locale).format(Calendar.getInstance().time)
            "!m" -> return SimpleDateFormat("MMMM", locale).format(Calendar.getInstance().time)
            "!ms" -> return  SimpleDateFormat("MMM", locale).format(Calendar.getInstance().time)
            "!y" -> return  SimpleDateFormat("yyyy", locale).format(Calendar.getInstance().time)
            "!ys" -> return  SimpleDateFormat("yy", locale).format(Calendar.getInstance().time)
            "!t12h" -> return  SimpleDateFormat("hh:mm aaa", locale).format(Calendar.getInstance().time)
            "!t24h" -> return  SimpleDateFormat("HH:mm", locale).format(Calendar.getInstance().time)
            else -> {
                return null
            }
        }
    }
}