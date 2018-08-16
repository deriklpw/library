package com.fih.idx.deriklibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fih.idx.deriklibrary.utils.click.MultiClickButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private MultiClickButton mBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.tv_click);

    }
}
