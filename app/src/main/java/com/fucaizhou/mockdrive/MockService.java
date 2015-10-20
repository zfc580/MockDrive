package com.fucaizhou.mockdrive;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.fucaizhou.util.ConverUtil;

/**
 * Created by fucai.zhou on 2015/10/8.
 */
public class MockService extends Service {

    public final static String TAG = "MockDemo";
    public final static int TO_LEFT = 1;
    public final static int TO_RIGHT = 2;
    public final static int TO_TOP = 3;
    public final static int TO_BOTTOM = 4;
    public final static int TO_LEFTTOP = 5;
    public final static int TO_RIGHTTOP = 6;
    public final static int TO_LEFTBOTTOM = 7;
    public final static int TO_RIGHTBOTTOM = 8;
    public final static int MODE_CYCLE = 9;
    public final static int MODE_ONCE = 10;

    public final static double[] XIAMEN = {118.089423,24.478933};
    private static boolean isMocking = false;

    private static int dis = 11;

    private MyWindowManager mManager;
    private LocationHandler locationHandler;
    private Handler winHandler = new Handler();
    private LocationManager lm;
    private String providerName = LocationManager.GPS_PROVIDER;

    // A background thread for the work tasks
    private HandlerThread mWorkThread;
    // Stores an instance of the object that dispatches work requests to the worker thread
    private Looper mUpdateLooper;

    private MockBinder mBinder = new MockBinder();
    //点击次数
    private static int i,j,k,l,m,n,o,p;
    //转化成投影坐标的x,y
    private int x,y;
    //当前的位置
    private Location mLocation;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(MockService.TAG, "MockService onBind.");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(MockService.TAG, "MockService onStartCommand.");
        mWorkThread = new HandlerThread("UpdateThread", Process.THREAD_PRIORITY_BACKGROUND);
        mWorkThread.start();

        // Get the Looper for the thread
        mUpdateLooper = mWorkThread.getLooper();
        locationHandler = new LocationHandler(mUpdateLooper);

        mManager = MyWindowManager.createInstance(MockService.this);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.addTestProvider(providerName, false, false, false, false, false, true, true, 0, 5);
            lm.setTestProviderEnabled(providerName, true);

        } catch (SecurityException e1){
            Toast.makeText(this,e1.getMessage().toString(),Toast.LENGTH_LONG).show();
            e1.printStackTrace();
        }

        initFloatviews();

        mLocation = new Location(providerName);
        if(MainActivity.localLocation != null){
            mLocation.setLatitude(MainActivity.localLocation.getLatitude());
            mLocation.setLongitude(MainActivity.localLocation.getLongitude());
        }else {
            mLocation.setLatitude(XIAMEN[1]);
            mLocation.setLongitude(XIAMEN[0]);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initFloatviews(){

        mManager.setBtnGestureListener(new MyWindowManager.IGestureListener() {
            @Override
            public void onShowPress(View view, MotionEvent event) {
                isMocking = false;
                Message msg = locationHandler.obtainMessage();
                msg.arg1 = MODE_CYCLE;
                switch (view.getId()){
                    case R.id.left:
                        Log.i(TAG, "left onShowPress.");
                        msg.what = TO_LEFT;
                        break;
                    case R.id.right:
                        Log.i(TAG, "right onShowPress.");
                        msg.what = TO_RIGHT;
                        break;
                    case R.id.top:
                        Log.i(TAG, "top onShowPress.");
                        msg.what = TO_TOP;
                        break;
                    case R.id.bottom:
                        Log.i(TAG, "bottom onShowPress.");
                        msg.what = TO_BOTTOM;
                        break;
                    case R.id.left_top:
                        Log.i(TAG, "left_top onShowPress.");
                        msg.what = TO_LEFTTOP;
                        break;
                    case R.id.right_top:
                        Log.i(TAG, "right_top onShowPress.");
                        msg.what = TO_RIGHTTOP;
                        break;
                    case R.id.left_bottom:
                        Log.i(TAG, "left_bottom onShowPress.");
                        msg.what = TO_LEFTBOTTOM;
                        break;
                    case R.id.right_bottom:
                        Log.i(TAG, "right_bottom onShowPress.");
                        msg.what = TO_RIGHTBOTTOM;
                        break;
                }
                locationHandler.sendMessage(msg);
            }

            @Override
            public void onPressUp(View view, MotionEvent event) {
                Log.i(TAG, "left onPressUp.");
                isMocking = false;
            }

            /**
             * 1.该方法相当于onClick,只有单击才会调用
             * 2.如果用onclick,长按也会被调用，除非有设长按监听setLongClickListener才会区分长按和单击；
             * @param view
             * @param event
             */
            @Override
            public void onSingleTap(View view, MotionEvent event) {
                isMocking = false;
                switch (view.getId()){
                    case R.id.left:
                        Log.i(TAG, "left button tap. currentThread:" + Thread.currentThread().getId());
                        mockGpsPoint(TO_LEFT);
                        break;
                    case R.id.right:
                        Log.i(TAG, "right button tap. currentThread:" + Thread.currentThread().getId());
                        mockGpsPoint(TO_RIGHT);
                        break;
                    case R.id.top:
                        Log.i(TAG, "top button tap. currentThread:" + Thread.currentThread().getId());
                        mockGpsPoint(TO_TOP);
                        break;
                    case R.id.bottom:
                        Log.i(TAG, "bottom button tap. currentThread:" + Thread.currentThread().getId());
                        mockGpsPoint(TO_BOTTOM);
                        break;
                    case R.id.left_top:
                        Log.i(TAG, "left_top button tap. currentThread:" + Thread.currentThread().getId());
                        mockGpsPoint(TO_LEFTTOP);
                        break;
                    case R.id.right_top:
                        Log.i(TAG, "right_top button tap. currentThread:" + Thread.currentThread().getId());
                        mockGpsPoint(TO_RIGHTTOP);
                        break;
                    case R.id.left_bottom:
                        Log.i(TAG, "left_bottom button tap. currentThread:" + Thread.currentThread().getId());
                        mockGpsPoint(TO_LEFTBOTTOM);
                        break;
                    case R.id.right_bottom:
                        Log.i(TAG, "right_bottom button tap. currentThread:" + Thread.currentThread().getId());
                        mockGpsPoint(TO_RIGHTBOTTOM);
                        break;
                }
            }

        });

    }

    private int[] UTM2Xy(String str) {
        int[] xy = new int[2];
        String[] utms = str.split(" ");
        //x
        xy[0] = Integer.parseInt(utms[2]);
        //y
        xy[1] = Integer.parseInt(utms[3]);
        return xy;
    }

    public class MockBinder extends Binder{

        public void removeFloatView(){
            mManager.removeFloatView();
        }

        public void addFloatView(){
            if(MainActivity.localLocation != null){
                mLocation.setLatitude(MainActivity.localLocation.getLatitude());
                mLocation.setLongitude(MainActivity.localLocation.getLongitude());
            }else {
                mLocation.setLatitude(XIAMEN[1]);
                mLocation.setLongitude(XIAMEN[0]);
            }
            winHandler.post(new Runnable() {
                @Override
                public void run() {
                    mManager.addFloatView();
                }
            });
        }

        public void setOriLocation(AMapLocation loc){
            if(loc != null){
                mLocation.setLatitude(loc.getLatitude());
                mLocation.setLongitude(loc.getLongitude());
                reset();
                Message msg = locationHandler.obtainMessage();
                msg.arg1 = MODE_CYCLE;
                msg.arg1 = -1;
                locationHandler.sendMessage(msg);
            }
        }
    }

    private static void reset(){
        i = 0;
        j = 0;
        k = 0;
        l = 0;
        m = 0;
        n = 0;
        o = 0;
        p = 0;
    }

    class LocationHandler extends Handler{

        LocationHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isMocking = true;
            int diretion = msg.what;
            int mode = msg.arg1;

            Log.i(MockService.TAG, "LocationHandler.handleMessage loc=" + mLocation);
            if(lm != null && mLocation != null){
                if(mode == MODE_CYCLE){
                    while (true){
                        if(!isMocking){
                            break;
                        }
                        Log.i(MockService.TAG, "thread:"+Thread.currentThread().getId()+"; "
                                +mLocation.getLongitude() + "," + mLocation.getLatitude());

                        mockGpsPoint(diretion);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    mockGpsPoint(diretion);
                }
            }
        }

    }

    public void mockGpsPoint(int diretion){

        //1.将基点坐标转化为UTM坐标；
        String utmLatlon = ConverUtil.latLon2UTM(mLocation.getLatitude(), mLocation.getLongitude());
        int[] xy = UTM2Xy(utmLatlon);

        //2.将UTM坐标向指定方向偏移；
        x = xy[0];
        y = xy[1];
        if(diretion == TO_LEFT){
            x -= dis;
        }else if(diretion == TO_RIGHT){
            x += dis;
        }else if(diretion == TO_TOP){
            y += dis;
        }else if(diretion == TO_BOTTOM){
            y -= dis;
        }else if(diretion == TO_LEFTTOP){
            y += 0.5*dis;
            x -= 0.5*dis;
        }else if(diretion == TO_RIGHTTOP){
            y += 0.5*dis;
            x += 0.5*dis;
        }else if(diretion == TO_LEFTBOTTOM){
            y -= 0.5*dis;
            x -= 0.5*dis;
        }else if(diretion == TO_RIGHTBOTTOM){
            y -= 0.5*dis;
            x += 0.5*dis;
        }

        //3.将偏移后的UTM坐标转为经纬度坐标；
        String str = "50 R "+ x + " " + y;
        double[] latlon = ConverUtil.utm2LatLon(str);

        mLocation.setLatitude(latlon[0]);
        mLocation.setLongitude(latlon[1]);
        mLocation.setAccuracy(70);
        mLocation.setSpeed(11);
        mLocation.setTime(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //没有设该时间，高德地图不会移动
            mLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        try {
            lm.setTestProviderLocation(providerName, mLocation);
        }catch (SecurityException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            e.printStackTrace();
        }

    }
}
