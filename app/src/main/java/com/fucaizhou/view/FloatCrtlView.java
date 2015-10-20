package com.fucaizhou.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fucaizhou.mockdrive.R;

import java.lang.reflect.Field;

/**
 * Created by fucai.zhou on 2015/10/8.
 */
public class FloatCrtlView extends LinearLayout {

    private WindowManager mWindowManager;

    private WindowManager.LayoutParams mParams;
    private GestureDetector mDetector;

    private float xInScreen;
    private float yInScreen;
    private float xDownInScreen;
    private float yDownInScreen;
    private float xInView;
    private float yInView;
    private int statusBarHeight;

    private CustomButton topBtn,bottomBtn,leftBtn,rightBtn;
    private CustomButton lefttopBtn,righttopBtn,leftbottomBtn,rightbottomBtn;
    private ImageView middleBtn;

    public CustomButton getTopBtn() {
        return topBtn;
    }

    public CustomButton getBottomBtn() {
        return bottomBtn;
    }

    public CustomButton getLeftBtn() {
        return leftBtn;
    }

    public CustomButton getRightBtn() {
        return rightBtn;
    }

    public ImageView getMiddleBtn() {
        return middleBtn;
    }

    public CustomButton getLefttopBtn() {
        return lefttopBtn;
    }

    public CustomButton getRighttopBtn() {
        return righttopBtn;
    }

    public CustomButton getLeftbottomBtn() {
        return leftbottomBtn;
    }

    public CustomButton getRightbottomBtn() {
        return rightbottomBtn;
    }

    public FloatCrtlView(Context context) {
        super(context);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_panel, this);
        topBtn = (CustomButton)findViewById(R.id.top);
        leftBtn = (CustomButton)findViewById(R.id.left);
        bottomBtn = (CustomButton)findViewById(R.id.bottom);
        rightBtn = (CustomButton)findViewById(R.id.right);
        middleBtn = (ImageView) findViewById(R.id.middle);
        lefttopBtn = (CustomButton) findViewById(R.id.left_top);
        righttopBtn = (CustomButton) findViewById(R.id.right_top);
        leftbottomBtn = (CustomButton) findViewById(R.id.left_bottom);
        rightbottomBtn = (CustomButton) findViewById(R.id.right_bottom);
    }

    public void setParams(WindowManager.LayoutParams params){
        mParams = params;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();

                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onTouchEvent(event);
    }


    /**
     *
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        mWindowManager.updateViewLayout(this, mParams);
    }

    /**
     *
     * @return
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
