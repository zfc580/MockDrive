package com.fucaizhou.mockdrive;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fucaizhou.view.CustomButton;
import com.fucaizhou.view.FloatCrtlView;

/**
 * Created by fucai.zhou on 2015/10/8.
 */
public class MyWindowManager {

    private final static String TAG = "MyWindowManager";
    private static final String[] gears={"1档","2档","3档"};

    public static int current_Gear;

    private Context mContext;

    private static MyWindowManager instance;

    private FloatCrtlView floatView;

    private LayoutParams floatParams;

    private WindowManager mWindowManger;

    public CustomButton leftBtn,rightBtn,topBtn;
    public ImageView middleBtn;
    public Button mGearBtn;
    private SharedPreferences mSharedPreference;

    private MyWindowManager(Context context){
        mContext = context;
        mSharedPreference = mContext.getSharedPreferences("MockDrive",Context.MODE_PRIVATE);
        floatView = new FloatCrtlView(mContext);
        leftBtn = floatView.getLeftBtn();
        rightBtn = floatView.getRightBtn();
        topBtn = floatView.getTopBtn();
        middleBtn = floatView.getMiddleBtn();

        mGearBtn = floatView.getGearBtn();
        mGearBtn.setText(gears[mSharedPreference.getInt("gear",0)]);
        mGearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gear = mSharedPreference.getInt("gear",0);
                current_Gear = (gear + 1) % (gears.length);
                mSharedPreference.edit().putInt("gear",current_Gear).apply();
                mGearBtn.setText(gears[current_Gear]);
            }
        });
    }

    public static MyWindowManager createInstance(Context context){
        if(instance == null){
            instance = new MyWindowManager(context.getApplicationContext());
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
                floatParams.gravity = Gravity.START | Gravity.TOP;
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
