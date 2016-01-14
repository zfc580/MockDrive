package com.fucaizhou.mockdrive;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.Marker;
import com.fucaizhou.mockdrive.MockService.MockBinder;


public class MainActivity extends Activity implements AMapLocationListener {

    public static AMapLocation localLocation;
    private TextView mGpsTitle,mGpsText;
    private Button mSetOriBtn;
    private Button mHelpBtn;
    private Button mShowBtn,mRemoveBtn;
    private LocationManager mGoogleLocManager;
    private MockBinder mMockBinder;
    private MapView mapView;
    private AMap aMap;
    Marker marker;
    LocationSource.OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private LocationListener mGoogleLocListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        aMap = mapView.getMap();
        // 设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                Log.i(MockService.TAG, "MainActivity activate.");
                mListener = onLocationChangedListener;

                if (mAMapLocationManager == null) {
                    mAMapLocationManager = LocationManagerProxy.getInstance(MainActivity.this);
                    //请求网络位置
                    mAMapLocationManager.requestLocationData(
                            LocationProviderProxy.AMapNetwork, 2000, 10, MainActivity.this);
                }
            }

            @Override
            public void deactivate() {
                Log.i(MockService.TAG, "MainActivity deactivate.");
                mListener = null;
                if (mAMapLocationManager != null) {
                    mAMapLocationManager.removeUpdates(MainActivity.this);
                    mAMapLocationManager.destroy();
                }
                mAMapLocationManager = null;
            }
        });
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 获取当前地图的缩放级别
        float mZoom = aMap.getCameraPosition().zoom;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(mZoom+5));

//        aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//
//                Log.i(MockService.TAG, "onMapLongClick latLng:"+latLng.longitude+","+latLng.latitude);
//                if(marker != null){
//                    marker.setVisible(false);
//                }
//                marker = aMap.addMarker(new MarkerOptions().position(latLng));
//                localLocation.setLatitude(latLng.latitude);
//                localLocation.setLongitude(latLng.longitude);
//                if(marker != null){
//                    mSetOriBtn.setVisibility(View.VISIBLE);
//                }
//                //mTextView.setText("当前位置："+latLng.longitude+","+latLng.latitude);
//            }
//        });

        //===========================================================================================

        mGpsText = (TextView) findViewById(R.id.gps_text);
        mGpsTitle = (TextView) findViewById(R.id.gps_title);
        mShowBtn = (Button) findViewById(R.id.show_panel);
        mHelpBtn = (Button) findViewById(R.id.help_btn);
        mHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HelpActivity.class));
            }
        });
        mRemoveBtn = (Button) findViewById(R.id.remove_panel);
        mSetOriBtn = (Button) findViewById(R.id.set_origin);

//        mSetOriBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(MockService.TAG, "onClick localLocation:"+localLocation.getLongitude()+","+localLocation.getLatitude());
//                mSetOriBtn.setVisibility(View.GONE);
//                mMockBinder.setOriLocation(localLocation);
//            }
//        });

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMockBinder.addFloatView();
            }
        });
        mRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMockBinder.removeFloatView();
            }
        });
        mGoogleLocListener = new LocationListener() {
            /**
             * 该方法为GPS位置回调，模拟GPS和真实GPS都通过该回调返回
             * @param location
             */
            @Override
            public void onLocationChanged(Location location) {
                if(mListener != null){
                    mListener.onLocationChanged(location);// 显示系统小蓝点
                }
                //Log.i(MockService.TAG, "onLocationChanged location:" + location.getLongitude() + "," + location.getLatitude());
                mGpsTitle.setText("模拟位置:");
                mGpsTitle.setTextColor(Color.RED);
                mGpsText.setText(location.getLongitude()+","+location.getLatitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mGoogleLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //请求GPS位置
        mGoogleLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mGoogleLocListener);

        Intent intent = new Intent(MainActivity.this, MockService.class);
        startService(intent);
        bindService(intent, mockConnetion, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mockConnetion = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(MockService.TAG,"MainActivity onServiceConnected.");
            mMockBinder = (MockBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleLocManager.removeUpdates(mGoogleLocListener);
        mapView.onDestroy();
        unbindService(mockConnetion);
    }

    /**
     * 该方法为网络定位的位置的回调，通过高德SDK获取到
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            //Log.i(MockService.TAG, "onLocationChanged aMapLocation:" + aMapLocation.getLongitude() + "," + aMapLocation.getLatitude());
            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            mGpsTitle.setText("真实位置:");
            mGpsTitle.setTextColor(Color.BLACK);
            mGpsText.setText(aMapLocation.getLongitude()+","+aMapLocation.getLatitude());
            if(localLocation == null){
                localLocation = aMapLocation;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
