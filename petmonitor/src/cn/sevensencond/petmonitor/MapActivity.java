package cn.sevensencond.petmonitor;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapStatus;
import com.baidu.mapapi.map.MKMapStatusChangeListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;

public class MapActivity extends Activity {

    BMapManager mBMapMan = null;
    MapView mMapView = null;
    MapController mMapController = null;

    LocationClient mLocClient;
    LocationData locData = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    // 定位图层
    MyLocationOverlay myLocationOverlay = null;

    boolean isFirstLoc = true;// 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init("MBwMkvhVTIRMlnsqlCeXZybo", null);
        // 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        // 设置启用内置的缩放控件
        mMapController = mMapView.getController();
        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        // GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
        // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        // mMapController.setCenter(point);//设置地图中心点
        mMapController.setZoom(14);// 设置地图zoom级别

        mLocClient = new LocationClient(this);
        locData = new LocationData();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        // mLocClient.requestLocation();

        // 定位图层初始化
        myLocationOverlay = new MyLocationOverlay(mMapView);
        // 设置定位数据
        myLocationOverlay.setData(locData);
        // 添加定位图层
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        // 修改定位数据后刷新图层生效
        mMapView.refresh();

        MKMapStatusChangeListener mylistener = new MKMapStatusChangeListener() {
            public void onMapStatusChange(MKMapStatus mapStatus) {
                float zoom = mapStatus.zoom; // 地图缩放等级
                int overlooking = mapStatus.overlooking; // 地图俯视角度
                int rotate = mapStatus.rotate; // 地图旋转角度
                GeoPoint targetGeo = mapStatus.targetGeo; // 中心点的地理坐标
                Point targetScreen = mapStatus.targetScreen; // 中心点的屏幕坐标
                // TODO add your process
                
                Projection projection = mMapView.getProjection();
                int circleLeftX = targetScreen.x * 2 / 10;
                GeoPoint gPoint = projection.fromPixels(circleLeftX, targetScreen.y);
                Log.d("Some", "gPoint is: "+ gPoint);

                double distance = DistanceUtil.getDistance(gPoint, targetGeo);
                Log.d("Some", "distance is: "+distance);
                
            }
        };
        // 为 mapview 注册地图状态监听者。
        mMapView.regMapStatusChangeListener(mylistener);
        
        MKMapViewListener mapViewListener = new MKMapViewListener() {    
            
            @Override    
            public void onMapMoveFinish() {    
                // 此处可以实现地图移动完成事件的状态监听    
            }    
                                   
            @Override    
            public void onClickMapPoi(MapPoi arg0) {    
                // 此处可实现点击到地图可点标注时的监听    
            }    
          
            @Override  
            public void onGetCurrentMap(Bitmap b) {  
                //用MapView.getCurrentMap()发起截图后，在此处理截图结果.    
            }  
          
            @Override  
            public void onMapAnimationFinish() {  
            /** 
             *  地图完成带动画的操作（如: animationTo()）后，此回调被触发 
             */  
            }  
          
            @Override  
            public void onMapLoadFinish() {  
                //地图初始化完成时，此回调被触发. 
                int mapWidth = mMapView.getWidth();
                int mapHeight = mMapView.getHeight();
                Log.d("MapView", "width is "+mapWidth+" height is "+mapHeight);
                Point centerPoint = mMapView.getCenterPixel();

                CircleView circleView = new CircleView(getApplicationContext());
                circleView.setMinimumWidth(mapWidth);
                circleView.setMinimumHeight(mapHeight);

                circleView.setCenterPixel(centerPoint);
                circleView.setRadius((int)(mapWidth*0.8/2));
                
                MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                // 控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    // 控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    // 使控件固定在某个地理位置
                    // pt,
                    0, 0,
                    // 控件对齐方式
                    MapView.LayoutParams.TOP);
                // 添加View到MapView中
                mMapView.addView(circleView, layoutParam);
            }       
        };    
        mMapView.regMapViewListener(mBMapMan, mapViewListener);  //注册监听 
    }
    
    public class CircleView extends View {
        
        int x = 240;
        int y = 378;
        int r = 200;
        
        public CircleView(Context context) {
            super(context);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
//            canvas.drawColor(Color.CYAN);
            Paint p = new Paint();
            // smooths
            p.setAntiAlias(true);
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE); 
            p.setStrokeWidth(4.5f);
            // opacity
            //p.setAlpha(0x80); //
            canvas.drawCircle(x, y, r, p);
            
            Paint p2 = new Paint();
            p2.setAntiAlias(true);
            p2.setColor(Color.BLACK);
            p2.setStyle(Paint.Style.FILL);
            p2.setAlpha(0x80);
            canvas.drawCircle(x, y, r, p2);
        }
        
        public void setRadius(int r) {
            this.r = r;
        }
        
        public void setCenterPixel(Point pt) {
            this.x = pt.x;
            this.y = pt.y;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;

            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            // 如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = location.getRadius();
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            locData.direction = location.getDerect();
            // 更新定位数据
            myLocationOverlay.setData(locData);
            // 更新图层数据执行刷新后生效
            mMapView.refresh();
            // 是手动触发请求或首次定位时，移动到定位点
            if (isFirstLoc) {
                // 移动地图到定位点
                Log.d("LocationOverlay", "receive location, animate to it");
                mMapController.animateTo(new GeoPoint(
                        (int) (locData.latitude * 1e6),
                        (int) (locData.longitude * 1e6)));
                // myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
            }
            // 首次定位完成
            isFirstLoc = false;
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        mMapView.destroy();
        if (mBMapMan != null) {
            mBMapMan.destroy();
            mBMapMan = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        if (mBMapMan != null) {
            mBMapMan.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        if (mBMapMan != null) {
            mBMapMan.start();
        }
        super.onResume();
    }
}
