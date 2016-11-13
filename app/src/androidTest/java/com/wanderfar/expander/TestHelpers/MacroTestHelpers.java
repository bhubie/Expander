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


import android.content.Context;

import com.wanderfar.expander.DynamicPhrases.DynamicPhrase;
import com.wanderfar.expander.DynamicPhrases.DynamicPhraseGenerator;
import com.wanderfar.expander.Models.Macro;
import io.paperdb.Paper;

public class MacroTestHelpers {

    public static final int ON_A_SPACE_OR_PERIOD = 3;
    public static final int ON_A_SPACE = 1;
    public static final int ON_A_PERIOD = 2;
    public static final int IMMEDIATELY  =  0;

    public static Macro createMacro(String macroName, String macroPhrase,
                                    String macroDescription, Boolean isCaseSensative, int ExpandWhenSetting){
        Macro macro = new Macro();

        macro.setName(macroName);
        macro.setPhrase(macroPhrase);
        macro.setDescription(macroDescription);
        macro.setExpandWhenSetting(ExpandWhenSetting);
        macro.setCaseSensitive(isCaseSensative);

        macro.setMacroPattern(setMacroRegexPattern(ExpandWhenSetting, macroName));
        return macro;
    }

    private static String setMacroRegexPattern(int whenToExpand, String name){

        String newName = name.replace("(", "\\(").replace(")", "\\)");

        switch(whenToExpand){

            case ON_A_SPACE_OR_PERIOD : return "(" + newName + ")" + "(\\s|\\.|\\.\\s)";
            case ON_A_SPACE : return "(" + newName + ")" + "(\\s)";
            case ON_A_PERIOD : return "(" + newName + ")" + "(\\.)";
            case IMMEDIATELY : return newName;
            default: return newName;

        }

    }

    public static Macro buildGenericTestMacro(){

        return createMacro("TestMacro", "Test Macro Phrase", "Test Macro Description", false, ON_A_SPACE_OR_PERIOD);
    }

    public static void initDB(Context context){
        Paper.init(context);
        Paper.book("Macros").destroy();
    }


    public static void saveMacro(Macro macroToSave){

        Paper.book("Macros").write("macro_" + macroToSave.getName(), macroToSave);
    }

    public static Macro getMacro(String macroToLoad){

       return Paper.book("Macros").read(macroToLoad, new Macro());
    }

    public static void createAndSaveDynamicvAlueMacros(){
        DynamicPhrase[] dynamicValues = DynamicPhraseGenerator.getDynamicPhrases();

        for (DynamicPhrase phrase:
             dynamicValues) {

            Macro macroToSave = createMacro(
                   phrase.getName() //Macro Name
                   ,"The phrase is: " + phrase.getPhrase() //Macro phase
                   ,null //Macro Description
                   ,false //is Case sensitive
                   ,ON_A_SPACE_OR_PERIOD //Expand when setting
            );

            saveMacro(macroToSave);
        }
    }

}
