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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import java.lang.ref.WeakReference


class DynamicValueDrawableSpan(verticalAlignment: Int) : DynamicDrawableSpan(verticalAlignment) {


    private var mContext: Context? = null
    private var mResourceId: Int = 0
    private var mSize: Int = 0
    private var mTextSize: Int = 0
    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var mTop: Int = 0
    lateinit private var mDrawable: Drawable
    private var mDrawableRef: WeakReference<Drawable>? = null
    lateinit private var mText: String


    constructor(context: Context, resourceId: Int, size: Int, alignment: Int, textSize: Int, text: String):  this(alignment) {

        mContext = context
        mResourceId = resourceId
        mWidth; mHeight; mSize = size
        mTextSize = textSize
        mText = text
    }


    override fun getDrawable(): Drawable {
        try {
            //mDrawable = mContext?.resources?.getDrawable(mResourceId) as Drawable
            mDrawable = TextDrawableCustom(mText, mSize) as Drawable
            mHeight = mSize

            val paint = Paint()
            paint.textSize = (mTextSize * .80).toFloat()
            mWidth = paint.measureText(mText + "    ").toInt()

            mTop = (mTextSize - mHeight) / 2
            mDrawable.setBounds(0, mTop, mWidth, mTop + mHeight)
        } catch (e: Exception) {
            // swallow
        }


        return mDrawable
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        //super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        val b = getCachedDrawable()
        canvas.save()

        var transY = bottom - b.bounds.bottom
        if (mVerticalAlignment === ALIGN_BASELINE) {
            transY = top + (bottom - top) / 2 - (b.bounds.bottom - b.bounds.top) / 2 - mTop
        }

        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()
    }

    private fun getCachedDrawable(): Drawable {
        if (mDrawableRef == null || mDrawableRef!!.get() == null) {
            mDrawableRef = WeakReference<Drawable>(drawable)
        }
        return mDrawableRef!!.get()
    }


}