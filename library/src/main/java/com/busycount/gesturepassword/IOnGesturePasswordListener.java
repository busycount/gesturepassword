package com.busycount.gesturepassword;

/**
 * OnGesturePasswordAction
 * <p>
 * 2018/12/2 | Count.C | Created
 */
public interface IOnGesturePasswordListener {

    void onDrawing();

    String readPassword();

    void onCreateSuccess();

    void onInvalid();

    void onVerifySuccess(String password);

    void onVerifyFailed();

}
