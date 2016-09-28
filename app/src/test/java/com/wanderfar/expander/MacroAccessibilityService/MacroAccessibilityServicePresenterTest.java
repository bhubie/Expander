package com.wanderfar.expander.MacroAccessibilityService;




import com.wanderfar.expander.Models.Macro;
import com.wanderfar.expander.Services.MacroAccessibilityServicePresenterImpl;
import com.wanderfar.expander.Services.MacroAccessibilityServiceView;
import com.wanderfar.expander.TestHelpers.TestHelpers;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class MacroAccessibilityServicePresenterTest {

    private List<Macro> macroList = new ArrayList<>();
    private MacroAccessibilityServiceView macroAccessibilityServiceView;
    private MacroAccessibilityServicePresenterImpl macroAccessibilityServicePresenter;

    @Before
    public void setupData(){


        macroAccessibilityServiceView = mock(MacroAccessibilityServiceView.class);

        macroAccessibilityServicePresenter = new MacroAccessibilityServicePresenterImpl(macroAccessibilityServiceView);


    }

    @Test
    public void matchedMacroWithASpace(){
        //Tests that if we have macro within the text bounds and the user hits space, that we successfully update the passed in text with the macro phrase


        String textBefore = "This string contains TestMacro ";
        String textAfter = "This string contains Test Macro Phrase ";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();


        macroList.add(TestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false));


        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, textBefore, cursorPositionBefore);
        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);
    }


    @Test
    public void matchedMacroWithAPeriod(){
        //Tests that if we have a macro within the text bounds and the user types a period that we successfully update the passed in text with the phrase of the matched macro

        String textBefore = "This string contains TestMacro.";
        String textAfter = "This string contains Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();


        macroList.add(TestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false));


        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, textBefore, cursorPositionBefore);
        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);
    }
    @Test
    public void invalidMacroMatch(){
        //Tests that if the text the user has typed does not contain a macro that we do not change the text to the macro phrase

        String text = "Here is some text that doesn't contain a Macro from the list";
        int cursorPosition = text.length();

        macroList.add(TestHelpers.createMacro("Test", "Test Phrase", null, false));

        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, text, cursorPosition);
        verify(macroAccessibilityServiceView, never()).updateText(text, cursorPosition);

    }

    @Test
    public void macroMatchNotWithinTextBounds(){
        //Tests that if a macro is present within the passed in text and it isn't in the text bounds range that we do not update the text even though there is a match

        String textBefore = "This string contains TestMacro.";
        String textAfter = "This string contains Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();



        macroList.add(TestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false));


        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, textBefore, cursorPositionBefore);
        verify(macroAccessibilityServiceView, never()).updateText(textBefore, cursorPositionBefore);

    }

    @Test
    public void undoSetTextTest(){
        //Tests that if the undo set text method is called that undo and set the text back to be the previous matched macro name

        String textBefore = "This string contains a TestMacro.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();


        macroList.add(TestHelpers.createMacro("TestMacro","Test Macro Phrase", "TestMacro Description", false));


        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, textBefore, cursorPositionBefore);
        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);

        macroAccessibilityServicePresenter.undoSetText();
        verify(macroAccessibilityServiceView, times(1)).updateText(textBefore, cursorPositionBefore - 1);

    }

    @Test
    public void caseSensitiveMacroInvalidMatchTest() {
        //Tests that if a macro is case sensitive and the user passes in the macro with the wrong case that we don't change the text

        String textBefore = "This string contains a testmacro.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();


        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true));
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


        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", true));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, textBefore, cursorPositionBefore);

        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);
    }

    @Test
    public void nonCaseSensitiveMacroMatchWithUpperCasePassedInText(){
        //if i have a macro that is not case sensitive and lowercase and the user passes in the macro in uppercase
        //I should have a match and pass back the expanded phrase



        String textBefore = "This string contains a testmacro.";
        String textAfter = "This string contains a Test Macro Phrase.";
        int cursorPositionBefore = textBefore.length();
        int cursorPositionAfter = textAfter.length();


        macroList.add(TestHelpers.createMacro("TESTMACRO","Test Macro Phrase", "TestMacro Description", false));
        macroAccessibilityServicePresenter.onAccessibilityEvent(macroList, textBefore, cursorPositionBefore);

        verify(macroAccessibilityServiceView, times(1)).updateText(textAfter, cursorPositionAfter);
    }

}
