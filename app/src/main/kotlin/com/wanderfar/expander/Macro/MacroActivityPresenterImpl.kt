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


import com.wanderfar.expander.Models.Macro
import com.wanderfar.expander.Models.MacroError
import com.wanderfar.expander.Models.MacroStore
import com.wanderfar.expander.Models.MacroValidator





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

    override fun saveMacro(name: String, phrase: String, description: String,
                           isCaseSensitive: Boolean, isNewMacro: Boolean){


        val mMacro = Macro()
        mMacro.name = name
        mMacro.phrase = phrase
        mMacro.description = description
        mMacro.isCaseSensitive = isCaseSensitive

        //TODO create method that will help determine the pattern based off of other settings
        mMacro.macroPattern = "(" + name + ")" + "(\\s|\\.|\\.\\s)"
        

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
                MacroStore.saveMacro(mMacro)
                view?.showSavedMacro()
            }
        }


    }

    fun loadMacro(macroToLoad: String){

        val macro = MacroStore.getMacro(macroToLoad)

            if(macro != null){
                view?.setData(macro)
            }

    }







}
