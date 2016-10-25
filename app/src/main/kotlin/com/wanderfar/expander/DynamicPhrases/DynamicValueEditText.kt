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

package com.wanderfar.expander.DynamicPhrases

import android.content.Context
import android.support.v7.widget.AppCompatEditText

import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.wanderfar.expander.DynamicPhrases.DynamicValueDrawableGenerator
import com.wanderfar.expander.DynamicPhrases.DynamicValueDrawableSpan
import com.wanderfar.expander.R


class DynamicValueEditText : AppCompatEditText {

    var displayDrawableForDynamicValue : Boolean = true
    var mIconSize: Int = 0
    var mIconAlignment: Int = 0
    var mIconTextSize: Int = 0
    var mTextLengthBefore: Int = 0

    lateinit var mCurrentSpanStarts: MutableList<Int>
    lateinit var mCurrentSpanEnds: MutableList<Int>

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

        this.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                //get the current length of the text before its changes so we can see if the user was deleting text
                mTextLengthBefore = text.length

                //get current spans and their locations. This will then help on deletion of span
                val currentSpans = text.getSpans(0, text.length, DynamicValueDrawableSpan::class.java)

                mCurrentSpanStarts = mutableListOf()
                mCurrentSpanEnds = mutableListOf()

                for (i in currentSpans.indices) {
                    mCurrentSpanStarts.add(text.getSpanStart(currentSpans[i]))
                    mCurrentSpanEnds.add(text.getSpanEnd(currentSpans[i]))
                }

            }


            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                if(displayDrawableForDynamicValue == true){
                    updateDynamicTextWithDrawable()

                }
            }

            override fun afterTextChanged(s: Editable) {

                //Detect if they were deleting text  if we are. delete the whole dynamic phrase we are deleting
                if( text.length < mTextLengthBefore){
                    var i = 0
                    while (i < mCurrentSpanStarts.size){

                        if (selectionEnd > mCurrentSpanStarts[i] && selectionEnd < mCurrentSpanEnds[i]){

                            val updatedText: CharSequence
                            try {
                                updatedText = text.removeRange(mCurrentSpanStarts[i], mCurrentSpanEnds[i] - 1)
                                setText(updatedText)
                            } catch(e: Exception) {
                            }

                            setSelection(text.length)
                        }
                        i++
                    }

                    mCurrentSpanStarts.clear()
                    mCurrentSpanEnds.clear()
                }
            }

        })

        array.recycle()
    }

    fun updateDynamicTextWithDrawable() {
        DynamicValueDrawableGenerator.addDynamicDrawables(context, text,
                mIconSize, mIconAlignment, mIconTextSize,
                5.0f, 35.0f)

    }


    fun setDisplayDynamicDrawable (displayDynamicDrawable : Boolean){
        this.displayDrawableForDynamicValue = displayDynamicDrawable
    }
}