package com.textstickerlib;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StickerTextView extends StickerView {

    private AutoResizeTextView textView;


    public StickerTextView(@NonNull Context context) {
        super(context);

    }

    public StickerTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected View getMainView() {

        if (textView != null) {
            return textView;
        }

        textView = new AutoResizeTextView(getContext());
        textView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.BLACK);
        textView.setSingleLine(false);
        textView.setTextSize(5000);
        textView.setMinTextSize(10);


        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);





        return textView;
    }


    public void setText(String text) {
        if (textView != null)
            textView.setText(text);
    }

    public void setTextcolor(int color){

        if(textView!=null){

            textView.setTextColor(color);
        }

    }
    public String getText() {
        if (textView != null)
            return textView.getText().toString();

        return null;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }
}
