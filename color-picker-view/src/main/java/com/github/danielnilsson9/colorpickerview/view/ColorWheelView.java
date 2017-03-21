package com.github.danielnilsson9.colorpickerview.view;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.danielnilsson9.colorpickerview.R;


public class ColorWheelView extends View {

    private static final String STATE_PARENT = "parent";
    private static final String STATE_ANGLE = "angle";
    private static final String STATE_OLD_COLOR = "color";


    private static final int[] COLORS = new int[] { 0xFFFF0000, 0xFFFF00FF,
            0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };


    private Paint mColorWheelPaint;
    private Paint mPointerColor;

    private int mColorWheelThickness;
    private int mColorWheelRadius;
    private int mPreferredColorWheelRadius;

    private int mColorCenterRadius;
    private int mPreferredColorCenterRadius;

    private int mColorPointerRadius;
    private int mColorPointerHaloRadius;
    private RectF mColorWheelRectangle = new RectF();
    private RectF mCenterRectangle = new RectF();
    private boolean mUserIsMovingPointer = false;

    private int mCenterOldColor;
    private int mCenterNewColor;
    private float mTranslationOffset;
    private float mSlopX;
    private float mSlopY;
    private float mAngle;
    private Paint mCenterOldPaint;
    private Paint mCenterNewPaint;
    private Paint mCenterHaloPaint;
    private OnColorChangedListener onColorChangedListener;

    private int oldChangedListenerColor;



    public ColorWheelView(Context context) {
        super(context);
        init(null, 0);
    }

    public ColorWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ColorWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }



    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.onColorChangedListener = listener;
    }

    public OnColorChangedListener getOnColorChangedListener() {
        return this.onColorChangedListener;
    }




    private void init(AttributeSet attrs, int defStyle) {

        final Resources b = getContext().getResources();

        mColorWheelThickness = b.getDimensionPixelSize(R.dimen.colorpickerview__color_wheel_thickness);
        mColorWheelRadius = b.getDimensionPixelSize(R.dimen.colorpickerview__color_wheel_radius);
        mPreferredColorWheelRadius = mColorWheelRadius;
        mColorCenterRadius = b.getDimensionPixelSize(R.dimen.colorpickerview__color_center_radius);
        mPreferredColorCenterRadius = mColorCenterRadius;
        mColorPointerRadius = b.getDimensionPixelSize(R.dimen.colorpickerview__color_pointer_radius);
        mColorPointerHaloRadius = b.getDimensionPixelSize(R.dimen.colorpickerview__color_pointer_halo_radius);

        mAngle = (float) (-Math.PI / 2);

        Shader s = new SweepGradient(0, 0, COLORS, null);

        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setShader(s);
        mColorWheelPaint.setStyle(Paint.Style.STROKE);
        mColorWheelPaint.setStrokeWidth(mColorWheelThickness);



        mPointerColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerColor.setColor(calculateColor(mAngle));

        mCenterNewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterNewPaint.setColor(calculateColor(mAngle));
        mCenterNewPaint.setStyle(Paint.Style.FILL);

        mCenterOldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterOldPaint.setColor(calculateColor(mAngle));
        mCenterOldPaint.setStyle(Paint.Style.FILL);

        mCenterHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterHaloPaint.setColor(Color.BLACK);
        mCenterHaloPaint.setAlpha(0x00);

        mCenterNewColor = calculateColor(mAngle);
        mCenterOldColor = calculateColor(mAngle);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // All of our positions are using our internal coordinate system.
        // Instead of translating
        // them we let Canvas do the work for us.
        canvas.translate(mTranslationOffset, mTranslationOffset);

        // Draw the color wheel.
        canvas.drawOval(mColorWheelRectangle, mColorWheelPaint);

        float[] pointerPosition = calculatePointerPosition(mAngle);

        // Draw the pointer (the currently selected color) slightly smaller on
        // top.
        canvas.drawCircle(pointerPosition[0], pointerPosition[1],
                mColorPointerRadius, mPointerColor);

        // Draw the halo of the center colors.
        //canvas.drawCircle(0, 0, mColorCenterHaloRadius, mCenterHaloPaint);


        // Draw the old selected color in the center.
        canvas.drawArc(mCenterRectangle, 90, 180, true, mCenterOldPaint);

        // Draw the new selected color in the center.
        canvas.drawArc(mCenterRectangle, 270, 180, true, mCenterNewPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int intrinsicSize = 2 * (mPreferredColorWheelRadius + mColorPointerHaloRadius);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(intrinsicSize, widthSize);
        } else {
            width = intrinsicSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(intrinsicSize, heightSize);
        } else {
            height = intrinsicSize;
        }

        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mTranslationOffset = min * 0.5f;

        // fill the rectangle instances.
        mColorWheelRadius = min / 2 - mColorWheelThickness - mColorPointerHaloRadius;
        mColorWheelRectangle.set(-mColorWheelRadius, -mColorWheelRadius,
                mColorWheelRadius, mColorWheelRadius);

        mColorCenterRadius = (int) ((float) mPreferredColorCenterRadius * ((float) mColorWheelRadius / (float) mPreferredColorWheelRadius));
        mCenterRectangle.set(-mColorCenterRadius, -mColorCenterRadius,
                mColorCenterRadius, mColorCenterRadius);
    }

    private int ave(int s, int d, float p) {
        return s + Math.round(p * (d - s));
    }

    private int calculateColor(float angle) {
        float unit = (float) (angle / (2 * Math.PI));
        if (unit < 0) {
            unit += 1;
        }

        if (unit <= 0) {
            return COLORS[0];
        }
        if (unit >= 1) {
            return COLORS[COLORS.length - 1];
        }

        float p = unit * (COLORS.length - 1);
        int i = (int) p;
        p -= i;

        int c0 = COLORS[i];
        int c1 = COLORS[i + 1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        return Color.argb(a, r, g, b);
    }


    public int getColor() {
        return mCenterNewColor;
    }

    public void setColor(int color) {
        mAngle = colorToAngle(color);
        mPointerColor.setColor(calculateColor(mAngle));
        setNewCenterColor(color);
    }

    private float colorToAngle(int color) {
        float[] colors = new float[3];
        Color.colorToHSV(color, colors);

        return (float) Math.toRadians(-colors[0]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);

        // Convert coordinates to our internal coordinate system
        float x = event.getX() - mTranslationOffset;
        float y = event.getY() - mTranslationOffset;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Check whether the user pressed on the pointer.
                float[] pointerPosition = calculatePointerPosition(mAngle);
                if (x >= (pointerPosition[0] - mColorPointerHaloRadius)
                        && x <= (pointerPosition[0] + mColorPointerHaloRadius)
                        && y >= (pointerPosition[1] - mColorPointerHaloRadius)
                        && y <= (pointerPosition[1] + mColorPointerHaloRadius)) {
                    mSlopX = x - pointerPosition[0];
                    mSlopY = y - pointerPosition[1];
                    mUserIsMovingPointer = true;
                    invalidate();
                }
                // Check whether the user pressed on the center.
                else if (x >= -mColorCenterRadius && x <= mColorCenterRadius
                        && y >= -mColorCenterRadius && y <= mColorCenterRadius) {
                    mCenterHaloPaint.setAlpha(0x50);
                    setColor(getOldCenterColor());
                    invalidate();
                }
                // Check whether the user pressed anywhere on the wheel.
                else if (Math.sqrt(x*x + y*y)  <= mColorWheelRadius + mColorPointerHaloRadius
                        && Math.sqrt(x*x + y*y) >= mColorWheelRadius - mColorPointerHaloRadius
                        ) {
                    mUserIsMovingPointer = true;
                    invalidate();
                }
                // If user did not press pointer or center, report event not handled
                else{
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mUserIsMovingPointer) {
                    mAngle = (float) Math.atan2(y - mSlopY, x - mSlopX);
                    mPointerColor.setColor(calculateColor(mAngle));

                    setNewCenterColor(mCenterNewColor = calculateColor(mAngle));
                    invalidate();
                }
                // If user did not press pointer or center, report event not handled
                else{
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mUserIsMovingPointer = false;
                mCenterHaloPaint.setAlpha(0x00);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private float[] calculatePointerPosition(float angle) {
        float x = (float) (mColorWheelRadius * Math.cos(angle));
        float y = (float) (mColorWheelRadius * Math.sin(angle));

        return new float[] { x, y };
    }

    public void setNewCenterColor(int color) {
        mCenterNewColor = color;
        mCenterNewPaint.setColor(color);
        if (mCenterOldColor == 0) {
            mCenterOldColor = color;
            mCenterOldPaint.setColor(color);
        }
        if (onColorChangedListener != null && color != oldChangedListenerColor ) {
            onColorChangedListener.onColorChanged(color);
            oldChangedListenerColor  = color;
        }
        invalidate();
    }


    public void setOldCenterColor(int color) {
        mCenterOldColor = color;
        mCenterOldPaint.setColor(color);
        invalidate();
    }

    public int getOldCenterColor() {
        return mCenterOldColor;
    }




    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        Bundle state = new Bundle();
        state.putParcelable(STATE_PARENT, superState);
        state.putFloat(STATE_ANGLE, mAngle);
        state.putInt(STATE_OLD_COLOR, mCenterOldColor);


        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle savedState = (Bundle) state;

        Parcelable superState = savedState.getParcelable(STATE_PARENT);
        super.onRestoreInstanceState(superState);

        mAngle = savedState.getFloat(STATE_ANGLE);
        setOldCenterColor(savedState.getInt(STATE_OLD_COLOR));
        int currentColor = calculateColor(mAngle);
        mPointerColor.setColor(currentColor);
        setNewCenterColor(currentColor);
    }

}
