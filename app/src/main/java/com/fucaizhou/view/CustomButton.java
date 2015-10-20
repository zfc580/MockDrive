package com.fucaizhou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.fucaizhou.mockdrive.MyWindowManager;

/**
 * Created by fucai.zhou on 2015/10/13.
 */
public class CustomButton extends Button {

    private GestureDetector mDetector;
    private boolean isSingleTapUp = false;
    private MyWindowManager.IGestureListener mListener;

    public CustomButton(Context context) {
        super(context);
        init(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){

        mDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
                mListener.onShowPress(CustomButton.this, e);
                isSingleTapUp = false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                mListener.onSingleTap(CustomButton.this, e);
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                isSingleTapUp = true;
                return super.onSingleTapUp(e);
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        if(!isSingleTapUp){
                            mListener.onPressUp(v,event);
                        }
                        break;
                }
                return mDetector.onTouchEvent(event);
            }
        });
    }

    public void setGestureListener(MyWindowManager.IGestureListener listener){
        mListener = listener;
    }
}
