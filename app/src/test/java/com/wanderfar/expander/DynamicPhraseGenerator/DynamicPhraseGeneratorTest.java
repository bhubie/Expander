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



package com.wanderfar.expander.DynamicPhraseGenerator;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import com.wanderfar.expander.TestHelpers.TestHelpers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.wanderfar.expander.TestHelpers.TestHelpers.setFinalStatic;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest({DynamicPhraseGenerator.class})
public class DynamicPhraseGeneratorTest {

    private final String DAY_OF_WEEK_PHRASE = "!d";
    private final String DAY_Of_WEEK_SHORT_PHRASE = "!ds";
    private final String DAY_OF_MONTH_PHRASE = "!dm";
    private final String MONTH_PHRASE = "!m";
    private final String MONTH_SHORT_PHRASE = "!ms";
    private final String YEAR_PHRASE = "!y";
    private final String YEAR_SHORT_PHRASE = "!ys";
    private final String TIME_12HR_PHRASE = "!t12h";
    private final String TIME_24HR_PHRASE = "!t24h";
    private final String CLIPBOARD_PHRASE = "!clipboard";
    private final String PHONE_MAKE_MODEL_PHRASE = "!phonemm";
    private final String DATE_PHRASE = "!date";


    private final Locale US_LOCALE = new Locale("en", "US");
    private final Locale UK_LOCALE = new Locale("en", "GB");

    private final String MONDAY = "Monday";
    private final String MONDAY_SHORT = "Mon";
    private final String TUESDAY = "Tuesday";
    private final String TUESDAY_SHORT = "Tue";
    private final String WEDNESDAY = "Wednesday";
    private final String WEDNESDAY_SHORT = "Wed";
    private final String THURSDAY = "Thursday";
    private final String THURSDAY_SHORT = "Thu";
    private final String FRIDAY = "Friday";
    private final String FRIDAY_SHORT = "Fri";
    private final String SATURDAY = "Saturday";
    private final String SATURDAY_SHORT = "Sat";
    private final String SUNDAY = "Sunday";
    private final String SUNDAY_SHORT = "Sun";

    @Before
    public void setup(){

    }

    @Mock
    Context mMockContext;

    @Mock
    Build mMockBuild;




    //Long day of week tests

    @Test
    public void dayOfWeekPhraseTestMonday(){
        //Tests that if the dynamic phrase for the day of the week is used and the day of the week is Monday that Monday is returned
        //!d

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(MONDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_OF_WEEK_PHRASE, US_LOCALE);
        assertEquals(MONDAY, result);
    }

    @Test
    public void dayOfWeekPhraseTestTuesday(){
        //Tests that if the dynamic phrase for the day of the week is used and the day of the week is Tuesday that Tuesday is returned
        //!d

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(TUESDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_OF_WEEK_PHRASE, US_LOCALE);
        assertEquals(TUESDAY, result);
    }

    @Test
    public void dayOfWeekPhraseTestWednesday(){
        //Tests that if the dynamic phrase for the day of the week is used and the day of the week is Wednesday that Wednesday is returned
        //!d

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(WEDNESDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_OF_WEEK_PHRASE, US_LOCALE);
        assertEquals(WEDNESDAY, result);
    }

    @Test
    public void dayOfWeekPhraseTestThursday(){
        //Tests that if the dynamic phrase for the day of the week is used and the day of the week is Thursday that Thursday is returned
        //!d

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(THURSDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_OF_WEEK_PHRASE, US_LOCALE);
        assertEquals(THURSDAY, result);
    }

    @Test
    public void dayOfWeekPhraseTestFriday(){
        //Tests that if the dynamic phrase for the day of the week is used and the day of the week is Friday that Friday is returned
        //!d

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(FRIDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_OF_WEEK_PHRASE, US_LOCALE);
        assertEquals(FRIDAY, result);
    }

    @Test
    public void dayOfWeekPhraseTestSaturday(){
        //Tests that if the dynamic phrase for the day of the week is used and the day of the week is Saturday that Saturday is returned
        //!d

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SATURDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_OF_WEEK_PHRASE, US_LOCALE);
        assertEquals(SATURDAY, result);
    }

    @Test
    public void dayOfWeekPhraseTestSunday(){
        //Tests that if the dynamic phrase for the day of the week is used and the day of the week is Sunday that Sunday is returned
        //!d

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_OF_WEEK_PHRASE, US_LOCALE);
        assertEquals(SUNDAY, result);
    }


    //Short day of week tests
    @Test
    public void dayOfWeekShortPhraseTestMonday(){
        //Tests that if the dynamic phrase for the day of the week short is used, and the day of the week is monday that Mon is returned
        //!ds

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(MONDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_Of_WEEK_SHORT_PHRASE, US_LOCALE);
        assertEquals(MONDAY_SHORT, result);
    }

    @Test
    public void dayOfWeekShortPhraseTestTuesday(){
        //Tests that if the dynamic phrase for the day of the week short is used, and the day of the week is Tuesday that Tue is returned
        //!ds

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(TUESDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_Of_WEEK_SHORT_PHRASE, US_LOCALE);
        assertEquals(TUESDAY_SHORT, result);
    }

    @Test
    public void dayOfWeekShortPhraseTestWednesday(){
        //Tests that if the dynamic phrase for the day of the week short is used, and the day of the week is Wednesday that Wed is returned
        //!ds

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(WEDNESDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_Of_WEEK_SHORT_PHRASE, US_LOCALE);
        assertEquals(WEDNESDAY_SHORT, result);
    }

    @Test
    public void dayOfWeekShortPhraseTestThursday(){
        //Tests that if the dynamic phrase for the day of the week short is used, and the day of the week is Thursday that Thu is returned
        //!ds

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(THURSDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_Of_WEEK_SHORT_PHRASE, US_LOCALE);
        assertEquals(THURSDAY_SHORT, result);
    }

    @Test
    public void dayOfWeekShortPhraseTestFriday(){
        //Tests that if the dynamic phrase for the day of the week short is used, and the day of the week is Friday that Fri is returned
        //!ds

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(FRIDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_Of_WEEK_SHORT_PHRASE, US_LOCALE);
        assertEquals(FRIDAY_SHORT, result);
    }

    @Test
    public void dayOfWeekShortPhraseTestSaturday(){
        //Tests that if the dynamic phrase for the day of the week short is used, and the day of the week is Saturday that Sat is returned
        //!ds

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SATURDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_Of_WEEK_SHORT_PHRASE, US_LOCALE);
        assertEquals(SATURDAY_SHORT, result);
    }

    @Test
    public void dayOfWeekShortPhraseTestSunday(){
        //Tests that if the dynamic phrase for the day of the week short is used, and the day of the week is Sunday that Sun is returned
        //!ds

        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_Of_WEEK_SHORT_PHRASE, US_LOCALE);
        assertEquals(SUNDAY_SHORT, result);
    }

    //Day of Month Tests

    @Test
    public void dayOfMonthPhraseTest(){
        //Test that if the dynamic phrase for day of month is used that we return the day of the month
        //Day we are mocking is 6/21/2015
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DAY_OF_MONTH_PHRASE, US_LOCALE);
        assertEquals("21", result);
    }

    //Month Name Tests

    @Test
    public void monthNamePhraseTest(){
        //Tests that if the dynamic phrase for month is used that we return the month name of the current month
        //The month we are mocking is June
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, MONTH_PHRASE, US_LOCALE);
        assertEquals("June", result);

    }

    @Test
    public void monthNameShortPhraseTest(){
        //Tests that if the dynamic phrase for month short is used that we return the short version of the month
        //The month we are mocking is June
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, MONTH_SHORT_PHRASE, US_LOCALE);
        assertEquals("Jun", result);

    }

    //Year Tests
    @Test
    public void yearPhraseTest(){
        //Tests that if the dynamic phrase for year is used that we return the current year
        //The year we are mocking is 2015
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, YEAR_PHRASE, US_LOCALE);
        assertEquals("2015", result);

    }

    @Test
    public void yearShortPhraseTest(){
        //Tests that if the dynamic phrase for year short is used that we return the current year in short form
        //The year we are mocking is 2015
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, YEAR_SHORT_PHRASE, US_LOCALE);
        assertEquals("15", result);

    }

    //Time Tests
    @Test
    public void time12HourTest(){
        //Tests that if the dynamic phrase for time 12 hours is used, we return the current time in 12 hour format
        //The time we are mocking is 5:35 PM
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, TIME_12HR_PHRASE, US_LOCALE);
        assertEquals("05:35 PM", result);

    }

    @Test
    public void time24HourTest(){
        //Tests that if the dynamic phrase for time 24 hours is used, we return the current time in 24 hour format
        //The time we are mocking is 5:35 PM
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, TIME_24HR_PHRASE, US_LOCALE);
        assertEquals("17:35", result);

    }

    @Test
    public void clipboardTest(){
        //Tests that if the dynamic phrase for clipboard is used we return what is in the clipboard

        String clipboardText = "Clipboard Text";

        //Mock a clipboard
        ClipboardManager clipboard = Mockito.mock(ClipboardManager.class);
        ClipData clipData = Mockito.mock(ClipData.class);
        ClipData.Item item = Mockito.mock(ClipData.Item.class);
        CharSequence charSequence = Mockito.mock(CharSequence.class);


        when(mMockContext.getSystemService(CLIPBOARD_SERVICE)).thenReturn(clipboard);
        when(clipboard.getPrimaryClip()).thenReturn(clipData);
        when(clipData.getItemAt(0)).thenReturn(item);
        when(item.getText()).thenReturn(charSequence);
        when(charSequence.toString()).thenReturn(clipboardText);


        String results = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, CLIPBOARD_PHRASE, US_LOCALE);

        assertEquals(clipboardText, results);
    }

    @Test
    public void noClipboardTest(){
        //Tests that when the clipboard is null that we return an empty string when the phrase is expanded
        //Only need to mock the clipboard manager as the Clip data will be null if we don't mock it

        ClipboardManager clipboard = Mockito.mock(ClipboardManager.class);
        when(mMockContext.getSystemService(CLIPBOARD_SERVICE)).thenReturn(clipboard);
        String results = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, CLIPBOARD_PHRASE, US_LOCALE);
        assertEquals("", results);
    }

    @Test
    public void dateTest(){
        //Tests that when the dynamic phrase for date is used that we return the current date

        //The date we are mocking is 06/21/15
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DATE_PHRASE, US_LOCALE);
        assertEquals("6/21/15", result);
    }

    @Test
    public void dateTestUKLocale(){
        //Tests that when the dynamic phrase for date is used and the User's Locale is UK that we return UK Locale

        //The date we are mocking is 06/21/15
        mockStatic(Calendar.class);
        Date date = TestHelpers.getAPastDayOfTheWeek(SUNDAY);
        when(Calendar.getInstance().getTime()).thenReturn(date);
        String result = DynamicPhraseGenerator.setDynamicPhraseValue(mMockContext, DATE_PHRASE, UK_LOCALE);
        assertEquals("21/06/15", result);
    }


}
