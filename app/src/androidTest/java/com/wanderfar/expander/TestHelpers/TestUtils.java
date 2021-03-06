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


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.MatcherAssert.assertThat;


public class TestUtils {

    private final static String APPLICATION_NAME = "Expander";
    private static UiDevice mDevice;

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static Matcher<View> hasErrorText(final String expectedError) {
        return new BoundedMatcher<View, View>(View.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with error: " + expectedError);
            }

            @Override
            protected boolean matchesSafely(View view) {

                if (!(view instanceof EditText)) {
                    return false;
                }

                EditText editText = (EditText) view;

                return expectedError.equals(editText.getError());
            }
        };
    }

    public static ViewAssertion isGone() {
        return new ViewAssertion() {
            public void check(View view, NoMatchingViewException noView) {
                assertThat(view, new VisibilityMatcher(View.GONE));
            }
        };
    }

    public static ViewAssertion isInvisible() {
        return new ViewAssertion() {
            public void check(View view, NoMatchingViewException noView) {
                assertThat(view, new VisibilityMatcher(View.INVISIBLE));
            }
        };
    }

    private static class VisibilityMatcher extends BaseMatcher<View> {

        private int visibility;

        public VisibilityMatcher(int visibility) {
            this.visibility = visibility;
        }

        @Override public void describeTo(Description description) {
            String visibilityName;
            if (visibility == View.GONE) visibilityName = "GONE";
            else if (visibility == View.VISIBLE) visibilityName = "VISIBLE";
            else visibilityName = "INVISIBLE";
            description.appendText("View visibility must equal " + visibilityName);
        }

        @Override public boolean matches(Object o) {

            if (o == null) {
                if (visibility == View.GONE || visibility == View.INVISIBLE) return true;
                else if (visibility == View.VISIBLE) return false;
            }

            if (!(o instanceof View))
                throw new IllegalArgumentException("Object must be instance of View. Object is instance of " + o);
            return ((View) o).getVisibility() == visibility;
        }
    }

    public static void clearSharedPrefs(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public static void setBooleanPref(Context context, String prefKey, Boolean value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(prefKey, value);
        editor.apply();
    }

    public static String getPhoneMakeModel(){
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static void scrollToAndClickAccessibilitySettingForApp() throws UiObjectNotFoundException {

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiScrollable accessibilityScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);
        accessibilityScreen.setAsVerticalList();
        accessibilityScreen.scrollTextIntoView(APPLICATION_NAME);

        UiObject expanderApp = new UiObject(
                new UiSelector().text(APPLICATION_NAME));


            expanderApp.click();

    }

    public static class LauncherHelper {
        public static final UiSelector ALL_APPS_BUTTON = new UiSelector().description("Apps");
        public static final UiSelector LAUNCHER_CONTAINER = new UiSelector().scrollable(true);
        public static final UiSelector LAUNCHER_ITEM =
                new UiSelector().className(android.widget.TextView.class.getName());
    }

    public static void turnOnAccessibilityPermission() throws UiObjectNotFoundException {
        UiScrollable texterScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);
        texterScreen.setAsVerticalList();

        UiObject permission = new UiObject(
                new UiSelector().text("Off"));

        permission.click();

        UiScrollable permissionScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);
        permissionScreen.setAsVerticalList();

        UiObject permissionButton = new UiObject(
                new UiSelector().text("OK"));

        permissionButton.click();
    }

    public static void turnOffAccessibliyPermission() throws UiObjectNotFoundException {
        UiScrollable texterScreen = new UiScrollable(LauncherHelper.LAUNCHER_CONTAINER);
        texterScreen.setAsVerticalList();

        UiObject permission = new UiObject(
                new UiSelector().text("On"));

        permission.click();

        UiObject permissionButton = new UiObject(
                new UiSelector().text("OK"));

        permissionButton.click();

    }

    public static void pressDeviceBack(){
        mDevice.pressBack();
    }

}