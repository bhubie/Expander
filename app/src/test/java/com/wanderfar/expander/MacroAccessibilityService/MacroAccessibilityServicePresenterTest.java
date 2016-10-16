package com.wanderfar.expander.MacroAccessibilityService;




import com.wanderfar.expander.Models.Macro;
import com.wanderfar.expander.Services.MacroAccessibilityServicePresenterImpl;
import com.wanderfar.expander.Services.MacroAccessibilityServiceView;
import com.wanderfar.expander.TestHelpers.TestHelpers;

import org.junit.Before;
import org.junit.Test;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class MacroAccessibilityServicePresenterTest {

    private List<Macro> macroList = new ArrayList<>();
    private MacroAccessibilityServiceView macroAccessibilityServiceView;
    private MacroAccessibilityServicePresenterImpl macroAccessibilityServicePresenter;

    private final String TESTMACRO_TEXT_BEFORE_WITH_SPACE = "This string contains TestMacro ";
    private final String TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE = "This string contains Test Macro Phrase ";
    private final int TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE = TESTMACRO_TEXT_BEFORE_WITH_SPACE.length();
    private final int TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE = TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE.length();

    private final String TESTMACRO_TEXT_BEFORE_WITH_PERIOD = "This string contains TestMacro.";
    private final String TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD = "This string contains Test Macro Phrase.";
    private final int TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD = TESTMACRO_TEXT_BEFORE_WITH_PERIOD.length();
    private final int TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD = TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD.length();

    private final String TESTMACRO_TEXT_BEFORE_EXPAND_IMMEDIATELY = "This string contains testmacro";
    private final String TESTMACRO_TEXT_AFTER_EXPANSION_EXPAND_IMMEDIATELY = "This string contains Test Macro Phrase";
    private final int TESTMACRO_CURSOR_POSITION_BEFORE_EXPAND_IMMEDIATELY = TESTMACRO_TEXT_BEFORE_EXPAND_IMMEDIATELY.length();
    private final int TESTMACRO_CURSOR_POSITION_AFTER_EXPAND_IMMEDIATELY = TESTMACRO_TEXT_AFTER_EXPANSION_EXPAND_IMMEDIATELY.length();

    private final String TESTMACRO_CASE_SENSITIVE_TEXT_BEFORE_WITH_PERIOD_MACRO_LOWERCASE = "This string contains a testmacro.";
    private final String TESTMACRO_CASE_SENSITIVE_TEXT_AFTER_EXPANSION_WITH_PERIOD_MACRO_LOWERCASE = "This string contains a Test Macro Phrase.";
    private final int TESTMACRO_CASE_SENSITIVE_CURSOR_POSITION_BEFORE_WITH_PERIOD = TESTMACRO_CASE_SENSITIVE_TEXT_BEFORE_WITH_PERIOD_MACRO_LOWERCASE.length();
    private final int TESTMACRO_CASE_SENSITIVE_CURSOR_POSITION_AFTER_WITH_PERIOD = TESTMACRO_CASE_SENSITIVE_TEXT_AFTER_EXPANSION_WITH_PERIOD_MACRO_LOWERCASE.length();

    private final Locale US_LOCALE = new Locale("en", "US");
    private final String DAY_OF_WEEK_LONG = new SimpleDateFormat("EEEE", US_LOCALE).format(Calendar.getInstance().getTime());
    private final String DAY_OF_WEEK_SHORT = new SimpleDateFormat("EE", US_LOCALE).format(Calendar.getInstance().getTime());
    private final String DAY_OF_MONTH = new SimpleDateFormat("d", US_LOCALE).format(Calendar.getInstance().getTime());
    private final String MONTH_LONG = new SimpleDateFormat("MMMM", US_LOCALE).format(Calendar.getInstance().getTime());
    private final String MONTH_SHORT = new SimpleDateFormat("MMM", US_LOCALE).format(Calendar.getInstance().getTime());
    private final String YEAR = new SimpleDateFormat("yyyy", US_LOCALE).format(Calendar.getInstance().getTime());



    @Before
    public void setupData(){
        macroAccessibilityServiceView = mock(MacroAccessibilityServiceView.class);
        macroAccessibilityServicePresenter = new MacroAccessibilityServicePresenterImpl(macroAccessibilityServiceView);

    }

    @Test
    public void matchedMacroWithASpace(){
        //Tests that if we have macro within the text bounds and the user hits space, that we successfully update the passed in text with the macro phrase

        macroList.add(TestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, TestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);
    }


    @Test
    public void matchedMacroWithAPeriod(){
        //Tests that if we have a macro within the text bounds and the user types a period that we successfully update the passed in text with the phrase of the matched macro

        macroList.add(TestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, TestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }
    @Test
    public void invalidMacroMatch(){
        //Tests that if the text the user has typed does not contain a macro that we do not change the text to the macro phrase

        String text = "Here is some text that doesn't contain a Macro from the list";
        int cursorPosition = text.length();

        macroList.add(TestHelpers.createMacro("Test", "Test Phrase", null, false, TestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, text, cursorPosition);
        verify(macroAccessibilityServiceView, never()).updateText(text, cursorPosition);

    }

    @Test
    public void macroMatchNotWithinTextBounds(){
        //Tests that if a macro is present within the passed in text and it isn't in the text bounds range that we do not update the text even though there is a match

        macroList.add(TestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description",
                false, TestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);
        verify(macroAccessibilityServiceView, never()).updateText(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

    }

    @Test
    public void undoSetTextTest(){
        //Tests that if the undo set text method is called that undo and set the text back to be the previous matched macro name

        macroList.add(TestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, TestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

        macroAccessibilityServicePresenter.undoSetText();
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD - 1);

    }

    @Test
    public void caseSensitiveMacroInvalidMatchTest() {
        //Tests that if a macro is case sensitive and the user passes in the macro with the wrong case that we don't change the text

        String textBefore = "This string contains a testmacro.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();


        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true, TestHelpers.ON_A_SPACE_OR_PERIOD));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, textBefore, cursorPositionBefore);

        verify(macroAccessibilityServiceView, never()).updateText(textAfter, cursorPositionAfter);
    }

    @Test
    public void caseSensitiveMacroValidMatchTest() {
        //Tests that if a macro is case sensitive and the user passes in the macro with the right Case that we change the test

        String textBefore = "This string contains a TESTMACRO.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();


        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true, TestHelpers.ON_A_SPACE_OR_PERIOD));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, textBefore, cursorPositionBefore);

        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);
    }

    @Test
    public void nonCaseSensitiveMacroMatchWithUpperCasePassedInText(){
        //if i have a macro that is not case sensitive and lowercase and the user passes in the macro in uppercase
        //I should have a match and pass back the expanded phrase

        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, TestHelpers.ON_A_SPACE_OR_PERIOD));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TESTMACRO_CASE_SENSITIVE_TEXT_BEFORE_WITH_PERIOD_MACRO_LOWERCASE,
                TESTMACRO_CASE_SENSITIVE_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_CASE_SENSITIVE_TEXT_AFTER_EXPANSION_WITH_PERIOD_MACRO_LOWERCASE,
                TESTMACRO_CASE_SENSITIVE_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }

    @Test
    public void macroSetToExpandOnASpace(){
        //Tests that if the given macro is set to expand on a space that it does

        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, TestHelpers.ON_A_SPACE));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);

    }

    @Test
    public void macroSetToExpandImmediately(){
        //Tests that if the given macro is set to Expand Immedietely, that it does

        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, TestHelpers.IMMEDIATELY));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TESTMACRO_TEXT_BEFORE_EXPAND_IMMEDIATELY,
                TESTMACRO_CURSOR_POSITION_BEFORE_EXPAND_IMMEDIATELY);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_EXPAND_IMMEDIATELY,
                TESTMACRO_CURSOR_POSITION_AFTER_EXPAND_IMMEDIATELY);

    }

    @Test
    public void macroSetToExpandOnAPeriod(){
        //Tests that if the given macro is set to Expand on a, that it does

        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, TestHelpers.ON_A_PERIOD));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }

    @Test
    public void macroSetToExpandOnASpaceOrPeriodTestWithSpace(){
        //Tests that if the given macro is set to Expand on a period or space, and the user hits space that it expands

        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, TestHelpers.ON_A_SPACE_OR_PERIOD));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);

    }

    @Test
    public void macroSetToExpandOnASpaceOrPeriodTestWithPeriod(){
        //Tests that if the given macro is set to Expand on a period or space, and the user hits a period that it expands

        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, TestHelpers.ON_A_SPACE_OR_PERIOD));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

    }

    @Test
    public void macroWithNoExpansionSettingShouldExpanOnASpaceOrPeriodTestWithPeriod(){
        //Tests that if the given macro does not have an expand when setting that the macro expands on a space or period

        Macro macro = new Macro();
        macro.name = "testmacro";
        macro.phrase = "Test Macro Phrase";
        macro.setCaseSensitive(false);
        macro.setMacroPattern("(" + macro.name + ")" + "(\\s|\\.|\\.\\s)");

        macroList.add(macro);

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

    }

    @Test
    public void macroWithNoExpansionSettingShouldExpanOnASpaceOrPeriodTestWithSpace() {
        //Tests that if the given macro does not have an expand when setting that the macro expands on a space or period

        Macro macro = new Macro();
        macro.name = "testmacro";
        macro.phrase = "Test Macro Phrase";
        macro.setCaseSensitive(false);
        macro.setMacroPattern("(" + macro.name + ")" + "(\\s|\\.|\\.\\s)");

        macroList.add(macro);

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);
    }

    @Test
    public void basicDynamicMacroTest(){
        //Basic dynamic macro test

        macroList.add(TestHelpers.createMacro("TestDynamicPhone",
                "!phone", "Ending phrase should have phone make and model", false, TestHelpers.ON_A_SPACE_OR_PERIOD));


        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                "TestDynamicPhone.",
                "TestDynamicPhone.".length());



        verify(macroAccessibilityServiceView, times(1)).updateText(
                "Phone Make and Model.",
                "Phone Make and Model.".length());
    }

    @Test
    public void macroWithDynamicDayOfWeekInPhrase(){
        //Tests that if a macro expanded phrase contains the dynamic phrase for day of week that when the phrase is expanded that
        //it will expand and have the day of the week

        String MacroName = "DayOfWeekMacro";
        String MacroPhrase = "!d is the current day of the week";
        String TextBefore = MacroName + ".";

        macroList.add(TestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of week", false, TestHelpers.ON_A_SPACE_OR_PERIOD));


        String textAfterExpansionWithPeriod = DAY_OF_WEEK_LONG + " is the current day of the week.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicDayOfWeekShortInPhrase(){
        //Tests that if a macro expanded phrase contains the dynamic phrase for day of week short that when the phrase is expanded, that
        //it will expand and have the day of the week short
        macroList.clear();
        String MacroName = "DayOfWeekMacroShort";
        String MacroPhrase = "!ds is the current day of the week";
        String TextBefore = MacroName + ".";
        macroList.add(TestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of week", false, TestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = DAY_OF_WEEK_SHORT + " is the current day of the week.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicDayOfMonthInPhrase(){
        //Tests that if the macro expanded phrase contains the dynamic phrase for day of month
        //and the phrase is expanded, we return the day of the month
        macroList.clear();

        String MacroName = "DayOfMonth";
        String MacroPhrase = "!dm is the current day of the month";
        String TextBefore = MacroName + ".";
        macroList.add(TestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of the month", false, TestHelpers.ON_A_SPACE_OR_PERIOD));


        String textAfterExpansionWithPeriod = DAY_OF_MONTH + " is the current day of the month.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());

    }

    @Test
    public void macroWithDynamicMonthInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the Month name
        //and the phrase is expanded, we return the name of the month

        macroList.clear();

        String MacroName = "MonthName";
        String MacroPhrase = "The current Month is !m";
        String TextBefore = MacroName + ".";
        macroList.add(TestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, TestHelpers.ON_A_SPACE_OR_PERIOD));


        String textAfterExpansionWithPeriod = "The current Month is " + MONTH_LONG + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicMonthShortNameInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the Month short name
        //and the phrase is expanded, we return the name of the month short version

        macroList.clear();

        String MacroName = "MonthNameShort";
        String MacroPhrase = "The current Month short name is !ms";
        String TextBefore = MacroName + ".";
        macroList.add(TestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, TestHelpers.ON_A_SPACE_OR_PERIOD));


        String textAfterExpansionWithPeriod = "The current Month short name is " + MONTH_SHORT + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicYearInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the Year
        //and the phrase is expanded, we return the current year

        macroList.clear();

        String MacroName = "Year";
        String MacroPhrase = "The current Year is !y";
        String TextBefore = MacroName + ".";
        macroList.add(TestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, TestHelpers.ON_A_SPACE_OR_PERIOD));


        String textAfterExpansionWithPeriod = "The current Year is " + YEAR + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList,
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

}
