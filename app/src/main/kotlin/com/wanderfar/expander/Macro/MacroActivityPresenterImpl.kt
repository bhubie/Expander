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


import com.wanderfar.expander.Models.*


class MacroActivityPresenterImpl(override var view: MacroActivityView?) : MacroActivityPresenter<MacroActivityView> {



    //var mMacroActivityView  = view

    override fun onCreate() {

    }

    override fun onCreate(macroToLoad: String) {
        //we need to check if we have a macro to load

        println(macroToLoad)
        if (macroToLoad.isNullOrEmpty().not()){
            loadMacro(macroToLoad)
        }
    }

    override fun onResume() {

    }


    override fun deleteMacro(name: String) {


        MacroStore.deleteMacro(name)
        view?.showSavedMacro()
    }

    override fun saveMacro(originalName: String, name: String, phrase: String, description: String,
                           expandWhenSetting: Int, isCaseSensitive: Boolean,
                           isNewMacro: Boolean){


        val mMacro = Macro()
        mMacro.name = name
        mMacro.phrase = phrase
        mMacro.description = description
        mMacro.isCaseSensitive = isCaseSensitive
        mMacro.expandWhenSetting = expandWhenSetting
        mMacro.macroPattern = setMacroRegexPattern(expandWhenSetting, name)
        

        //Validate we have a name and phrase before saving
        when(MacroValidator.validateMacro(mMacro, isNewMacro)){
            //when we have an empty name, show empty name error
            MacroError.EMPTY_NAME -> view?.showMacroNoNameError()

            //when we have an empty phrase, show empty phrase error
            MacroError.EMPTY_PHRASE -> view?.showMacroNoPhraseError()

            //wen we have a duplicate name, return duplicate name error
            MacroError.DUPLICATE_NAME -> view?.showDuplicateMacroError()

            //When we have no error, save the macro
            MacroError.NO_ERROR -> {

                //If the macro is not a new one, and the original name doesn't equal the current name
                //Delete the original name
                if(isNewMacro.not() && (originalName == name).not()){
                    MacroStore.deleteMacro(originalName)
                }

                //Save the macro
                MacroStore.saveMacro(mMacro)

                view?.showSavedMacro()
            }
        }


    }

    override fun checkIfMacroIsChanged(originalName: String, newName: String, phrase: String, description: String, expandWhenSetting: Int,
                                       isCaseSensitive: Boolean, isNewMacro : Boolean) {

        val mMacro = Macro()

        mMacro.name = newName
        mMacro.phrase = phrase
        mMacro.description = description
        mMacro.isCaseSensitive = isCaseSensitive
        mMacro.expandWhenSetting = expandWhenSetting
        mMacro.macroPattern = setMacroRegexPattern(expandWhenSetting, newName)

        //If the macro has changed, as the user if they want to save or keep changed
        //If it hasn't changed call the back button like normal
        if (MacroStore.hasMacroChanged(mMacro, originalName)){
            view?.askIfUserWantsToSaveChanges()
        } else {
            view?.goBack()
        }

    }

    fun loadMacro(macroToLoad: String){

        val macro = MacroStore.getMacro(macroToLoad)

            if(macro != null){
                view?.setData(macro)
            }

    }

    fun setMacroRegexPattern(whenToExpand : Int, name : String) : String {
        when (whenToExpand) {
            MacroConstants.ON_A_SPACE_OR_PERIOD -> return "(" + name + ")" + "(\\s|\\.|\\.\\s)"
            MacroConstants.ON_A_SPACE -> return "(" + name + ")" + "(\\s)"
            MacroConstants.ON_A_PERIOD -> return "(" + name + ")" + "(\\.)"
            MacroConstants.IMMEDIATELY -> return name
            else -> {
                return name
            }
        }
    }

}
