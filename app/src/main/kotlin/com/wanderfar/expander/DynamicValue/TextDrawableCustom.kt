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

import android.graphics.*
import android.graphics.drawable.Drawable


class TextDrawableCustom(private val text: String, textSize: Int, xPos : Float, yPos : Float) : Drawable() {
    private val textPaint: Paint
    private val backgroundPaint: Paint
    private var textWidth: Float = 0.0f
    private var textSize: Float = 0.0f
    private var xPos: Float = xPos
    private var yPos: Float = yPos

    init {

        this.textPaint = Paint()
        this.backgroundPaint = Paint()

        //Text Paint
        textPaint.color = Color.parseColor("#2196F3")

        textPaint.textSize = (textSize * .80).toFloat()
        textPaint.isAntiAlias = true
        textPaint.isFakeBoldText = true
        textPaint.style = Paint.Style.FILL_AND_STROKE

        this.textWidth = textPaint.measureText(text) / 2
        this.textSize = textPaint.textSize

        //Text Background Paint
        //backgroundPaint.color = Color.RED
        //backgroundPaint.style = Paint.Style.FILL

    }

    override fun draw(canvas: Canvas) {
        val r = Rect()

        textPaint.getTextBounds(text, 0, text.length, r)
        //val xPos = (canvas.width / 2)
        val yPos = Math.abs(r.height()) / 2

        //canvas.drawRect(5f, 40f - this.textSize, 5f + this.textWidth, 40f, backgroundPaint)
        //canvas.drawText(text, 5.toFloat(), 35.toFloat(), textPaint)
        canvas.drawText(text, this.xPos, this.yPos, textPaint)

    }

    override fun setAlpha(alpha: Int) {
        textPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter) {
        textPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

}