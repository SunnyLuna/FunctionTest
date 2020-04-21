package com.decard.zj.founctiontest.network;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.decard.zj.founctiontest.R;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    private Button btn_test;
    private TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initViews();
    }

    private void initViews() {
        btn_test = findViewById(R.id.btn_test);
        tv_test = findViewById(R.id.tv_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitUtil.INSTANCE.getdc().getDc("{\"func\": \"dc_stopAliPay\",\"in\": [\"100\"]}").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        tv_test.setText(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        tv_test.setText(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
    }
}
