package io.magics.circularviewmagics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

/**
 * Circular ImageView with circular padding and border.
 * Created by Erik on 24.01.2018.
 */

public class CircularViewMagics extends android.support.v7.widget.AppCompatImageView
        implements View.OnClickListener{

    private RectF mViewBounds = new RectF();
    private RectF mInnerShadowBounds = new RectF();

    private Paint mShadowPaint;
    private Paint mViewPaint;
    private Paint mOuterRingPaint;
    private Paint mOuterRingShadowPaint;
    private boolean mCustomCorners = false;

    private float mRoundedAllCorners = 0.0f;
    private float mRoundedTopLeft = 0.0f;
    private float mRoundedTopRight = 0.0f;
    private float mRoundedBottomLeft = 0.0f;
    private float mRoundedBottomRight = 0.0f;

    private boolean mInnerShadow = false;

    private boolean mOuterRing = false;

    private float mOuterRingStroke = 0.0f;
    private int mOuterRingColor;
    private float mOuterRingAlpha = 1.0f;

    private boolean mOuterRingGradient = false;
    private int mGradientColorFrom;
    private int mGradientColorTo;

    private int mViewColor;
    private int mViewHeight;
    private int mViewWidth;

    private boolean animState = false;
    private Animation mAnimIn;
    private Animation mAnimOut;

    @Override
    public void onClick(View v) {
        if (animState){
            mAnimOut.start();
        } else {
            mAnimIn.start();
        }
    }

    public CircularViewMagics(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularViewMagics, 0, 0);

        try {
            mCustomCorners = a.getBoolean(R.styleable.CircularViewMagics_roundedCorners, false);
            mRoundedAllCorners = a.getFloat(R.styleable.CircularViewMagics_radiusAllCorners, 1.0f);
            mRoundedTopLeft = a.getFloat(R.styleable.CircularViewMagics_radiusTopLeft, 1.0f);
            mRoundedTopRight = a.getFloat(R.styleable.CircularViewMagics_radiusTopRight, 1.0f);
            mRoundedBottomLeft = a.getFloat(R.styleable.CircularViewMagics_radiusBottomLeft, 1.0f);
            mRoundedBottomRight = a.getFloat(R.styleable.CircularViewMagics_radiusBottomRight, 1.0f);

            mInnerShadow = a.getBoolean(R.styleable.CircularViewMagics_innerShadow, false);

            mOuterRing = a.getBoolean(R.styleable.CircularViewMagics_outerRing, false);

            mOuterRingStroke = a.getDimension(R.styleable.CircularViewMagics_outerRingStroke, 0.0f);
            mOuterRingColor = a.getColor(R.styleable.CircularViewMagics_outerRingColor, 0xffffffff);
            mOuterRingAlpha = a.getFloat(R.styleable.CircularViewMagics_outerRingAlpha, 1.0f);
        }
        finally {
            a.recycle();
        }
        this.setOnClickListener(this);
        init();
    }

    public boolean isCustomCornerRadius(){
        return mCustomCorners;
    }

    public void setIsCustomCornerRadius(Boolean custom){
        mCustomCorners = custom;
        invalidate();
    }

    public void setAllCorners(float radius){
        mRoundedAllCorners = radius;
        invalidate();
    }

    public void setAllCorners(float topLeft, float topRight, float bottomLeft, float bottomRight){
        mRoundedTopLeft = topLeft;
        mRoundedTopRight = topRight;
        mRoundedBottomLeft = bottomLeft;
        mRoundedBottomRight = bottomRight;
        invalidate();
    }

    public float getTopLeftRadius(){
        return mRoundedTopLeft;
    }

    public float getTopRightRadius(){
        return mRoundedTopRight;
    }

    public float getBottomLeftRadius(){
        return mRoundedBottomLeft;
    }

    public float getBottomRightRadius(){
        return mRoundedBottomRight;
    }

    public void setHasInnerShadow(Boolean isShadowed){
        mInnerShadow = isShadowed;
        invalidate();
    }

    public boolean isOuterRingVisible(){
        return mOuterRing;
    }

    public void setShowOuterRing(Boolean show){
        mOuterRing = show;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCustomCorners) {
            canvas.drawRoundRect(mViewBounds, 30, 30, mViewPaint);
        } else {
            canvas.drawOval(mViewBounds, mViewPaint);
        }
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mOuterRingStroke;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) mOuterRingStroke;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minW, MeasureSpec.getSize(widthMeasureSpec));

        int minH;
        if (mOuterRing){
            minH = (w - (int) mOuterRingStroke) + getPaddingBottom() + getPaddingTop();
        } else {
            minH = w + getPaddingBottom() + getPaddingTop();
        }

        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minH);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xPadding = (float) (getPaddingLeft() + getPaddingRight());
        float yPadding = (float) (getPaddingTop() + getPaddingBottom());
        if(mOuterRing){
            yPadding += mOuterRingStroke;
            xPadding += mOuterRingStroke;
        }
        float ww = (float) w - xPadding;
        float hh = (float) h - yPadding;

        if (!mCustomCorners){

            float diameter = Math.min(ww, hh);
            mViewBounds = new RectF(0.0f, 0.0f, diameter, diameter);
            mViewBounds.offsetTo(getPaddingLeft(), getPaddingTop());

        } else {
            mViewBounds = new RectF(xPadding / 2, yPadding / 2, xPadding / 2, yPadding / 2);
            mViewBounds.offsetTo(getPaddingLeft(), getPaddingTop());
        }

        if (mInnerShadow){
            mInnerShadowBounds = new RectF(
                    mViewBounds.left + 10,
                    mViewBounds.bottom + 10,
                    mViewBounds.right - 10,
                    mViewBounds.bottom + 20
            );
        }
    }

    private void init() {

        mShadowPaint = new Paint(Paint.HINTING_OFF);
        mShadowPaint.setColor(0xff101010);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        mViewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mViewPaint.setStyle(Paint.Style.FILL);

        mOuterRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterRingPaint.setStyle(Paint.Style.FILL);
        mOuterRingPaint.setColor(mOuterRingColor);
        int paintAlpha = Math.round(mOuterRingAlpha * 255);
        mOuterRingPaint.setAlpha(Float.floatToIntBits(paintAlpha));

        mOuterRingShadowPaint = new Paint(Paint.HINTING_OFF);
        mOuterRingShadowPaint.setColor(0xff101010);
        mOuterRingShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

    }
}
