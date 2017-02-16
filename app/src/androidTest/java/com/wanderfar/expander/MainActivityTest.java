package com.wanderfar.expander;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import com.wanderfar.expander.MainActivity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.ON_A_SPACE_OR_PERIOD;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.buildGenericTestMacro;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.createMacro;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.saveMacro;
import static com.wanderfar.expander.TestHelpers.TestUtils.atPosition;
import static com.wanderfar.expander.TestHelpers.TestUtils.isGone;



@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule(MainActivity.class);


    private UiDevice mDevice;

    private Boolean isPermissionOff;

    private UiObject mAccessibilityPermissionSetting;

    private String mApplicationName;
    private final String SETTINGS_APP_NAME = "Settings";
    private final String ACCESSIBILITY_SETTINGS_NAME = "Accessibility";
    private final String APP_BUTTON = "Apps";


    @Before
    public void initDevice(){
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());


        //get application name
        mApplicationName = mActivityRule.getActivity().getResources().getString(R.string.app_name);

        // Start from the home screen
        mDevice.pressHome();

        //Initialize the DB and clear it
        initDB(InstrumentationRegistry.getTargetContext());

    }

    @Test
    public void whenNoMacroIsSavedNoMacroFoundMessageShouldBeDisplayed(){
        //Launch the main activity
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        //Validate that the no Macro Message is visible

        onView(withId(R.id.noMacroFound)).check(matches(isDisplayed()));
    }

    @Test
    public void whenAMacroIsLoadedNoMacroFoundMessageShouldNotBeDisplayed(){

        //create a generic test macro and save it
        //Save the Macro
        saveMacro(buildGenericTestMacro());
        
        //Launch the main activity
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        //Validate that the no Macro Message is not visible
        onView(withId(R.id.noMacroFound)).check(isGone());
    }

    @Test
    public void whenNoSortBySettingIsFoundSortByNameShouldBeSetAsDefaultAndBeChecked(){
       //TODO Figure out way with espresso to test that menu item is checked
        /* //Clear shared Prefs
        clearSharedPrefs(InstrumentationRegistry.getTargetContext());

        //Launch the main activity
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.action_sort_by)).perform(click());


        onView(withText("Shortcut Name")).check(matches(isChecked()));*/

    }

    @Test
    public void whenNoSortBySettingIsFoundMacrosShouldBeSortedAlphabeticallyByMacroName(){
        //Create A Macro Named B
        saveMacro(createMacro("B", "Macro Phrase", "Macro Description", false, ON_A_SPACE_OR_PERIOD));

        //Create A Macro Named A
        saveMacro(createMacro("A", "Macro Phrase", "Macro Description", false, ON_A_SPACE_OR_PERIOD));

        //Launch the main activity
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        //Validate that A is first, in view and that B is second
        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText("A")))));

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(1, hasDescendant(withText("B")))));

    }

}
