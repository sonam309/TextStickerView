package com.textsticker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.textstickerlib.StickerTextView;
import com.textstickerlib.StickerView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout parent_fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent_fl = findViewById(R.id.parent_fl);


        // add a stickerText to canvas
        StickerTextView tv_sticker = new StickerTextView(this);
        tv_sticker.setText("StickerView is a android UI library that make you able to use Single hand gesture to rotate, scale and flip the ‘sticker’.");
        tv_sticker.setTextcolor(getResources().getColor(R.color.design_default_color_error));
        tv_sticker.setTextGravity(Gravity.CENTER);

        parent_fl.addView(tv_sticker);

        tv_sticker.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded() {

                Toast.makeText(MainActivity.this, "edit", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStickerClicked() {

                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();

            }
        });

        parent_fl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tv_sticker.setControlsVisibility(false);

                return true;
            }
        });

    }
}