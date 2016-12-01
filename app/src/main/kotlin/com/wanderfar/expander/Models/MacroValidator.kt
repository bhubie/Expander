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



object MacroValidator {


    fun validateMacro(macro: Macro, isNewMacro: Boolean): MacroError {

        //Paper.init(context)
        //Paper.init(Expander.context)

        with(macro){
            if(name.isNullOrEmpty()) return MacroError.EMPTY_NAME
            if(phrase.isNullOrEmpty()) return MacroError.EMPTY_PHRASE
            if(Paper.book("Macros").exist(name) && isNewMacro) return MacroError.DUPLICATE_NAME
        }

        return MacroError.NO_ERROR
    }
}

enum class MacroError {
    EMPTY_NAME,
    EMPTY_PHRASE,
    DUPLICATE_NAME,
    NO_ERROR
}