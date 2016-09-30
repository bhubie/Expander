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

package com.wanderfar.expander.TestHelpers;

import com.wanderfar.expander.Models.Macro;



public class TestHelpers {

    public static final int ON_A_SPACE_OR_PERIOD = 3;
    public static final int ON_A_SPACE = 1;
    public static final int ON_A_PERIOD = 2;
    public static final int IMMEDIATELY  =  0;

    public static Macro createMacro(String macroName, String macroPhrase,
                                    String macroDescription, Boolean isCaseSensative, int ExpandWhenSetting){
        Macro macro = new Macro();

        macro.name = macroName;
        macro.phrase = macroPhrase;
        macro.description = macroDescription;
        macro.setExpandWhenSetting(ExpandWhenSetting);
        macro.setCaseSensitive(isCaseSensative);

        macro.macroPattern = setMacroRegexPattern(ExpandWhenSetting, macroName);
        return macro;
    }

    private static String setMacroRegexPattern(int whenToExpand, String name){

        switch(whenToExpand){

            case ON_A_SPACE_OR_PERIOD : return "(" + name + ")" + "(\\s|\\.|\\.\\s)";
            case ON_A_SPACE : return "(" + name + ")" + "(\\s)";
            case ON_A_PERIOD : return "(" + name + ")" + "(\\.)";
            case IMMEDIATELY : return name;
            default: return name;

        }


    }
}
