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

package com.wanderfar.expander.MacroAccesibilityService;


import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.wanderfar.expander.MainActivity.MainActivity;
import com.wanderfar.expander.Models.Macro;
import com.wanderfar.expander.R;
import com.wanderfar.expander.MacroAccessibilityService.MacroAccessibilityService;
import com.wanderfar.expander.TestHelpers.MacroTestHelpers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.createAndSaveDynamicvAlueMacros;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.getSavedMacros;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.saveMacro;
import static com.wanderfar.expander.TestHelpers.TestUtils.pressDeviceBack;
import static com.wanderfar.expander.TestHelpers.TestUtils.scrollToAndClickAccessibilitySettingForApp;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class MacroAccessibilityServiceTest {

    //Tests to make sure the UI is updated correctly when the service is enabled and macros are there
    //TODO Research and finish tests later. cannot get UI Automator to start accessibility service via test. For not running tests.  Main test logic is in MacroAccessibilityServicePresenterTest

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule(MainActivity.class, true, false);

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    private Macro mTestMacro;
    private List<Macro> mSavedMacros;


    @Before
    public void setUp() {

        try {
            Intent serviceIntent =
                    new Intent(InstrumentationRegistry.getTargetContext(), MacroAccessibilityService.class);
            mServiceRule.startService(serviceIntent);

        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        //Initialize the DB and clear it
        initDB(InstrumentationRegistry.getTargetContext());


        //Build a generic Test Macro
        this.mTestMacro = MacroTestHelpers.buildGenericTestMacro();

        //Save the Macro
        saveMacro(mTestMacro);

        //Generate macros for Dynamic variables
        createAndSaveDynamicvAlueMacros();

        //Get the currently saved macros so we can test against them
        this.mSavedMacros = getSavedMacros();

        //Launch main activity
        Intent intent = new Intent();
        mMainActivityActivityTestRule.launchActivity(intent);

        //Click the snackbar to turn on accessibility setting permission
        onView(allOf(withId(android.support.design.R.id.snackbar_action)))
                .perform(click());



        //Turn on Accessibility Setting Permission
        try {
            scrollToAndClickAccessibilitySettingForApp();
            //solo.clickOnText("Expander");
            //onView(withText("Expander")).perform(click());
            //turnOnAccessibilityPermission();
            pressDeviceBack();
            pressDeviceBack();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testAccessibilityServiceFiresWhenMacroNameIsTyped(){

        //Click the Add button
        onView(withId(R.id.fab)).perform(click());


        onView(withId(R.id.input_description)).perform(clearText(), typeText(mSavedMacros.get(0).getName()));
        onView(withId(R.id.input_description)).perform(typeText("."));
        onView(withId(R.id.input_description)).perform(typeText(" "));
        onView(withId(R.id.input_name)).check(matches(isDisplayed()));

    }
}
