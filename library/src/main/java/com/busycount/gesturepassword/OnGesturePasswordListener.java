package com.busycount.gesturepassword;

/**
 * OnGesturePasswordListener
 * <p>
 * 2018/12/5 | Count.C | Created
 */
public abstract class OnGesturePasswordListener implements IOnGesturePasswordListener {
    @Override
    public void onDrawing() {

    }

    @Override
    public String readPassword() {
        return null;
    }

    @Override
    public void onCreateSuccess() {

    }

    @Override
    public void onInvalid() {

    }

    @Override
    public void onVerifySuccess(String password) {

    }

    @Override
    public void onVerifyFailed() {

    }
}
