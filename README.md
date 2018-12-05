# gesturepassword
&lt;Android> GesturePasswordView

## Use

1. In your xml

```xml
    <com.busycount.gesturepassword.GesturePasswordView
        android:id="@+id/passwordView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@color/colorWhite"
        app:autoClearDelay="1500"
        app:lineColor="@color/colorPrimary"
        app:lineColorWrong="@color/colorAccent"
        app:lineWidth="10dp"
        app:num="4"
        app:pointColor="@color/colorPrimaryDark"
        app:pointFactor="3"
        app:pointRadius="10dp"
        app:validLength="6" />
```

2. In your Activity

```java
protected void onCreate(Bundle savedInstanceState) {
    gesturePasswordView = findViewById(R.id.passwordView);
    //you can also use OnGesturePasswordListener to implement some method
    gesturePasswordView.setOnGesturePasswordListener(new IOnGesturePasswordListener() {
        
        @Override
        public void onDrawing() {
             Toast.makeText(MainActivity.this, "startDraw", Toast.LENGTH_SHORT).show();
        }

        @Override
        public String readPassword() {
            //if not null,start verify
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

//reset
gesturePasswordView.redraw(false);
//start verify
gesturePasswordView.redraw(true);
```



## Implementation
Step 1. Add the JitPack repository to your build file Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency, [Lastest release](https://github.com/busycount/gesturepassword/releases)
```
dependencies {
    implementation 'com.github.busycount:gesturepassword:lastest'
}
```
