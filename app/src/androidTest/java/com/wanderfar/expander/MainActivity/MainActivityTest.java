package com.wanderfar.expander.MainActivity;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import com.wanderfar.expander.MainActivity.MainActivity;
import com.wanderfar.expander.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.ON_A_SPACE_OR_PERIOD;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.buildGenericTestMacro;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.createMacro;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.getMacro;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.saveMacro;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.setMacroLastUsed;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.setMacroUsageCount;
import static com.wanderfar.expander.TestHelpers.TestUtils.atPosition;
import static com.wanderfar.expander.TestHelpers.TestUtils.clearSharedPrefs;
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
    public void macrosShouldSortBasedOnTheCurrentSortBySettingTheUserHasSelected(){

        clearSharedPrefs(InstrumentationRegistry.getTargetContext());

        //Create A Macro Named B
        saveMacro(createMacro("B", "Macro Phrase", "Macro Description", false, ON_A_SPACE_OR_PERIOD));

        //Create A Macro Named A
        saveMacro(createMacro("A", "Macro Phrase", "Macro Description", false, ON_A_SPACE_OR_PERIOD));

        //Create A Macro Named C
        saveMacro(createMacro("C", "Macro Phrase", "Macro Description", false, ON_A_SPACE_OR_PERIOD));

        //Set Usage Count Fields
        setMacroUsageCount(getMacro("B"), 2);
        setMacroUsageCount(getMacro("C"), 1);

        //Set Last Used Fields
        try {
            Date cDate = new SimpleDateFormat("MM/dd/yyyy").parse("02/01/2017");
            setMacroLastUsed(getMacro("C"), cDate);

            Date bDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/2017");
            setMacroLastUsed(getMacro("B"), bDate);

            Date aDate = new SimpleDateFormat("MM/dd/yyyy").parse("12/01/2016");
            setMacroLastUsed(getMacro("A"), aDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Launch the main activity
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        //Validate that A is first, in view and that B is second as sort by Name is default
        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText("A")))));

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(1, hasDescendant(withText("B")))));

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(2, hasDescendant(withText("C")))));


        //Next sort by Usage Count
        onView(withId(R.id.action_sort_by)).perform(click());
        onView(withText("Usage Count")).perform(click());

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText("B")))));

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(1, hasDescendant(withText("C")))));

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(2, hasDescendant(withText("A")))));

        //Next sort by last used
        onView(withId(R.id.action_sort_by)).perform(click());
        onView(withText("Last Used")).perform(click());

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText("C")))));

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(1, hasDescendant(withText("B")))));

        onView(withId(R.id.noteListRecyclerView))
                .check(matches(atPosition(2, hasDescendant(withText("A")))));


    }

}
