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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TestHelpers {

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

        switch(whenToExpand){

            case ON_A_SPACE_OR_PERIOD : return "(" + name + ")" + "(\\s|\\.|\\.\\s)";
            case ON_A_SPACE : return "(" + name + ")" + "(\\s)";
            case ON_A_PERIOD : return "(" + name + ")" + "(\\.)";
            case IMMEDIATELY : return name;
            default: return name;

        }


    }

    public static Date getAPastDayOfTheWeek(String dayToGet) {

        Calendar calendar = new GregorianCalendar();

        switch (dayToGet) {
            case "Monday" :  calendar.set(2015, Calendar.JUNE, 15);
                break;
            case "Tuesday" :  calendar.set(2015, Calendar.JUNE, 16);
                break;
            case "Wednesday" :  calendar.set(2015, Calendar.JUNE, 17);
                break;
            case "Thursday" :  calendar.set(2015, Calendar.JUNE, 18);
                break;
            case "Friday" :  calendar.set(2015, Calendar.JUNE, 19);
                break;
            case "Saturday" :  calendar.set(2015, Calendar.JUNE, 20);
                break;
            case "Sunday" :  calendar.set(2015, Calendar.JUNE, 21);
                break;
        }

        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,35);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTime();
    }
}
