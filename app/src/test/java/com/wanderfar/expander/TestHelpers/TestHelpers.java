package com.wanderfar.expander.TestHelpers;

import com.wanderfar.expander.Models.Macro;

/**
 * Created by bretthuber on 9/12/16.
 */

public class TestHelpers {

    public static Macro createMacro(String macroName, String macroPhrase,
                                    String macroDescription, Boolean isCaseSensative){
        Macro macro = new Macro();

        macro.name = macroName;
        macro.phrase = macroPhrase;
        macro.description = macroDescription;
        macro.macroPattern = "(" + macro.getName() + ")" + "(\\s|\\.|\\.\\s)";
        macro.setCaseSensitive(isCaseSensative);
        return macro;
    }
}
