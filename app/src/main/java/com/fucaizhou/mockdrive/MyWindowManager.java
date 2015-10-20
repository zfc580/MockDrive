package com.fucaizhou.mockdrive;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.fucaizhou.view.CustomButton;
import com.fucaizhou.view.FloatCrtlView;

/**
 * Created by fucai.zhou on 2015/10/8.
 */
public class MyWindowManager {

    private Context mContext;

    private static MyWindowManager instance;

    private FloatCrtlView floatView;

    private LayoutParams floatParams;

    private WindowManager mWindowManger;

    public CustomButton leftBtn,rightBtn,topBtn,bottomBtn;
    public CustomButton lefttopBtn,righttopBtn,leftbottomBtn,rightbottomBtn;
    public ImageView middleBtn;

    private MyWindowManager(Context context){
        mContext = context;
        floatView = new FloatCrtlView(mContext);
        leftBtn = floatView.getLeftBtn();
        rightBtn = floatView.getRightBtn();
        topBtn = floatView.getTopBtn();
        bottomBtn = floatView.getBottomBtn();
        middleBtn = floatView.getMiddleBtn();
        lefttopBtn = floatView.getLefttopBtn();
        righttopBtn = floatView.getRighttopBtn();
        leftbottomBtn = floatView.getLeftbottomBtn();
        rightbottomBtn = floatView.getRightbottomBtn();
    }

    public static MyWindowManager createInstance(Context context){
        if(instance == null){
            instance = new MyWindowManager(context);
        }
        return instance;
    }

    public void addFloatView(){
        mWindowManger = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = mWindowManger.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManger.getDefaultDisplay().getHeight();
        if (floatView != null) {

            if (floatParams == null) {
                floatParams = new LayoutParams();
                floatParams.type = LayoutParams.TYPE_PHONE;
                floatParams.format = PixelFormat.RGBA_8888;
                floatParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                floatParams.gravity = Gravity.LEFT | Gravity.TOP;
                floatParams.width = 500;
                floatParams.height = 500;
                floatParams.x = screenWidth;
                floatParams.y = screenHeight / 2;
            }
            floatView.setParams(floatParams);
            try {
                mWindowManger.addView(floatView, floatParams);
            }catch (IllegalStateException e){
                Toast.makeText(mContext, "不可重复添加悬浮框",Toast.LENGTH_LONG).show();
            }

        }
    }

    public void setBtnGestureListener(IGestureListener listener){
        leftBtn.setGestureListener(listener);
        rightBtn.setGestureListener(listener);
        topBtn.setGestureListener(listener);
        bottomBtn.setGestureListener(listener);
        lefttopBtn.setGestureListener(listener);
        righttopBtn.setGestureListener(listener);
        leftbottomBtn.setGestureListener(listener);
        rightbottomBtn.setGestureListener(listener);

    }



    public void removeFloatView(){
        if (mWindowManger != null) {
            mWindowManger.removeView(floatView);
            mWindowManger = null;
        }
    }

    public interface IGestureListener{

        public void onShowPress(View view, MotionEvent event);

        public void onPressUp(View view, MotionEvent event);

        public void onSingleTap(View view, MotionEvent event);
    }
}
