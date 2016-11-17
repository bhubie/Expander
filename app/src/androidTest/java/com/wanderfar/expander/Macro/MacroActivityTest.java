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

package com.wanderfar.expander.Macro;



import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import com.wanderfar.expander.MainActivity.MainActivity;
import com.wanderfar.expander.TestHelpers.MacroTestHelpers;
import com.wanderfar.expander.Models.Macro;
import com.wanderfar.expander.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.saveMacro;
import static com.wanderfar.expander.TestHelpers.TestUtils.atPosition;
import static com.wanderfar.expander.TestHelpers.TestUtils.clearSharedPrefs;
import static com.wanderfar.expander.TestHelpers.TestUtils.hasErrorText;
import static com.wanderfar.expander.TestHelpers.TestUtils.isGone;
import static com.wanderfar.expander.TestHelpers.TestUtils.setBooleanPref;
import static java.lang.Thread.sleep;



@RunWith(AndroidJUnit4.class)
public class MacroActivityTest {

    @Rule
    public ActivityTestRule<MacroActivity> mActivityTestRule = new ActivityTestRule(MacroActivity.class, true, false);

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule(MainActivity.class, true, false);

    private Macro testMacro;

    @Before
    public void setUp() {

        //Initialize the DB and clear it
        initDB(InstrumentationRegistry.getTargetContext());

        //Build a generic Test Macro
        this.testMacro = MacroTestHelpers.buildGenericTestMacro();

        //Save the Macro
        saveMacro(testMacro);
    }



    //Start activity tests

    @Test
    public void newMacroActivityStart(){
        //Test if activity is started new that existing macro isn't loaded
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.input_name)).check(matches(withText("")));
    }

    @Test
    public void existingMacroActivityStart(){
        //Test if activity is started  via existing macro, that macro is pulled up and proper fields are pulled in


        //create an intent to pass in the macro
        Intent intent = new Intent();
        intent.putExtra("MACRONAME", testMacro.getName());

        //launch the activity
        mActivityTestRule.launchActivity(intent);

        //Verify results
        onView(withId(R.id.input_name)).check(matches(withText(testMacro.getName())));
        onView(withId(R.id.input_phrase)).check(matches(withText(testMacro.getPhrase())));
        onView(withId(R.id.input_description)).check(matches(withText(testMacro.getDescription())));
        if (testMacro.isCaseSensitive()){
            onView(withId(R.id.case_sensitive_switch)).check(matches(isChecked()));
        }

    }

    //Save tests

    @Test
    public void saveValidMacroTest() {
        //Test that if user creates macro and all valid fields are filled in that macro is saved and user is back to main activity

        //Launch the activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Fill in name
        onView(withId(R.id.input_name)).perform(clearText(), typeText("ANewTestMacro"));

        //Fill in Phrase
        onView(withId(R.id.input_phrase)).perform(clearText(), typeText("A New Test Phrase"));

        //Click the Save button
        onView(withId(R.id.action_save)).perform(click());

        //Validate we are returned to main activity and that the just saved macro exists
        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(1, hasDescendant(withText("ANewTestMacro")))));

    }

    @Test
    public void saveMacroWithNoNameError(){
        //Tests that if user hits save and macro name isn't filled in that we throw save error

        //Launch the activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Fill in Phrase
        onView(withId(R.id.input_phrase)).perform(clearText(), typeText("Test Phrase"));

        //Click the Save button
        onView(withId(R.id.action_save)).perform(click());

        //Validate that the input name is throwing an error and the record wasn't saved
        onView(withId(R.id.input_name)).check(matches(hasErrorText("Please Provide a name before saving")));
    }

    @Test
    public void saveMacroWithNoPhraseError(){
        //Tests that if user hits save and the phrase isn't filled in that we throw no phrase error

        //Launch the activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Fill in Name
        onView(withId(R.id.input_name)).perform(clearText(), typeText("TestMacro"));

        //Click the Save button
        onView(withId(R.id.action_save)).perform(click());

        //Validate that the input phrase is throwing an error and the record wasn't saved
        onView(withId(R.id.input_phrase)).check(matches(hasErrorText("Please provide a phrase before saving")));
    }

    @Test
    public void saveMacroWithDuplicateNameError(){
        //Tests that if user hits save and macro name is duplicate that we throw duplicate name error


        //Launch the activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Fill in name with existing macro name
        onView(withId(R.id.input_name)).perform(clearText(), typeText(testMacro.getName()));

        //Fill in phrase with existing macro phrase
        onView(withId(R.id.input_phrase)).perform(clearText(), typeText(testMacro.getPhrase()));

        //Click the Save button
        onView(withId(R.id.action_save)).perform(click());

        //Validate that the input name is throwing duplicate macro name and doesn't save
        onView(withId(R.id.input_name)).check(matches(hasErrorText("Duplicate name found, please try again")));
    }


    //Delete tests
    @Test
    public void newMacroDeleteTest(){
        //Tests that if user hits delete button on a new macro that isn't saved we don't do anything

        //Launch the activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Click the delete button
        onView(withId(R.id.action_trash)).perform(click());

        //validate nothing happened and we are in the same activity
        onView(withId(R.id.input_name)).check(matches(isDisplayed()));
    }

    @Test
    public void existingMacroDeleteTest(){
        //Tests that if user hits delete button on existing macro macro is deleted and returned to main activity

        //create an intent to pass in the macro
        Intent intent = new Intent();
        intent.putExtra("MACRONAME", testMacro.getName());

        //launch the activity
        mActivityTestRule.launchActivity(intent);

        //press the delete button
        onView(withId(R.id.action_trash)).perform(click());

        //Validate we are now back to the main activity and that the macro is not there
        onView(withId(R.id.cv)).check(doesNotExist());

    }

    //Back button tests
    @Test
    public void existingMacroChangeMadeDeviceBackButtonSaveChangesTest(){
        //Test that if user hits the device back button on existing macro and a change was made that we ask if the want to save
        //And if they hit save we save the changes and go back

        //create an intent to pass in the macro
        Intent intent = new Intent();
        intent.putExtra("MACRONAME", testMacro.getName());

        //launch the activity
        mActivityTestRule.launchActivity(intent);

        //Change the macro phrase
        onView(withId(R.id.input_phrase)).perform(clearText(), typeText("New Macro Phrase"));

        //press the back button to get rid of keyboard
        pressBack();

        //Press back again to go back
        pressBack();

        //Validate save changes pop up is called
        onView(withText("Changes Found")).check(matches(isDisplayed()));

        //Press the save changes button
        onView(withId(android.R.id.button1)).perform(click());

        //validate that the macro shown now has the new macro phrase that was previously entered
        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText("New Macro Phrase")))));
    }

    @Test
    public void existingMacroNoChangeMadeDeviceBackButtonTest() {
        //Test that if a user hits back button on existing macro and no change was made that we go back

        //launch the main activity
        Intent intent = new Intent();
        mMainActivityActivityTestRule.launchActivity(intent);

        //Click the macro
        onView(withId(R.id.noteListRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //press the back button to get rid of keyboard
        pressBack();

        //Press back again to go back
        pressBack();

        //Validate that the the pop up that detects changes is not called
        onView(withText("Changes Found")).check(doesNotExist());
    }

    @Test
    public void existingMacroChangeMadeToolbarBackButtonSaveChangesTest(){
        //Test that if user hits the toolbar back button on existing macro and a change was made that we ask if the want to save
        //And if they hit save we save the changes and go back

        //create an intent to pass in the macro
        Intent intent = new Intent();
        intent.putExtra("MACRONAME", testMacro.getName());

        //launch the activity
        mActivityTestRule.launchActivity(intent);

        //Change the macro phrase
        onView(withId(R.id.input_phrase)).perform(clearText(), typeText("New Macro Phrase"));

        //press the back button to get rid of keyboard
        onView(withContentDescription("Navigate up")).perform(click());

        //Validate save changes pop up is called
        onView(withText("Changes Found")).check(matches(isDisplayed()));

        //Press the save changes button
        onView(withId(android.R.id.button1)).perform(click());

        //validate that the macro shown now has the new macro phrase that was previously entered
        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText("New Macro Phrase")))));
    }

    @Test
    public void existingMacroNoChangeMadeToolbarBackButtonTest() {
        //Test that if a user hits toolbar back button on existing macro and no change was made that we go back

        //launch the main activity
        Intent intent = new Intent();
        mMainActivityActivityTestRule.launchActivity(intent);

        //Click the macro
        onView(withId(R.id.noteListRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //press the back button to get rid of keyboard
        onView(withContentDescription("Navigate up")).perform(click());

        //Validate that the the pop up that detects changes is not called
        onView(withText("Changes Found")).check(doesNotExist());
    }

    @Test
    public void newMacroDeviceBackButtonTest() {
        //Test that if a user hits back button on new macro, that we go back

        //launch the main activity
        Intent intent = new Intent();
        mMainActivityActivityTestRule.launchActivity(intent);

        //Wait for the activity to load all the way
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Click the new button
        onView(withId(R.id.fab)).perform(click());

        //press the back button to get rid of keyboard
        onView(withContentDescription("Navigate up")).perform(click());

        //validate that we are back on the main activity and the macro is listed
        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText(testMacro.getName())))));

    }


    @Test
    public void dynamicValuesTurnedOffTest(){
        //Validates that if dynamic values is turned off in the settings that the add dynamic values button is not present

        //Clear shared Prefs
        clearSharedPrefs(InstrumentationRegistry.getTargetContext());

        //Set pref
        setBooleanPref(InstrumentationRegistry.getTargetContext(), "isDynamicValuesEnabled", false);

        //launch the main activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Validate button is not present
        onView(withId(R.id.dynamic_value_button)).check(isGone());

    }

    @Test
    public void dynamicValuesTurnedOnTest(){
        //Validates that if dynamic values is turned on in the settings that the add dynamic values button is present

        //Clear shared Prefs
        clearSharedPrefs(InstrumentationRegistry.getTargetContext());

        //Set pref
        setBooleanPref(InstrumentationRegistry.getTargetContext(), "isDynamicValuesEnabled", true);

        //launch the main activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Validate button is present
        onView(withId(R.id.dynamic_value_button)).check(matches(isDisplayed()));
    }

    @Test
    public void addDynamicValue(){
        //Validates that when a user adds a dynamic value from the pop up that the dynamic value is added to the phrase edit text
        //And that it properly adds a space if the last character didn't contain a space

        //Clear shared Prefs
        clearSharedPrefs(InstrumentationRegistry.getTargetContext());

        //Set pref
        setBooleanPref(InstrumentationRegistry.getTargetContext(), "isDynamicValuesEnabled", true);

        //launch the main activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Input text into the phrase field
        onView(withId(R.id.input_phrase)).perform(clearText(), typeText("Test Phrase"));

        //Click the dynamic value button
        onView(withId(R.id.dynamic_value_button)).perform(click());

        //Click the first item in the recyclerview
        onView(withId(R.id.dynamicValueRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //Validate the phrase now contains the dynamic value phrase for day of week
        onView(withId(R.id.input_phrase)).check(matches(withText("Test Phrase !d")));
    }

    @Test
    public void clickDynamicValueHeader(){
        //Validates that when you click the add dynamic value header for Date/Time that nothing happens

        //Clear shared Prefs
        clearSharedPrefs(InstrumentationRegistry.getTargetContext());

        //Set pref
        setBooleanPref(InstrumentationRegistry.getTargetContext(), "isDynamicValuesEnabled", true);

        //launch the main activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Click the dynamic value button
        onView(withId(R.id.dynamic_value_button)).perform(click());

        //Click the first item in the recyclerview
        onView(withId(R.id.dynamicValueRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Validate the phrase now contains the dynamic value phrase for day of week
        onView(withId(R.id.dynamicValueRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteDynamicValueFromPhrase(){
        //Tests that if the user hits the back space button and they are deleting a dynamic value
        //That the whole dynamic value is deleted

        //Clear shared Prefs
        clearSharedPrefs(InstrumentationRegistry.getTargetContext());

        //Set pref
        setBooleanPref(InstrumentationRegistry.getTargetContext(), "isDynamicValuesEnabled", true);

        //launch the main activity
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        //Input text into the phrase field
        onView(withId(R.id.input_phrase)).perform(clearText(), typeText("Test Phrase !d"));

        //Press the delete key in input phrase
        onView(withId(R.id.input_phrase)).perform(pressKey(KeyEvent.KEYCODE_DEL));

        //Validate the phrase now contains the dynamic value phrase for day of week
        onView(withId(R.id.input_phrase)).check(matches(withText("Test Phrase ")));
    }
}
