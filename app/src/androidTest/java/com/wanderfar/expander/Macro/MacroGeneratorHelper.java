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


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.createAndSaveDynamicvAlueMacros;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.createTestMacros;
import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;

@RunWith(AndroidJUnit4.class)
public class MacroGeneratorHelper {


    // Run a test below to help generate macros for manual App testing

    @Test
    public void createDynamicValueMacros(){
        //Creates dynamic value macros for testing

        //Initialize the DB and clear it
        initDB(InstrumentationRegistry.getTargetContext());

        createAndSaveDynamicvAlueMacros();
    }

    @Test
    public void create1000TestMacros(){

        //Initialize the DB and clear it
        initDB(InstrumentationRegistry.getTargetContext());

        createTestMacros(100);
    }
}
