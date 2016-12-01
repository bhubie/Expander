package com.wanderfar.expander.MacroAccesibilityService;






import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.wanderfar.expander.Application.Expander;
import com.wanderfar.expander.DynamicPhraseGenerator.DynamicPhraseGenerator;
import com.wanderfar.expander.DynamicValue.DynamicValueDrawableGenerator;
import com.wanderfar.expander.Models.Macro;
import com.wanderfar.expander.Services.MacroAccessibilityServicePresenterImpl;
import com.wanderfar.expander.Services.MacroAccessibilityServiceView;
import com.wanderfar.expander.TestHelpers.MacroTestHelpers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.saveMacro;
import static com.wanderfar.expander.TestHelpers.TestUtils.getPhoneMakeModel;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;



@RunWith(AndroidJUnit4.class)
@PrepareForTest({Expander.class})
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
    private final String YEAR_SHORT = new SimpleDateFormat("yy", US_LOCALE).format(Calendar.getInstance().getTime());
    private final String TIME_12_HOURS = new SimpleDateFormat("hh:mm aaa", US_LOCALE).format(Calendar.getInstance().getTime());
    private final String TIME_24_HOURS = new SimpleDateFormat("HH:mm", US_LOCALE).format(Calendar.getInstance().getTime());
    private final String DATE = DateFormat.getDateInstance(DateFormat.SHORT, US_LOCALE).format(Calendar.getInstance().getTime());

    @Before
    public void setupData(){
        macroAccessibilityServiceView = mock(MacroAccessibilityServiceView.class);
        macroAccessibilityServicePresenter = new MacroAccessibilityServicePresenterImpl(macroAccessibilityServiceView);

        initDB(InstrumentationRegistry.getTargetContext());
    }



    @Test
    public void matchedMacroWithASpace(){
        //Tests that if we have macro within the text bounds and the user hits space, that we successfully update the passed in text with the macro phrase

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE, true);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);
    }


    @Test
    public void matchedMacroWithAPeriod(){
        //Tests that if we have a macro within the text bounds and the user types a period that we successfully update the passed in text with the phrase of the matched macro

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD, true);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }
    @Test
    public void invalidMacroMatch(){
        //Tests that if the text the user has typed does not contain a macro that we do not change the text to the macro phrase

        String text = "Here is some text that doesn't contain a Macro from the list";
        int cursorPosition = text.length();

        saveMacro(MacroTestHelpers.createMacro("Test", "Test Phrase", null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(text, cursorPosition, true);
        verify(macroAccessibilityServiceView, never()).updateText(text, cursorPosition);

    }

    @Test
    public void macroMatchNotWithinTextBounds(){
        //Tests that if a macro is present within the passed in text and it isn't in the text bounds range that we do not update the text even though there is a match

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description",
                false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD, true);
        verify(macroAccessibilityServiceView, never()).updateText(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

    }

    @Test
    public void undoSetTextTest(){
        //Tests that if the undo set text method is called that undo and set the text back to be the previous matched macro name

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD, true);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

        macroAccessibilityServicePresenter.undoSetText();
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

    }

    @Test
    public void caseSensitiveMacroInvalidMatchTest() {
        //Tests that if a macro is case sensitive and the user passes in the macro with the wrong case that we don't change the text

        String textBefore = "This string contains a testmacro.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(textBefore, cursorPositionBefore, true);

        verify(macroAccessibilityServiceView, never()).updateText(textAfter, cursorPositionAfter);
    }

    @Test
    public void caseSensitiveMacroValidMatchTest() {
        //Tests that if a macro is case sensitive and the user passes in the macro with the right Case that we change the test

        String textBefore = "This string contains a TESTMACRO.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(textBefore, cursorPositionBefore, true);
        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);
    }

    @Test
    public void nonCaseSensitiveMacroMatchWithUpperCasePassedInText(){
        //if i have a macro that is not case sensitive and lowercase and the user passes in the macro in uppercase
        //I should have a match and pass back the expanded phrase

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_CASE_SENSITIVE_TEXT_BEFORE_WITH_PERIOD_MACRO_LOWERCASE,
                TESTMACRO_CASE_SENSITIVE_CURSOR_POSITION_BEFORE_WITH_PERIOD, true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_CASE_SENSITIVE_TEXT_AFTER_EXPANSION_WITH_PERIOD_MACRO_LOWERCASE,
                TESTMACRO_CASE_SENSITIVE_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }

    @Test
    public void macroSetToExpandOnASpace(){
        //Tests that if the given macro is set to expand on a space that it does

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE, true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);

    }

    @Test
    public void macroSetToExpandImmediately(){
        //Tests that if the given macro is set to Expand Immedietely, that it does

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.IMMEDIATELY));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_EXPAND_IMMEDIATELY,
                TESTMACRO_CURSOR_POSITION_BEFORE_EXPAND_IMMEDIATELY, true);


        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_EXPAND_IMMEDIATELY,
                TESTMACRO_CURSOR_POSITION_AFTER_EXPAND_IMMEDIATELY);

    }

    @Test
    public void macroSetToExpandOnAPeriod(){
        //Tests that if the given macro is set to Expand on a, that it does


        saveMacro(MacroTestHelpers.createMacro("TESTMACRO",
                "Test Macro Phrase",
                "TestMacro Description",
                false, MacroTestHelpers.ON_A_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD, true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }

    @Test
    public void macroSetToExpandOnASpaceOrPeriodTestWithSpace(){
        //Tests that if the given macro is set to Expand on a period or space, and the user hits space that it expands

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO",
                "Test Macro Phrase",
                "TestMacro Description",
                false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE, true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);

    }

    @Test
    public void macroSetToExpandOnASpaceOrPeriodTestWithPeriod(){
        //Tests that if the given macro is set to Expand on a period or space, and the user hits a period that it expands

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO",
                "Test Macro Phrase",
                "TestMacro Description",
                false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD, true);


        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

    }

    @Test
    public void macroWithNoExpansionSettingShouldExpanOnASpaceOrPeriodTestWithPeriod(){
        //Tests that if the given macro does not have an expand when setting that the macro expands on a space or period

        Macro macro = new Macro();
        macro.setName("testmacro");
        macro.setPhrase("Test Macro Phrase");
        macro.setCaseSensitive(false);
        macro.setMacroPattern("(" + macro.getName() + ")" + "(\\s|\\.|\\.\\s)");

        saveMacro(macro);

       macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD, true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

    }

    @Test
    public void macroWithNoExpansionSettingShouldExpanOnASpaceOrPeriodTestWithSpace() {
        //Tests that if the given macro does not have an expand when setting that the macro expands on a space or period

        Macro macro = new Macro();
        macro.setName("testmacro");
        macro.setPhrase("Test Macro Phrase");
        macro.setCaseSensitive(false);
        macro.setMacroPattern("(" + macro.getName() + ")" + "(\\s|\\.|\\.\\s)");

        saveMacro(macro);

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE, true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);
    }


    @Test
    public void macroWithDynamicDayOfWeekInPhrase(){
        //Tests that if a macro expanded phrase contains the dynamic phrase for day of week that when the phrase is expanded that
        //it will expand and have the day of the week

        String MacroName = "DayOfWeekMacro";
        String MacroPhrase = "!d is the current day of the week";
        String TextBefore = MacroName + ".";


        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of week", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = DAY_OF_WEEK_LONG + " is the current day of the week.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicDayOfWeekShortInPhrase(){
        //Tests that if a macro expanded phrase contains the dynamic phrase for day of week short that when the phrase is expanded, that
        //it will expand and have the day of the week short
        String MacroName = "DayOfWeekMacroShort";
        String MacroPhrase = "!ds is the current day of the week";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of week", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = DAY_OF_WEEK_SHORT + " is the current day of the week.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicDayOfMonthInPhrase(){
        //Tests that if the macro expanded phrase contains the dynamic phrase for day of month
        //and the phrase is expanded, we return the day of the month

        String MacroName = "DayOfMonth";
        String MacroPhrase = "!dm is the current day of the month";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of the month", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = DAY_OF_MONTH + " is the current day of the month.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());

    }

    @Test
    public void macroWithDynamicMonthInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the Month name
        //and the phrase is expanded, we return the name of the month


        String MacroName = "MonthName";
        String MacroPhrase = "The current Month is !m";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current Month is " + MONTH_LONG + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicMonthShortNameInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the Month short name
        //and the phrase is expanded, we return the name of the month short version


        String MacroName = "MonthNameShort";
        String MacroPhrase = "The current Month short name is !ms";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));


        String textAfterExpansionWithPeriod = "The current Month short name is " + MONTH_SHORT + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicYearInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the Year
        //and the phrase is expanded, we return the current year


        String MacroName = "Year";
        String MacroPhrase = "The current Year is !y";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current Year is " + YEAR + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicYearShortInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the Year short
        //and the phrase is expanded, we return the current year short version


        String MacroName = "YearShort";
        String MacroPhrase = "The current Year is !ys";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current Year is " + YEAR_SHORT + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicTime12HoursInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the time 12 hours
        //and the phrase is expanded, we return the current time in 12 hour format


        String MacroName = "Time12Hours";
        String MacroPhrase = "The current time in 12 hours is !t12h";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current time in 12 hours is " + TIME_12_HOURS + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicTime24HoursInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the time 12 hours
        //and the phrase is expanded, we return the current time in 12 hour format


        String MacroName = "Time24Hours";
        String MacroPhrase = "The current time in 24 hour format is !t24h";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current time in 24 hour format is " + TIME_24_HOURS + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicPhoneMakeModel(){
        //Tests that if the macro expanded contains the dynamic phrase for phone make/model
        //and the phrase is expanded, we return the make model of the phone.


        String makeModel = getPhoneMakeModel();

        String MacroName = "PhoneMakeModel";
        String MacroPhrase = "The phone make/model is: !phonemm";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The phone make/model is: " + makeModel + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());

    }

    @Test
    public void macroWithDynamicDateInPhrase() {
        //Tests that if the macro expanded phrase contains the dynamic phrase for the date
        //and the phrase is expanded, we return the date

        String MacroName = "Date";
        String MacroPhrase = "The current date is: !date";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current date is: " + DATE + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length(), true);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void macroWithDynamicValueUndoButtonTest(){
        //Tests that when there is a dynamic value in a macro phrase and the user hits undo
        //That the phrase is undone successfully
        //Test will make sure it works for every dynamic value

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {


                //DynamicPhrase[] dynamicValues = DynamicPhraseGenerator.getDynamicPhrases();
                String[] dynamicValues = DynamicPhraseGenerator.getDynamicPhrases();

                for (String phrase: dynamicValues) {

                    macroAccessibilityServiceView = mock(MacroAccessibilityServiceView.class);
                    macroAccessibilityServicePresenter = new MacroAccessibilityServicePresenterImpl(macroAccessibilityServiceView);

                    //String macroName = phrase.getName();
                    String macroName = DynamicValueDrawableGenerator.getFriendlyName(phrase);

                    //String macroPhrase = "The output is: " + phrase.getPhrase();
                    String macroPhrase = "The output is: " + phrase;


                    String textBefore = macroName + ".";

                    String textAfterExpansion = "The output is: "
                            //+ DynamicPhraseGenerator.setDynamicPhraseValue(InstrumentationRegistry.getTargetContext(), phrase.getPhrase(), US_LOCALE)
                            + DynamicPhraseGenerator.setDynamicPhraseValue(InstrumentationRegistry.getTargetContext(),
                            phrase, US_LOCALE)
                            + ".";

                    saveMacro(MacroTestHelpers.createMacro(macroName,
                            macroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));


                    macroAccessibilityServicePresenter.onAccessibilityEvent(
                            textBefore,
                            textBefore.length(), true);

                    verify(macroAccessibilityServiceView, times(1)).updateText(
                            textAfterExpansion,
                            textAfterExpansion.length());

                    macroAccessibilityServicePresenter.undoSetText();
                    verify(macroAccessibilityServiceView, times(1)).updateText(textBefore,
                            textBefore.length());
                }
            }
        });



    }
}
