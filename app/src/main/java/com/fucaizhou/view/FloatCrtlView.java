package com.fucaizhou.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
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

    private CustomButton topBtn,leftBtn,rightBtn;
    private ImageView middleBtn;
    private Button mGearBtn;

    public Button getGearBtn(){
        return mGearBtn;
    }

    public CustomButton getTopBtn() {
        return topBtn;
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

    public FloatCrtlView(Context context) {
        super(context);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_panel, this);
        topBtn = (CustomButton)findViewById(R.id.top);
        leftBtn = (CustomButton)findViewById(R.id.left);
        rightBtn = (CustomButton)findViewById(R.id.right);
        middleBtn = (ImageView) findViewById(R.id.middle);
        mGearBtn = (Button) findViewById(R.id.gear);
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
