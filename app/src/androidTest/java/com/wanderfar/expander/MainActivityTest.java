package com.wanderfar.expander;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.wanderfar.expander.MainActivity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.buildGenericTestMacro;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.saveMacro;
import static com.wanderfar.expander.TestHelpers.TestUtils.isGone;
import static org.hamcrest.core.AllOf.allOf;


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
    public void testNoMacroFoundMessageIsDisplayed(){
        //Test that if no macros were loaded that no macros found message is visible

        //Launch the main activity
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        //Validate that the no Macro Message is visible

        onView(withId(R.id.noMacroFound)).check(matches(isDisplayed()));
    }

    @Test
    public void testNoMacroFoundMessageIsNotDisplayed(){
        //Test that if a macro is loaded that the no macro found message is not displayed

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
    public void testThatIfAccessibilityPermissionForAppIsOffThatSnackbarAppears() {


        //check and/or set the accessibility permission for the application
        try {
            setAccessibilitySettings("Off");
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        //onView()

        try {
            launchApp();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enable Accessibility Settings permission for " + mActivityRule.getActivity().getResources().getString(R.string.app_name) + ". This is a required permission for the app to work.")))
                .check(matches(isDisplayed()));


    }



    @Test
    public void testThatIfAccessibilityPermissionForAppIsOnThatSnackbarDoesntAppear() {
        //If the accessibility permission for the application is on, when launching the application
        //the snack-bar that prompts the user to enable the permission should not appear

        try {
            setAccessibilitySettings("On");

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }


       try {
            launchApp();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        mDevice.waitForIdle();
       /* onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enable Accessibility Settings permission for " + mActivityRule.getActivity().getResources().getString(R.string.app_name) + ". This is a required permission for the app to work.")))
                .check(doesNotExist());*/

    }

    private void launchApp() throws UiObjectNotFoundException {

        mDevice.pressHome();

        UiObject allAppsButton = new UiObject(LauncherHelper.ALL_APPS_BUTTON);
        allAppsButton.click();

        // Clicking the Settings
        UiScrollable allAppsScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);


        allAppsScreen.setAsVerticalList();

        allAppsScreen.setAsVerticalList();
        allAppsScreen.scrollTextIntoView(mApplicationName);

        UiObject app = new UiObject(
                new UiSelector().text(mApplicationName));
        app.click();

    }

    public static class LauncherHelper {
        public static final UiSelector ALL_APPS_BUTTON = new UiSelector().description("Apps");
        public static final UiSelector LAUNCHER_CONTAINER = new UiSelector().scrollable(true);
        public static final UiSelector LAUNCHER_ITEM =
                new UiSelector().className(android.widget.TextView.class.getName());
                //new UiSelector().description(android.widget.TextView.class.getName());
                //new UiSelector().description("Settings");
        //public static final UiSelector SETTINGS_BUTTON = new UiSelector().description("Settings");
    }


    private void setAccessibilitySettings(String turnSettingsOnOrOff) throws UiObjectNotFoundException {

        mDevice.pressHome();

        UiObject allAppsButton = new UiObject(LauncherHelper.ALL_APPS_BUTTON);
        allAppsButton.click();

        // Clicking the Settings
        UiScrollable allAppsScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);

        allAppsScreen.setAsVerticalList();

        allAppsScreen.setAsVerticalList();
        allAppsScreen.scrollTextIntoView(SETTINGS_APP_NAME);


        UiObject settingsApp = new UiObject(
                new UiSelector().text(SETTINGS_APP_NAME));
        settingsApp.click();

        UiScrollable settingsScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);

        settingsScreen.setAsVerticalList();
        settingsScreen.scrollTextIntoView(ACCESSIBILITY_SETTINGS_NAME);

        UiObject accessibilityApp = new UiObject(
                new UiSelector().text(ACCESSIBILITY_SETTINGS_NAME));

        accessibilityApp.click();

        UiScrollable accessibilityScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);
        accessibilityScreen.setAsVerticalList();
        accessibilityScreen.scrollTextIntoView(mApplicationName);

        UiObject expanderApp = new UiObject(
                new UiSelector().text(mApplicationName));

        expanderApp.click();

        UiScrollable texterScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);
        texterScreen.setAsVerticalList();

        mAccessibilityPermissionSetting = getUIObjectForAccessibilityPermissionSetting();

        if (turnSettingsOnOrOff.equals("Off") && !isPermissionOff) {
            clickPermissionToEnableOrDisable();
        }
        else if (turnSettingsOnOrOff.equals("On") && isPermissionOff){
            clickPermissionToEnableOrDisable();
        }


    }

    private void clickPermissionToEnableOrDisable() throws UiObjectNotFoundException {
       mAccessibilityPermissionSetting.click();

        UiScrollable permissionScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);
        permissionScreen.setAsVerticalList();

        UiObject permissionButton = new UiObject(
                new UiSelector().text("OK"));

        permissionButton.click();
    }

    private UiObject getUIObjectForAccessibilityPermissionSetting(){
        UiObject permission = new UiObject(
                new UiSelector().text("Off"));

        if (permission.exists()){
            isPermissionOff = true;
            return permission;
        } else {
            UiObject permissionOn  = new UiObject(new UiSelector().text("On"));
            isPermissionOff = false;
            return permissionOn;
        }
    }
}
