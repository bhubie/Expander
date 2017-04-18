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


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wanderfar.expander.R


class FragmentSlide : Fragment() {

    private val ARG_LAYOUT_RES_ID = "layoutResId"
    private var layoutResId: Int = 0

    companion object{
        fun  newInstance(resID: Int) : Fragment {
            val accessibilitySlide = FragmentSlide()

            val args = Bundle()
            args.putInt("layoutResId", resID)
            accessibilitySlide.arguments = args

            return accessibilitySlide
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null && arguments.containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = arguments.getInt(ARG_LAYOUT_RES_ID)
        }


    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(layoutResId, container, false)
        if (layoutResId == R.layout.activity_introduction_accessibility_slide){
            println("Setting click listener!")
            val root = view?.findViewById(R.id.main)
            root?.setOnClickListener {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivityForResult(intent, 0)
            }
        }

        return view
    }

}
