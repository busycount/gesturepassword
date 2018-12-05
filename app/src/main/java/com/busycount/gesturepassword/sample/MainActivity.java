package com.busycount.gesturepassword.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.busycount.gesturepassword.GesturePasswordView;
import com.busycount.gesturepassword.IOnGesturePasswordListener;

public class MainActivity extends AppCompatActivity {

    private GesturePasswordView gesturePasswordView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView.setText("绘制手势图案,请至少连接4个点");
        gesturePasswordView = findViewById(R.id.passwordView);
        gesturePasswordView.setOnGesturePasswordListener(new IOnGesturePasswordListener() {

            @Override
            public void onDrawStart() {
                textView.setText("完成后松开手指");
            }

            @Override
            public void onDrawStop() {

            }

            @Override
            public String readPassword() {
                return "0-1-2-4";
            }

            @Override
            public void onCreateSuccess() {
                textView.setText("再次绘制手势图案进行确认");
            }

            @Override
            public void onVerifyFailed() {
                textView.setText("输入错误请重试");
            }

            @Override
            public void onVerifySuccess(String str) {
                Toast.makeText(MainActivity.this, "onVerifySuccess " + str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInvalid() {
                textView.setText("绘制手势图案,请至少连接4个点");
            }

        });
    }


    public void redraw(View view) {
        gesturePasswordView.redraw(false);
        textView.setText("绘制手势图案,请至少连接4个点");
    }

    public void sure(View view) {
        if (gesturePasswordView.isVerify()) {
            Toast.makeText(this, "Create success", Toast.LENGTH_SHORT).show();
        } else {
            gesturePasswordView.redraw(true);
        }

    }
}
