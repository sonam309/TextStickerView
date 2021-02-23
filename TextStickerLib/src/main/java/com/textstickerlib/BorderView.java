package com.textstickerlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

public class BorderView extends View {


    public BorderView(Context context) {
        super(context);
    }

    public BorderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.getLayoutParams();

        /**
         *  Rect four integer coordinates for a rectangle.
         */

        Rect border = new Rect();

        /**
         *  get margin from layout and set into Rect
         */

        border.left = this.getLeft() - params.leftMargin;
        border.right = this.getRight() - params.rightMargin;
        border.top = this.getTop() - params.topMargin;
        border.bottom = this.getBottom() - params.bottomMargin;


        /**
         * The Paint class holds the style and color information about how to draw geometries, text and bitmaps.
         */

        Paint borderPaint = new Paint();

        borderPaint.setStrokeWidth(5);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(border, borderPaint);

    }
}
