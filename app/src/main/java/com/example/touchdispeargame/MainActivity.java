package com.example.touchdispeargame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvCombo;

    private TouchView touchView;
    private View mask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        touchView = findViewById(R.id.touch_view);

        tvCombo = findViewById(R.id.combo);

        mask = findViewById(R.id.mask);

        touchView.setShowMaskListener(new TouchView.OnShowMaskListener() {
            @Override
            public void onShow(boolean isShow) {
                mask.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onShowCombo(String info) {

                tvCombo.setVisibility(info.equals("done") ? View.GONE : View.VISIBLE);

                tvCombo.setText(info);
            }
        });



    }

}