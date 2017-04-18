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

package com.wanderfar.expander.Application.ApplicationIntroduction

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment

import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.wanderfar.expander.R


class ApplicationIntroduction : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colorString = resources.getString(R.color.colorPrimary)

        //misc settings
        showSkipButton(true)
        isProgressButtonEnabled = true
        showStatusBar(false)

        //Slide one
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_intro_activity_slide_one_label),
                getString(R.string.app_intro_activity_slide_one_description),
                R.drawable.ic_expander_web_res512, Color.parseColor(colorString)))

        //AccessibilitySetting Slide
        addSlide(FragmentSlide.newInstance(R.layout.activity_introduction_accessibility_slide)
               )


    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
    }

}