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

package com.wanderfar.expander.DynamicValue

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.AttributeSet
import com.wanderfar.expander.R


class DynamicValueTextView : AppCompatTextView {

    var displayDrawableForDynamicValue : Boolean = true
    var mIconSize: Int = 0
    var mIconAlignment: Int = 0
    var mIconTextSize: Int = 0

    constructor(context: Context) :  super(context) {

        mIconSize = textSize.toInt()
        mIconAlignment = textSize.toInt()
        mIconTextSize = textSize.toInt()
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


        mIconSize = textSize.toInt()
        mIconAlignment =  textSize.toInt()
        mIconTextSize =  textSize.toInt()

        text = text

        array.recycle()
    }

    override fun setText(text: CharSequence, type: BufferType) {
        var text = text
        if (displayDrawableForDynamicValue){
            if (!TextUtils.isEmpty(text)) {
                val builder = SpannableStringBuilder(text)

                DynamicValueDrawableGenerator.addDynamicDrawables(context, builder,
                        mIconSize, mIconAlignment, mIconTextSize, 5.0f, 25.0f)
                text = builder
            }
        }
        super.setText(text, type)
    }


    fun setDisplayDynamicDrawable (displayDynamicDrawable : Boolean){
        this.displayDrawableForDynamicValue = displayDynamicDrawable
    }
}