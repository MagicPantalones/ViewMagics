package io.magics.textviewmagics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Just a simple library that will add an extra space to the end of a TextView
 * Created by Erik on 01.02.2018.
 */

public class TextViewMagics extends android.support.v7.widget.AppCompatTextView {
    public TextViewMagics(Context context) {
        super(context);
    }

    public TextViewMagics(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewMagics(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        setTextColor(getPaint().getColor());

        Layout layout = getLayout();
        String text = getText().toString();

        for(int i = 0; i < layout.getLineCount(); i++){
            final int start = layout.getLineStart(i);
            final int end = layout.getLineEnd(i);

            String line = text.substring(start, end);

            final float left = layout.getLineLeft(i);
            final int baseLine = layout.getLineBaseline(i);

            canvas.drawText(line, left + getPaddingLeft(), baseLine + getPaddingTop(), getPaint());


        }

        super.onDraw(canvas);
    }
}
