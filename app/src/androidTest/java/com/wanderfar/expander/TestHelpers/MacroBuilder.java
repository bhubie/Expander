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

import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.setMacroRegexPattern;

public class MacroBuilder {

    private String macroName;
    private String macroPhrase;
    private String macroDescription;
    private boolean isCaseSensative = false;
    private boolean expandWithinWords = false;
    private int expandWhenSetting = 0;

    public MacroBuilder(){

    }

    public MacroBuilder setMacroName(String macroName){
        this.macroName = macroName;
        return this;
    }

    public MacroBuilder setMacroPhrase(String macroPhrase){
        this.macroPhrase = macroPhrase;
        return this;
    }

    public MacroBuilder setMacroDescription(String macroDescription){
        this.macroDescription = macroDescription;
        return this;
    }

    public MacroBuilder setIsCaseSensative(boolean isCaseSensative){
        this.isCaseSensative = isCaseSensative;
        return this;
    }

    public MacroBuilder setExpandWithinWords(boolean expandWithinWords){
        this.expandWithinWords = expandWithinWords;
        return this;
    }

    public MacroBuilder setExpandWhenSetting(int expandWhenSetting){
        this.expandWhenSetting = expandWhenSetting;
        return this;
    }

    public Macro build(){
        Macro macro = new Macro();
        macro.setName(macroName);
        macro.setPhrase(macroPhrase);
        macro.setDescription(macroDescription);
        macro.setCaseSensitive(isCaseSensative);
        macro.setExpandWhenSetting(expandWhenSetting);
        macro.setExpandWithinWords(expandWithinWords);
        macro.setMacroPattern(setMacroRegexPattern(expandWhenSetting, macroName));
        return macro;
    }

}
