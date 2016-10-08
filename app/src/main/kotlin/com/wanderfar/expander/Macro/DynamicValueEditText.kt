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

package com.wanderfar.expander.Macro

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.wanderfar.expander.R


class DynamicValueEditText : AppCompatEditText {

    var displayDrawableForDynamicValue : Boolean = true

    constructor(context: Context) :  super(context) {

        //mEmojiconSize = textSize as Int
        //mEmojiconTextSize = textSize as Int
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){

        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {

        init(attrs)
    }

    fun init(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.DynamicValueEditText)

        displayDrawableForDynamicValue = array.getBoolean(
                R.styleable.DynamicValueEditText_displayDrawableForDynamicValue, true)

        array.recycle()
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if(displayDrawableForDynamicValue == true){
            updateDynamicTextWithDrawable()
        }
    }

    private fun updateDynamicTextWithDrawable() {
        DynamicValueDrawableGenerator.addDynamicDrawables(text)
    }

    fun setDisplayDynamicDrawable (displayDynamicDrawable : Boolean){
        this.displayDrawableForDynamicValue = displayDynamicDrawable
    }
}