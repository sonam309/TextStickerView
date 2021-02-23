package com.textsticker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.textstickerlib.StickerTextView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout parent_fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent_fl=findViewById(R.id.parent_fl);


        // add a stickerText to canvas
        StickerTextView tv_sticker = new StickerTextView(this);
        tv_sticker.setText("hello everyOne");
        parent_fl.addView(tv_sticker);

    }
}