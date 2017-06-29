package com.madreain.androiddream.ui.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.madreain.androiddream.R;

public class AboutActivity extends AppCompatActivity {

    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initListener();
    }

    private void initView() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_return = (ImageView) findViewById(R.id.img_return);
        img_add = (ImageView) findViewById(R.id.img_add);
        txt_title.setText(getResources().getString(R.string.txt_about));
        img_add.setVisibility(View.GONE);
    }

    private void initListener() {
        img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
