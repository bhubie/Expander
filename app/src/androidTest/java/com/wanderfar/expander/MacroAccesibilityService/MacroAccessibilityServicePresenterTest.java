package com.wanderfar.expander.MacroAccesibilityService;






import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wanderfar.expander.AppSettings.AppSettings;
import com.wanderfar.expander.Application.Expander;
import com.wanderfar.expander.DynamicPhraseGenerator.DynamicPhraseGenerator;
import com.wanderfar.expander.DynamicValue.DynamicValueDrawableGenerator;
import com.wanderfar.expander.Models.Macro;
import com.wanderfar.expander.MacroAccessibilityService.MacroAccessibilityServicePresenterImpl;
import com.wanderfar.expander.MacroAccessibilityService.MacroAccessibilityServiceView;
import com.wanderfar.expander.TestHelpers.MacroTestHelpers;

import org.junit.Assert;
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
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.getMacro;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.getMacroStoreUpdatedFlag;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.saveMacro;
import static com.wanderfar.expander.TestHelpers.TestUtils.getPhoneMakeModel;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
@PrepareForTest({Expander.class})
public class MacroAccessibilityServicePresenterTest {

    private List<Macro> macroList = new ArrayList<>();
    private MacroAccessibilityServiceView macroAccessibilityServiceView;
    private MacroAccessibilityServicePresenterImpl macroAccessibilityServicePresenter;
    private AppSettings appSettings;

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
        appSettings = mock(AppSettings.class);
        when(appSettings.isDynamicValuesEnabled()).thenReturn(true);
        macroAccessibilityServicePresenter = new MacroAccessibilityServicePresenterImpl(macroAccessibilityServiceView, appSettings);

        initDB(InstrumentationRegistry.getTargetContext());
    }



    @Test
    public void shouldReturnExpandedMacroWhenUserTypesMacroFollowedByASpace(){

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);
    }


    @Test
    public void shouldReturnExpandedMacroWhenUserTypesMacroFollowedByAPeriod(){

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }
    @Test
    public void shouldNotReturnAnExpandedMacroWhenMacroIsNotMatched(){

        String text = "Here is some text that doesn't contain a Macro from the list";
        int cursorPosition = text.length();

        saveMacro(MacroTestHelpers.createMacro("Test", "Test Phrase", null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(text, cursorPosition);
        verify(macroAccessibilityServiceView, never()).updateText(text, cursorPosition);

    }

    @Test
    public void shouldNotReturnAnExpandedMacroWhenMatchedMacroIsNotWithinPassedTextBounds(){

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description",
                false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);
        verify(macroAccessibilityServiceView, never()).updateText(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

    }

    @Test
    public void shouldUndoExpandedTextBackToOriginalWhenMacroIsExpandedAndUserHitsUndoButton(){

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

        macroAccessibilityServicePresenter.undoSetText();
        verify(macroAccessibilityServiceView, times(1)).updateText(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

    }

    @Test
    public void shouldNotUpdateTextWhenMatchedMacroIsCaseSensitiveAndMacroIsPassedInWithWrongCase() {

        String textBefore = "This string contains a testmacro.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(textBefore, cursorPositionBefore);

        verify(macroAccessibilityServiceView, never()).updateText(textAfter, cursorPositionAfter);
    }

    @Test
    public void shouldCallUpdateTextWhenMatchedMacroIsCaseSensitiveAndPassedInTextContainedCorrectCase() {

        String textBefore = "This string contains a TESTMACRO.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(textBefore, cursorPositionBefore);
        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);
    }

    @Test
    public void shouldUpdateTextWhenMatchedMacroIsNotCaseSensitiveAndPassedInTextContainsMacroPassedInUppercase(){

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_CASE_SENSITIVE_TEXT_BEFORE_WITH_PERIOD_MACRO_LOWERCASE,
                TESTMACRO_CASE_SENSITIVE_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_CASE_SENSITIVE_TEXT_AFTER_EXPANSION_WITH_PERIOD_MACRO_LOWERCASE,
                TESTMACRO_CASE_SENSITIVE_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }

    @Test
    public void shouldCallUpdateTextWhenPassedInTextContainsAMacroFollowedByASpace(){

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);

    }

    @Test
    public void shouldCallUpdateTextWhenPassedInTextContainsAMatchedMacroThatIsSetToExpandImmedietelyWhenMatched(){

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.IMMEDIATELY));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_EXPAND_IMMEDIATELY,
                TESTMACRO_CURSOR_POSITION_BEFORE_EXPAND_IMMEDIATELY);


        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_EXPAND_IMMEDIATELY,
                TESTMACRO_CURSOR_POSITION_AFTER_EXPAND_IMMEDIATELY);

    }

    @Test
    public void shouldCallUpdateTextWhenPassedInTextContainsMacroFollowedByAPeriodAndMatchedMacroIsSetToExpandOnPeriod(){

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO",
                "Test Macro Phrase",
                "TestMacro Description",
                false, MacroTestHelpers.ON_A_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);
    }

    @Test
    public void shouldCallUpdateTextWhenPassedInTextContainsMacroFollowedByASpaceAndMatchedMacroIsSetToExpandOnPeriodorSpace(){

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO",
                "Test Macro Phrase",
                "TestMacro Description",
                false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);

    }

    @Test
    public void shouldCallUpdateTextWhenPassedInTextContainsMacroFollowedByAPeriodAndMatchedMacroIsSetToExpandOnPeriodorSpace(){

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO",
                "Test Macro Phrase",
                "TestMacro Description",
                false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);


        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

    }

    @Test
    public void shouldCallUpdateTextWhenPassedInTextContainsMacroFollowedByAPeriodAndMatchedMacroDoesNotHaveAExpandWhenSetting(){

        Macro macro = new Macro();
        macro.setName("testmacro");
        macro.setPhrase("Test Macro Phrase");
        macro.setCaseSensitive(false);
        macro.setMacroPattern("(" + macro.getName() + ")" + "(\\s|\\.|\\.\\s)");

        saveMacro(macro);

       macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_PERIOD);

    }

    @Test
    public void shouldCallUpdateTextWhenPassedInTextContainsMacroFollowedByASpaceAndMatchedMacroDoesNotHaveAExpandWhenSetting() {

        Macro macro = new Macro();
        macro.setName("testmacro");
        macro.setPhrase("Test Macro Phrase");
        macro.setCaseSensitive(false);
        macro.setMacroPattern("(" + macro.getName() + ")" + "(\\s|\\.|\\.\\s)");

        saveMacro(macro);

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TESTMACRO_TEXT_BEFORE_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_SPACE);

        verify(macroAccessibilityServiceView, times(1)).updateText(
                TESTMACRO_TEXT_AFTER_EXPANSION_WITH_SPACE,
                TESTMACRO_CURSOR_POSITION_AFTER_WITH_SPACE);
    }


    @Test
    public void shouldCallUpdateTextThatContainsTheDayOfTheWeekWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForDayOFWeek(){

        String MacroName = "DayOfWeekMacro";
        String MacroPhrase = "!d is the current day of the week";
        String TextBefore = MacroName + ".";


        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of week", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = DAY_OF_WEEK_LONG + " is the current day of the week.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldCallUpdateTextThatContainsTheDayOfTheWeekShortWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForDayOfWeekShort(){

        String MacroName = "DayOfWeekMacroShort";
        String MacroPhrase = "!ds is the current day of the week";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of week", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = DAY_OF_WEEK_SHORT + " is the current day of the week.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldCallUpdateTextThatContainsTheDayOfTheMonthWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForDayOfMonth(){

        String MacroName = "DayOfMonth";
        String MacroPhrase = "!dm is the current day of the month";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, "Ending phrase should have the current day of the month", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = DAY_OF_MONTH + " is the current day of the month.";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());

    }

    @Test
    public void shouldCallUpdateTextThatContainsTheCurrentMonthWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForMonthName() {

        String MacroName = "MonthName";
        String MacroPhrase = "The current Month is !m";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current Month is " + MONTH_LONG + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldCallUpdateTextThatContainsTheCurrentMonthShortNameWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForMonthShortName() {

        String MacroName = "MonthNameShort";
        String MacroPhrase = "The current Month short name is !ms";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));


        String textAfterExpansionWithPeriod = "The current Month short name is " + MONTH_SHORT + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldCallUpdateTextThatContainsTheCurrentYearWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForYear() {

        String MacroName = "Year";
        String MacroPhrase = "The current Year is !y";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current Year is " + YEAR + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldCallUpdateTextThatContainsTheCurrentYearShortNameWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForYearShortName() {

        String MacroName = "YearShort";
        String MacroPhrase = "The current Year is !ys";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current Year is " + YEAR_SHORT + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldCallUpdateTextThatContainsTheCurrentTime12HoursFormatWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForTime12Hours() {

        String MacroName = "Time12Hours";
        String MacroPhrase = "The current time in 12 hours is !t12h";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current time in 12 hours is " + TIME_12_HOURS + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldCallUpdateTextThatContainsTheCurrentTime24HoursFormatWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForTime24Hours() {

        String MacroName = "Time24Hours";
        String MacroPhrase = "The current time in 24 hour format is !t24h";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current time in 24 hour format is " + TIME_24_HOURS + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldCallUpdateTextThatContainsThePhonesMakeAndModelWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForPhoneMakeModel(){

        String makeModel = getPhoneMakeModel();

        String MacroName = "PhoneMakeModel";
        String MacroPhrase = "The phone make/model is: !phonemm";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The phone make/model is: " + makeModel + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());

    }

    @Test
    public void shouldCallUpdateTextThatContainsTheCurrentDateWhenPassedInTextContainsMacroAndTheMatchedMacroPhraseContainsDynamicValueForDate() {

        String MacroName = "Date";
        String MacroPhrase = "The current date is: !date";
        String TextBefore = MacroName + ".";

        saveMacro(MacroTestHelpers.createMacro(MacroName,
                MacroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        String textAfterExpansionWithPeriod = "The current date is: " + DATE + ".";

        macroAccessibilityServicePresenter.onAccessibilityEvent(
                TextBefore,
                TextBefore.length());

        verify(macroAccessibilityServiceView, times(1)).updateText(
                textAfterExpansionWithPeriod ,
                textAfterExpansionWithPeriod .length());
    }

    @Test
    public void shouldUndoExpandedTextBackToOriginalWhenMacroIsExpandedAndUserHitsUndoButtonAndMatchedMacroContainedADynamicPhrase(){

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {

                String[] dynamicValues = DynamicPhraseGenerator.getDynamicPhrases();

                for (String phrase: dynamicValues) {
                    macroAccessibilityServicePresenter = new MacroAccessibilityServicePresenterImpl(macroAccessibilityServiceView, appSettings);
                    String macroName = DynamicValueDrawableGenerator.getFriendlyName(phrase);

                    String macroPhrase = "The output is: " + phrase;
                    String textBefore = macroName + ".";

                    String textAfterExpansion = "The output is: "
                            + DynamicPhraseGenerator.setDynamicPhraseValue(InstrumentationRegistry.getTargetContext(),
                            phrase, US_LOCALE)
                            + ".";

                    saveMacro(MacroTestHelpers.createMacro(macroName,
                            macroPhrase, null, false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));


                    macroAccessibilityServicePresenter.onAccessibilityEvent(
                            textBefore,
                            textBefore.length());

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

    @Test
    public void shouldCallUpdateStatisticsServiceTwiceWhenAMacroIsMatchedAndThenUnDone(){
        String textBefore = "This string contains a TESTMACRO.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(textBefore, cursorPositionBefore);
        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);
        verify(macroAccessibilityServiceView, times(1)).startUpdateMacroStatisticsService("TESTMACRO", "Increase");

        macroAccessibilityServicePresenter.undoSetText();

        verify(macroAccessibilityServiceView, times(1)).startUpdateMacroStatisticsService("TESTMACRO", "Decrease");

    }

    @Test
    public void shouldNotCallShowFloatingUIForRedoButtonWhenUserUndosTextAndRedoButtonIsSetToNotBeDisplayed(){

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        when(appSettings.isRedoButtonEnabled()).thenReturn(false);
        when(appSettings.isFloatingUIEnabled()).thenReturn(true);
        when(appSettings.getFloatingUIColor()).thenReturn(1);
        when(appSettings.getOpacityValue()).thenReturn(1);

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        macroAccessibilityServicePresenter.undoSetText();
        verify(macroAccessibilityServiceView, never()).showFloatingUI(appSettings.getFloatingUIColor(), appSettings.getOpacityValue() ,"Redo");

    }

    @Test
    public void shouldCallShowFloatingUIForRedoButtonWhenUserUndosTextAndRedoButtonIsSetToBeDisplayed(){

        saveMacro(MacroTestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        when(appSettings.isRedoButtonEnabled()).thenReturn(true);
        when(appSettings.isFloatingUIEnabled()).thenReturn(true);
        when(appSettings.getFloatingUIColor()).thenReturn(1);
        when(appSettings.getOpacityValue()).thenReturn(1);

        macroAccessibilityServicePresenter.onAccessibilityEvent(TESTMACRO_TEXT_BEFORE_WITH_PERIOD,
                TESTMACRO_CURSOR_POSITION_BEFORE_WITH_PERIOD);

        macroAccessibilityServicePresenter.undoSetText();
        verify(macroAccessibilityServiceView, times(1)).showFloatingUI(appSettings.getFloatingUIColor(), appSettings.getOpacityValue() ,"Redo");

    }

    @Test
    public void textShouldContainExpandedPhraseAgainWhenUserHitsRedoButton(){
        String textBefore = "This string contains a TESTMACRO.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();

        saveMacro(MacroTestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true, MacroTestHelpers.ON_A_SPACE_OR_PERIOD));

        macroAccessibilityServicePresenter.onAccessibilityEvent(textBefore, cursorPositionBefore);
        macroAccessibilityServicePresenter.undoSetText();
        macroAccessibilityServicePresenter.redoSetText();

        verify(macroAccessibilityServiceView, times(2)).updateText(textAfter,
                cursorPositionAfter);
        verify(macroAccessibilityServiceView, times(2)).startUpdateMacroStatisticsService("TESTMACRO", "Increase");
    }
}
