package com.busycount.gesturepassword;

/**
 * OnGesturePasswordAction
 * <p>
 * 2018/12/2 | Count.C | Created
 */
public interface OnGesturePasswordAction {

    String readPassword();

    void onCreateSuccess();

    void onVerifyFailed();

    void onVerifySuccess(String password);

    void onInvalid();
}
