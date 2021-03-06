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


package com.wanderfar.expander.Models


import io.paperdb.Paper



object MacroStore {



    fun saveMacro(macro: Macro){

        Paper.book("Macros").write(macro.name, macro)
        setMacroStoreUpdatedFlag(true)

    }

    fun hasStoreBeenUpdated(): Boolean{
        return Paper.book().read("macroStoreUpdated", false)
    }

    fun setMacroStoreUpdatedFlag(flag: Boolean) {
        Paper.book().write("macroStoreUpdated", flag)
    }

    fun getMacroKeys() : MutableList<String>{
        return Paper.book("Macros").allKeys
    }

    fun getMacros() : MutableList<Macro>{

        val macroList = mutableListOf<Macro>()

        val keys = Paper.book("Macros").allKeys


        keys.mapTo(macroList) { //val macro = macroDB.getObject(item, Macro::class.java)
            Paper.book("Macros").read<Macro>(it)
        }

        return macroList
    }

    fun deleteMacro(name : String){

        //Paper.init(Expander.context)
        Paper.book("Macros").delete(name)
        setMacroStoreUpdatedFlag(true)
    }

    fun getMacro(macroToLoad: String) : Macro? {
        val macro = Paper.book("Macros").read<Macro>(macroToLoad)
        return macro
    }

    fun hasMacroChanged(macroToCheck: Macro, originalName: String): Boolean{
        val loadedMacro = Paper.book("Macros").read<Macro>(originalName)

        println(loadedMacro.areObjectMemberEqual(macroToCheck))
        return loadedMacro.areObjectMemberEqual(macroToCheck).not()
    }

    fun Macro.areObjectMemberEqual(macroToCheck : Macro): Boolean{
        return this.name == macroToCheck.name && this.phrase == macroToCheck.phrase
                && this.description.equals(macroToCheck.description) && this.isCaseSensitive == macroToCheck.isCaseSensitive
                && this.expandWhenSetting == macroToCheck.expandWhenSetting && this.macroPattern == macroToCheck.macroPattern
                && this.expandWithinWords == macroToCheck.expandWithinWords
    }


}