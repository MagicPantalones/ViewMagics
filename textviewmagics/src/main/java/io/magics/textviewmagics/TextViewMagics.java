package io.magics.textviewmagics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * This view is supposed to add an extra space to each line ending.
 * Will revisit at a later time.
 * Created by Erik on 01.02.2018.
 */

public class TextViewMagics extends android.support.v7.widget.AppCompatTextView {
    Paint paint;
    int color;
    Typeface typeface;

    public TextViewMagics(Context context) {
        super(context);
        init();
    }

    public TextViewMagics(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewMagics(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = getPaint();
        color = paint.getColor();
        typeface = paint.getTypeface();


        paint.setColor(color);
        paint.setTypeface(typeface);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Layout layout = getLayout();
        String text = getText().toString();

        for(int i = 0; i < layout.getLineCount(); i++){
            final int start = layout.getLineStart(i);
            final int end = layout.getLineEnd(i);

            String line = text.substring(start, end) + "-!-";

            final float left = layout.getLineLeft(i);
            final int baseLine = layout.getLineBaseline(i);

            canvas.drawText(line,
                    left + getTotalPaddingLeft(),
                    baseLine + getTotalPaddingTop(),
                    paint);
        }
    }
}
