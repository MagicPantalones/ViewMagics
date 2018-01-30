package io.magics.circularviewmagics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Circular ImageView with circular padding and border.
 * Created by Erik on 24.01.2018.
 */

public class CircularViewMagics extends android.support.v7.widget.AppCompatImageView
        implements View.OnClickListener{

    private Path clipPath;

    private int srcDrawableInt;
    private Drawable srcDrawable;

    private Paint clipPaint;
    private Paint outerRingPaint;

    private String wantedShape;
    private int shape;
    private float topCornerRadius;
    private float bottomCornerRadius;
    private boolean outerCircleEnabled;
    private int outerCircleColor;
    private float outerCircleStroke;

    private RectF viewBounds;
    private RectF outerCircleRect;

    private static final int VIEW_CIRCLE = 420;
    private static final int VIEW_RECTANGLE = 421;

    public CircularViewMagics(Context context){
        super(context);
    }

    public CircularViewMagics(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        init();
    }

    public CircularViewMagics(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attrs){
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircularViewMagics, 0, 0);

        srcDrawableInt = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", -1);
        try {
            wantedShape = a.getString(R.styleable.CircularViewMagics_viewShape);
            outerCircleEnabled = a.getBoolean(R.styleable.CircularViewMagics_outerRingEnabled, false);
            outerCircleColor = a.getColor(R.styleable.CircularViewMagics_outerRingColor, getContext().getResources().getColor(android.R.color.white));
            outerCircleStroke = a.getDimension(R.styleable.CircularViewMagics_outerRingStroke, 1.0f);
            topCornerRadius = a.getDimension(R.styleable.CircularViewMagics_topCornersRadius, 50.0f);
            bottomCornerRadius = a.getDimension(R.styleable.CircularViewMagics_bottomCornersRadius, 50.0f);
        }
        finally {
            a.recycle();
        }
    }

    @Override
    public void onClick(View v) {

    }

    protected void init(){

        outerCircleRect = new RectF();
        viewBounds = new RectF();
        clipPath = new Path();

        if (wantedShape == null){
            shape = VIEW_CIRCLE;
        } else if (wantedShape.equals("420")){
            shape = VIEW_CIRCLE;
        } else if (wantedShape.equals("421")){
            shape = VIEW_RECTANGLE;
        }

        clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerRingPaint.setColor(outerCircleColor);

        setDefaultVals();

    }

    private void setDefaultVals(){

        if (srcDrawableInt != -1){
            srcDrawable = getContext().getDrawable(srcDrawableInt);
            setImageDrawable(srcDrawable);
            invalidate();
        } else {
            setDrawable();
            setImageDrawable(srcDrawable);
            invalidate();
        }
    }

    private void setDrawable(){
        srcDrawable = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                float centerX = canvas.getWidth() / 2;
                float centerY = canvas.getHeight() / 2;

                if (shape == VIEW_RECTANGLE) {
                    canvas.drawRoundRect(viewBounds, topCornerRadius, bottomCornerRadius, outerRingPaint);
                } else {
                    canvas.drawCircle(centerX, centerY, canvas.getHeight(), outerRingPaint);
                }
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (outerCircleEnabled) {
            outerCircleRect.set(0, 0, screenWidth, screenHeight);
            viewBounds.set(outerCircleStroke, outerCircleStroke, screenWidth - outerCircleStroke, screenHeight - outerCircleStroke);
        } else
            viewBounds.set(0, 0, screenWidth,screenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (shape == VIEW_RECTANGLE) {
            if (outerCircleEnabled) {
                canvas.drawRoundRect(outerCircleRect, topCornerRadius, bottomCornerRadius, outerRingPaint);
            }
            canvas.drawRoundRect(viewBounds, topCornerRadius, bottomCornerRadius, clipPaint);
            clipPath.addRoundRect(viewBounds, topCornerRadius, bottomCornerRadius, Path.Direction.CW);
        } else {
            if (outerCircleEnabled) {
                canvas.drawCircle(outerCircleRect.centerX(), outerCircleRect.centerY(), (outerCircleRect.height() / 2), outerRingPaint);
            }
            canvas.drawCircle(viewBounds.centerX(), viewBounds.centerY(), (viewBounds.height() / 2), clipPaint);
            clipPath.addCircle(viewBounds.centerX(), viewBounds.centerY(), (viewBounds.height() / 2), Path.Direction.CW);
        }
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
