package com.refreshDemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_lv;
    private Button btn_rv;
    private Button btn_sv;
    private Button btn_wb;
    private Button btn_lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_lv=this.findViewById(R.id.btn_lv);
        btn_rv=this.findViewById(R.id.btn_rv);
        btn_sv=this.findViewById(R.id.btn_sv);
        btn_wb=this.findViewById(R.id.btn_wb);
        btn_lottie=this.findViewById(R.id.btn_lottie);
        btn_lv.setOnClickListener(this);
        btn_rv.setOnClickListener(this);
        btn_sv.setOnClickListener(this);
        btn_wb.setOnClickListener(this);
        btn_lottie.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_lv:
                startActivity(new Intent(MainActivity.this,ListViewActivity.class));
                break;
            case R.id.btn_rv:
                startActivity(new Intent(MainActivity.this,RecyclerViewActivity.class));
                break;
            case R.id.btn_sv:
                startActivity(new Intent(MainActivity.this,ScrollerViewActivity.class));
                break;
            case R.id.btn_wb:
                startActivity(new Intent(MainActivity.this,WebViewActivity.class));
                break;
            case R.id.btn_lottie:
                startActivity(new Intent(MainActivity.this,LottieActivity.class));
        }
    }
}
