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



package com.wanderfar.expander.Services

import android.preference.PreferenceManager
import com.wanderfar.expander.DynamicPhraseGenerator.DynamicPhraseGenerator
import com.wanderfar.expander.Models.Macro
import com.wanderfar.expander.Models.MacroConstants
import java.util.*


class MacroAccessibilityServicePresenterImpl (view : MacroAccessibilityServiceView) : MacroAccessibilityServicePresenter {

    var macroAccessibilityServiceView = view

    lateinit var text : String
    var matchedMacro : String = null.toString()
    var matchedMacroStartingPosition : Int = 0
    var matchedMacroEndingPosition : Int = 0
    var isInitialized : Boolean = false
    var charactersInsertedFromDynamicPhrases : Int = 0


    override fun onAccessibilityEvent(macrosToCheck : MutableList<Macro>, textToCheck : String, cursorPosition: Int,
                                      replaceDynamicPhrases: Boolean) {

        if (isInitialized){
            macroAccessibilityServiceView.hideFloatingUI()
        }
        print("Is repeat visit is: " + isInitialized)
        isInitialized == true

        //declare variable that will say if a match was found
        var aMatchWasFound : Boolean = false


        //declare variables that will store the start of the match and the new cursor position
        var newCursorPosition : Int = 0
        charactersInsertedFromDynamicPhrases = 0

        text = textToCheck

        //Hide the floating UI if it is already out there.
        //macroAccessibilityServiceView.hideFloatingUI()

        for (macro: Macro in macrosToCheck) {

            println(macro.name + " " + macro.macroPattern.toRegex().containsMatchIn(text.toString()))

            //get the length of the macro we are checking as we don't want to scan the whole text
            val macrolength = macro.name.length + 1

            //set the start index of the string to search
            val textSearchStart = setTextSearchStart(macrolength, cursorPosition)

            //Create the regex matcher
            val matcher = Regex(macro.macroPattern, setRegexOptions(macro.isCaseSensitive))


            //See if we have a match
            val result = matcher.find(text.toString().substring(0, getValidPosition(cursorPosition)), textSearchStart)


            //if we have a match, and the match wasn't undone, modify the text
            if (result != null && hasCurrentMatchBeenUnDone(macro.name,
                    result.range.start, matchedMacro, matchedMacroStartingPosition).not()){


                //If Macro is set to expand immediately, set the end replacing range to be end range + 1
                //This fixes bug where it wasn't replacing the whole shortcut when setting the phrase
                if (macro.expandWhenSetting == MacroConstants.IMMEDIATELY){
                    if (replaceDynamicPhrases){
                        text = text.replaceRange(result.range.start, result.range.endInclusive + 1, replaceDynamicPhrases(macro.phrase))
                    }else {
                        text = text.replaceRange(result.range.start, result.range.endInclusive + 1, macro.phrase)
                    }

                    newCursorPosition = charactersInsertedFromDynamicPhrases + setNewCursorPosition(result.range.start, macro.phrase.length)
                } else {
                    if (replaceDynamicPhrases){
                        text = text.replaceRange(result.range.start, result.range.endInclusive, replaceDynamicPhrases(macro.phrase))
                    } else {
                        text = text.replaceRange(result.range.start, result.range.endInclusive, macro.phrase)
                    }

                    newCursorPosition = charactersInsertedFromDynamicPhrases + setNewCursorPosition(result.range.start, macro.phrase.length) + 1
                }

                aMatchWasFound = true

                //Set the matched macro for undo purposes
                matchedMacro = macro.name
                matchedMacroStartingPosition = result.range.start
                matchedMacroEndingPosition = result.range.start + macro.phrase.length

            }
        }

        if (aMatchWasFound){
            macroAccessibilityServiceView.updateText(text, newCursorPosition)
        }
        else {
            //macroAccessibilityServiceView.hideFloatingUI()
        }
    }

    private fun getValidPosition(cursorPosition: Int): Int {
        if (cursorPosition < 0){
            return 0
        } else {
            return cursorPosition
        }
    }

    override fun undoSetText() {
        text = text.replaceRange(matchedMacroStartingPosition,
                matchedMacroEndingPosition + charactersInsertedFromDynamicPhrases,
                matchedMacro)

        macroAccessibilityServiceView.updateText(text, setNewCursorPosition(matchedMacroStartingPosition, matchedMacro.length + 1))
    }

    private fun  setTextSearchStart(macroLength: Int, cursorPosition: Int): Int {

        if ((cursorPosition - macroLength) < 0){
            return 0
        } else
        {
            return cursorPosition - macroLength
        }
    }

    private fun setRegexOptions(isCaseSensitive: Boolean) : Set<RegexOption> {
        val options = mutableSetOf<RegexOption>()


        if (isCaseSensitive.not()){
            options.add(RegexOption.IGNORE_CASE)
        }


        return options
    }

    private fun setNewCursorPosition(matchStart: Int, phraseLength : Int) : Int {
        val position = matchStart + phraseLength

        return position
    }

    private fun hasCurrentMatchBeenUnDone(currentMatch : String, currentMatchStart : Int, previousMatch : String, previousMatchStart: Int)
            : Boolean {
        //Check if the current match we are on matches the previous match and that the starting positions match as well
        //If they do, that means the user "un-did" the match so we shouldn't re-do it

        return currentMatch == previousMatch && currentMatchStart.equals(previousMatchStart)
    }

    private fun replaceDynamicPhrases(textToCheck : String) : String {

        //Get the list of dynamic values
        val dynamicPhrases = DynamicPhraseGenerator.checkTextForDynamicPhrases(textToCheck)

        //if the list is empty, return the original text
        if (dynamicPhrases.isEmpty()){
            return textToCheck

        }
        else {

            //if the list isn't empty, replace the text
            var updatedText = textToCheck

            for ((phrase) in dynamicPhrases){
                //Get the value of the phrase
                val phraseValue = DynamicPhraseGenerator.setDynamicPhraseValue(phrase, Locale.getDefault())

                //Replace it in the text and return it
                updatedText = updatedText.replace(phrase, phraseValue.toString(), false)

                charactersInsertedFromDynamicPhrases += (phraseValue.toString().length - phrase.length)

            }

            return updatedText
        }

    }


}