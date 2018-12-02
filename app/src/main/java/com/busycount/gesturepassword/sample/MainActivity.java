package com.busycount.gesturepassword.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.busycount.gesturepassword.OnGesturePasswordAction;
import com.busycount.gesturepassword.GesturePasswordView;

public class MainActivity extends AppCompatActivity {

    private GesturePasswordView gesturePasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gesturePasswordView = findViewById(R.id.passwordView);
        gesturePasswordView.setOnGesturePasswordAction(new OnGesturePasswordAction() {
            @Override
            public String readPassword() {
                return null;
            }

            @Override
            public void onCreateSuccess() {
                Toast.makeText(MainActivity.this, "onCreateSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerifyFailed() {
                Toast.makeText(MainActivity.this, "onVerifyFailed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerifySuccess(String str) {
                Toast.makeText(MainActivity.this, "onVerifySuccess " + str, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onInvalid() {
                Toast.makeText(MainActivity.this, "onInvalid", Toast.LENGTH_SHORT).show();
            }

        });
    }


    public void redraw(View view) {
        gesturePasswordView.redraw(false);
    }

    public void sure(View view) {
        gesturePasswordView.redraw(true);
    }
}
