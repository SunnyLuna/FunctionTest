package com.decard.zj.founctiontest.widgets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.decard.zj.founctiontest.R;
import com.example.commonlibs.widgets.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;

public class SpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        NiceSpinner niceSpinner = findViewById(R.id.nice_spinner);
        ArrayList<String> dataset = new ArrayList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        niceSpinner.attachDataSource(dataset);
    }
}
