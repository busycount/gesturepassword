package com.busycount.gesturepassword;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * GesturePasswordView
 * <p>
 * 2018/11/30 | Count.C | Created
 */
public class GesturePasswordView extends View {


    public GesturePasswordView(Context context) {
        this(context, null);
    }

    public GesturePasswordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GesturePasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GesturePasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(attrs);
    }


    private Paint paintPoint;
    private Paint paintLine;
    private int num;
    private int validLength;
    private int pointRadius;
    private int pointFactor;
    private int pointArea;
    private int lineColor;
    private int lineColorWrong;
    private List<PasswordPoint> listSource = new ArrayList<>();
    private List<PasswordPoint> listSelect = new ArrayList<>();
    private List<PasswordPoint> listUnSelect = new LinkedList<>();
    private boolean isSetFinish;
    private float floatX;
    private float floatY;
    private boolean isVerify;
    private boolean isWrong;
    private int autoClearDelay;
    private int jointPointSize;
    private String jointPointStr;
    private IOnGesturePasswordListener onGesturePasswordListener;

    private void initAttr(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.GesturePasswordView);
        int pointColor = ta.getColor(R.styleable.GesturePasswordView_pointColor, Color.BLACK);
        pointRadius = ta.getDimensionPixelOffset(R.styleable.GesturePasswordView_pointRadius, 0);
        pointFactor = ta.getInt(R.styleable.GesturePasswordView_pointFactor, 2);
        pointArea = pointRadius * pointFactor;
        int lineWidth = ta.getDimensionPixelOffset(R.styleable.GesturePasswordView_lineWidth, 0);
        lineColor = ta.getColor(R.styleable.GesturePasswordView_lineColor, Color.GRAY);
        lineColorWrong = ta.getColor(R.styleable.GesturePasswordView_lineColorWrong, Color.RED);
        num = ta.getInteger(R.styleable.GesturePasswordView_num, 3);
        validLength = ta.getInteger(R.styleable.GesturePasswordView_validLength, num + 1);
        autoClearDelay = ta.getInteger(R.styleable.GesturePasswordView_autoClearDelay, 1500);
        ta.recycle();

        paintPoint = new Paint();
        paintPoint.setColor(pointColor);
        paintLine = new Paint();
        paintLine.setStrokeWidth(lineWidth);

        initPoints();
    }

    private void initPoints() {
        PasswordPoint point;
        for (int y = 0; y < num; y++) {
            for (int x = 0; x < num; x++) {
                point = new PasswordPoint(x, y);
                listSource.add(point);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        int screenArea = Math.min(screenWidth, screenHeight);

        int width = getTargetSize(widthMeasureSpec, screenArea);
        int height = getTargetSize(heightMeasureSpec, screenArea);

        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        calcCell(size);
    }

    private int getTargetSize(int measureSpec, int defSize) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else if (mode == MeasureSpec.AT_MOST) {
            return Math.min(size, defSize);
        } else {
            return defSize;
        }
    }

    private void calcCell(int size) {
        int cellSize = size / num;
        int startY = cellSize / 2;
        int startX = cellSize / 2;
        for (PasswordPoint p : listSource) {
            p.coordinateX = startX + p.x * cellSize;
            p.coordinateY = startY + p.y * cellSize;
        }
        if (pointRadius == 0) {
            pointRadius = cellSize / 10;
            pointArea = pointRadius * pointFactor;
        }
        if (paintLine.getStrokeWidth() == 0) {
            paintLine.setStrokeWidth(pointRadius * 0.8f);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSelectLine(canvas);
        drawPoint(canvas);
    }

    private void drawPoint(Canvas canvas) {
        for (PasswordPoint point : listSource) {
            canvas.drawCircle(point.coordinateX, point.coordinateY, pointRadius, paintPoint);
        }
    }

    private void drawSelectLine(Canvas canvas) {
        if (listSelect.isEmpty()) {
            return;
        }
        paintLine.setColor(isWrong ? lineColorWrong : lineColor);
        PasswordPoint p1;
        PasswordPoint p2 = null;
        for (int i = 0, length = listSelect.size() - 1; i < length; i++) {
            p1 = listSelect.get(i);
            p2 = listSelect.get(i + 1);
            canvas.drawLine(p1.coordinateX, p1.coordinateY, p2.coordinateX, p2.coordinateY, paintLine);
        }

        if (!isSetFinish) {
            if (p2 == null) {
                p2 = listSelect.get(listSelect.size() - 1);
            }
            canvas.drawLine(p2.coordinateX, p2.coordinateY, floatX, floatY, paintLine);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                joinPointBefore();
                joinPoint(event, false);
                break;
            case MotionEvent.ACTION_MOVE:
                joinPoint(event, false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                joinPoint(event, true);
                break;
            default:
                break;
        }
        return true;
    }

    private void joinPointBefore() {
        if (isSetFinish) {
            return;
        }
        clearData();
        onDrawStart();
    }

    private void joinPoint(MotionEvent event, boolean isTouchFinish) {
        if (isSetFinish) {
            return;
        }
        floatX = event.getX();
        floatY = event.getY();
        for (PasswordPoint p : listUnSelect) {
            if (p.isInclude(floatX, floatY, pointArea)) {
                checkAdd(p);
                break;
            }
            if (p.x > floatX && p.y > floatY) {
                break;
            }
        }
        isSetFinish = listSelect.size() == listSource.size() || isTouchFinish;
        if (isSetFinish) {
            onDrawStop();
            generateAction();
        } else {
            invalidate();
        }
    }

    private void checkAdd(PasswordPoint p) {
        if (listSelect.isEmpty()) {
            listSelect.add(p);
            listUnSelect.remove(p);
            return;
        }
        PasswordPoint lastP = listSelect.get(listSelect.size() - 1);
        int dx = p.x - lastP.x;
        int dy = p.y - lastP.y;
        if (Math.abs(dx) >= 2 || Math.abs(dy) >= 2) {
            if (dx == 0) {
                int mY = dy > 0 ? 1 : -1;
                checkLine(lastP, 0, mY, Math.abs(dy));
            } else if (dy == 0) {
                int mX = dx > 0 ? 1 : -1;
                checkLine(lastP, mX, 0, Math.abs(dx));
            } else if (Math.abs(dx / dy) == 1) {
                int mX = dx > 0 ? 1 : -1;
                int mY = dy > 0 ? 1 : -1;
                checkLine(lastP, mX, mY, Math.abs(dx));
            } else {
                listSelect.add(p);
                listUnSelect.remove(p);
            }
        } else {
            listSelect.add(p);
            listUnSelect.remove(p);
        }
    }

    private void checkLine(PasswordPoint lastP, int mX, int mY, int length) {
        List<PasswordPoint> tempList = new ArrayList<>();
        PasswordPoint temp;
        boolean canAdd = true;
        for (int i = 1; i < length; i++) {
            temp = new PasswordPoint(lastP.x + mX * i, lastP.y + mY * i);
            if (listSelect.contains(temp)) {
                canAdd = false;
                break;
            } else {
                tempList.add(temp);
            }
        }
        if (canAdd) {
            for (PasswordPoint pIndex : tempList) {
                int index = listUnSelect.indexOf(pIndex);
                if (index != -1) {
                    listSelect.add(listUnSelect.get(index));
                    listUnSelect.remove(index);
                }
            }
        }
    }

    private void generateAction() {
        StringBuilder builder = new StringBuilder();
        for (PasswordPoint p : listSelect) {
            builder.append(p.num(num));
        }
        if (isVerify) {
            if (listSelect.size() < validLength) {
                onInvalid();
            } else {
                if (jointPointSize == listSelect.size() && getPointStr().equals(jointPointStr)) {
                    onVerifySuccess(jointPointStr);
                } else {
                    onVerifyFailed();
                }
            }
        } else {
            if (listSelect.size() < validLength) {
                onInvalid();
            } else {
                jointPointSize = listSelect.size();
                jointPointStr = getPointStr();
                onCreatedSuccess();
            }
        }
    }

    private String getPointStr() {
        StringBuilder builder = new StringBuilder();
        for (PasswordPoint p : listSelect) {
            builder.append(p.num(num));
        }
        return builder.toString();
    }

    private void onDrawStart() {
        if (onGesturePasswordListener != null) {
            onGesturePasswordListener.onDrawStart();
        }
    }

    private void onDrawStop() {
        if (onGesturePasswordListener != null) {
            onGesturePasswordListener.onDrawStop();
        }
    }

    private void readPassword() {
        jointPointStr = onGesturePasswordListener != null ? onGesturePasswordListener.readPassword() : null;
        isVerify = !TextUtils.isEmpty(jointPointStr);
    }

    private void onVerifySuccess(String str) {
        drawSuccess();
        if (onGesturePasswordListener != null) {
            onGesturePasswordListener.onVerifySuccess(str);
        }
    }

    private void onCreatedSuccess() {
        drawSuccess();
        if (onGesturePasswordListener != null) {
            onGesturePasswordListener.onCreateSuccess();
        }
    }

    private void onInvalid() {
        drawError();
        if (onGesturePasswordListener != null) {
            onGesturePasswordListener.onInvalid();
        }
    }

    private void onVerifyFailed() {
        drawError();
        if (onGesturePasswordListener != null) {
            onGesturePasswordListener.onVerifyFailed();
        }
    }

    private void drawSuccess() {
        invalidate();
    }

    private void drawError() {
        isWrong = true;
        invalidate();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                clear();
            }
        }, autoClearDelay);
    }

    private void clear() {
        isWrong = false;
        isSetFinish = false;
        clearData();
        invalidate();
    }

    private void clearData() {
        listUnSelect.clear();
        listUnSelect.addAll(listSource);
        listSelect.clear();
    }


    public void redraw(boolean isVerifyMode) {
        isVerify = isVerifyMode;
        clear();
    }

    public boolean isVerify() {
        return isVerify;
    }

    public void setOnGesturePasswordListener(@NonNull IOnGesturePasswordListener onGesturePasswordListener) {
        this.onGesturePasswordListener = onGesturePasswordListener;
        readPassword();
    }
}
