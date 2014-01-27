package cn.sevensencond.petmonitor;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import cn.sevensencond.petmonitor.DevicePageActivity.MAP_ID;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.mapapi.map.Symbol.Stroke;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MapActivity extends Activity {

    BMapManager mBMapMan = null;
    MapView mMapView = null;
    MapController mMapController = null;

    LocationClient mLocClient;
    LocationData locData = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    // 定位图层
    MyLocationOverlay myLocationOverlay = null;

//    boolean isFirstLoc = true;// 是否首次定位
    
    // 检索服务，用于路径规划
    MKSearch mMKSearch = null;
    RouteOverlay routeOverlay = null;
    // Target Location, 以后要改成真实坐标
    int targetLat = 40057031;
    int targetLgt = 116307852;
    
    boolean fenceIn = false;
    CircleView circleView = null;
    
    private RelativeLayout mapLocationLayout = null;
    private RelativeLayout mapFenceLayout = null;
    
    boolean isCurLocTarget = false;
    ImageButton routeButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init("MBwMkvhVTIRMlnsqlCeXZybo", null);
        // 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
        setContentView(R.layout.activity_map);

        mMapView = (MapView) findViewById(R.id.bmapsView);
        // 设置启用内置的缩放控件
//      mMapView.setBuiltInZoomControls(true);
        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        mMapController = mMapView.getController();
        // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        // GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
        // mMapController.setCenter(point); //设置地图中心点
        mMapController.setZoom(14);// 设置地图zoom级别
        
        mMKSearch = new MKSearch();  
        mMKSearch.init(mBMapMan, new MySearchListener());//注意，MKSearchListener只支持一个，以最后一次设置为准
        
        
        mapLocationLayout = (RelativeLayout)findViewById(R.id.maplocation_layout);
        mapFenceLayout = (RelativeLayout)findViewById(R.id.fence_relativelayout_buttons);
        
        Bundle localBundle = getIntent().getBundleExtra("cn.sevensencond.petmonitor.map");
        int mapType = localBundle.getInt("cn.sevensencond.petmonitor.mapType", -1);
        if (mapType == MAP_ID.TRACK.ordinal()) {
            mapLocationLayout.setVisibility(View.VISIBLE);
            mapFenceLayout.setVisibility(View.GONE);

            addFence();
            
            // 设置接收数据
            mLocClient = new LocationClient(this);
            locData = new LocationData();
            mLocClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);// 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            mLocClient.setLocOption(option);
            mLocClient.start();
            
            // 定位图层初始化
            myLocationOverlay = new MyLocationOverlay(mMapView);
            // 设置定位数据
            myLocationOverlay.setData(locData);
            // 设置图标
            myLocationOverlay.setMarker(getResources().getDrawable(R.drawable.point1));
            // 添加定位图层
            mMapView.getOverlays().add(myLocationOverlay);
            myLocationOverlay.enableCompass();
            // 修改定位数据后刷新图层生效
            mMapView.refresh();
            
            routeButton = (ImageButton)findViewById(R.id.maplocation_button_route);
            ImageButton preLocButton = (ImageButton)findViewById(R.id.maplocation_button_prev);
            ImageButton nextLocButton = (ImageButton)findViewById(R.id.maplocation_button_next);
            View.OnClickListener locListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (isCurLocTarget) {
                        locateUser(v);
                    } else {
                        locateTarget(v);
                    }
                }
            };
            preLocButton.setOnClickListener(locListener);
            nextLocButton.setOnClickListener(locListener);
            
            addTargetOverlay();
            locateTarget(mMapView);
        } else if (mapType == MAP_ID.FENCE.ordinal()) {
            
        }
        
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
//                Point centerPoint = mMapView.getCenterPixel();
//                addCircleView(centerPoint, mapWidth, mapHeight);
            }
        };
        mMapView.regMapViewListener(mBMapMan, mapViewListener);  //注册监听 
    }
    
    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addFence(){
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
        mMapView.getOverlays().add(graphicsOverlay);
        //添加点
//        graphicsOverlay.setData(drawPoint());
        //添加折线
//        graphicsOverlay.setData(drawLine());
        //添加多边形
//        graphicsOverlay.setData(drawPolygon());
        //添加圆
        int radius = 2500;
        double lat = targetLat/1E6, lon = targetLgt/1E6;
        boolean isFenceIn = true;
        graphicsOverlay.setData(drawCircle(radius, lat, lon, isFenceIn));
        //绘制文字
        TextOverlay textOverlay = new TextOverlay(mMapView);
        mMapView.getOverlays().add(textOverlay);
//        textOverlay.addText(drawText());
        //执行地图刷新使生效
        mMapView.refresh();
    }
    /**
     * 绘制圆，该圆随地图状态变化
     * @return 圆对象
     */
    public Graphic drawCircle(int mRadius, double mLat, double mLon, boolean isFenceIn) { 
        int lat = (int) (mLat*1E6);
        int lon = (int) (mLon*1E6);     
        GeoPoint centerPoint = new GeoPoint(lat, lon);
        
        //构建圆
        Geometry circleGeometry = new Geometry();
    
        //设置圆中心点坐标和半径
        circleGeometry.setCircle(centerPoint, mRadius);
        //设置样式
        Symbol circleSymbol = new Symbol();
        Symbol.Color circleColor, translucentCircleColor;
        if (isFenceIn) {
            circleColor = circleSymbol.new Color(0xFF0000FF);
            translucentCircleColor = circleSymbol.new Color(0x200000FF);
        } else {
            circleColor = circleSymbol.new Color(0xFFFF0000);
            translucentCircleColor = circleSymbol.new Color(0x20FF0000);
        }
        circleSymbol.setSurface(translucentCircleColor, 1, 3, new Stroke(3, circleColor));
        //生成Graphic对象
        Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);
        return circleGraphic;
   }
    
    public class MySearchListener implements MKSearchListener {    
        @Override    
        public void onGetAddrResult(MKAddrInfo result, int iError) {    
               //返回地址信息搜索结果    
        }    
        @Override    
        public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {    
            //返回驾乘路线搜索结果    
            if (result == null) {  
                return;  
            }  
            routeOverlay = new RouteOverlay(MapActivity.this, mMapView);  // 此处仅展示一个方案作为示例  
            routeOverlay.setData(result.getPlan(0).getRoute(0));  
            mMapView.getOverlays().add(routeOverlay);
            mMapView.refresh();
        }    
        @Override    
        public void onGetPoiResult(MKPoiResult result, int type, int iError) {    
                //返回poi搜索结果    
        }    
        @Override    
        public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {    
                //返回公交搜索结果    
        }    
        @Override    
        public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {    
                //返回步行路线搜索结果    
        }    
        @Override        
        public void onGetBusDetailResult(MKBusLineResult result, int iError) {    
                //返回公交车详情信息搜索结果    
        }    
        @Override    
        public void onGetSuggestionResult(MKSuggestionResult result, int iError) {    
                //返回联想词信息搜索结果    
        }  
        @Override   
        public void onGetShareUrlResult(MKShareUrlResult result , int type, int error) {  
               //在此处理短串请求返回结果.   
        }
        @Override
        public void onGetPoiDetailSearchResult(int arg0, int arg1) {
            // TODO Auto-generated method stub
            
        }
    }
    
    public class CircleView extends View {
        
        // default center pixel coordinate and default radius
        int x = 240;
        int y = 378;
        int r = 200;
        int color = Color.RED;
        Paint strokePaint, fillPaint;
        
        public CircleView(Context context) {
            super(context);
            strokePaint = new Paint();
            fillPaint = new Paint();
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
//            canvas.drawColor(Color.CYAN);
            // smooths
            strokePaint.setAntiAlias(true);
            strokePaint.setColor(color);
            strokePaint.setStyle(Paint.Style.STROKE); 
            strokePaint.setStrokeWidth(3f);
            canvas.drawCircle(x, y, r, strokePaint);
            
            fillPaint.setAntiAlias(true);
            fillPaint.setColor(color);
            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setAlpha(0x20);
            canvas.drawCircle(x, y, r, fillPaint);
        }
        
        public void setRadius(int r) {
            this.r = r;
        }
        
        public void setCenterPixel(Point pt) {
            this.x = pt.x;
            this.y = pt.y;
        }
        
        public void setColor(int color) {
            this.color = color;
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
//            if (isFirstLoc) {
//                // 移动地图到定位点
//                Log.d("LocationOverlay", "receive location, animate to it");
//                locateUser(mMapView);
////                mMapController.animateTo(new GeoPoint(
////                        (int) (locData.latitude * 1e6),
////                        (int) (locData.longitude * 1e6)));
//                // myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
//            }
//            // 首次定位完成
//            isFirstLoc = false;
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }
    
    public void addCircleView(Point centerPoint, int minWidth, int minHeight) {
        circleView = new CircleView(getApplicationContext());
        circleView.setMinimumWidth(minWidth);
        circleView.setMinimumHeight(minHeight);

        circleView.setCenterPixel(centerPoint);
        circleView.setRadius((int)(minWidth*0.9/2));
        
        circleView.setColor(Color.RED);
        
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
    
    public void addTargetOverlay() {
        //准备overlay图像数据，根据实情情况修复  
        Drawable mark= getResources().getDrawable(R.drawable.point2);
        //用OverlayItem准备Overlay数据  
        OverlayItem item1 = new OverlayItem(new GeoPoint(targetLat, targetLgt),"item1","item1");
        item1.setAnchor(OverlayItem.ALING_CENTER);
        //创建IteminizedOverlay  
        ItemizedOverlay<OverlayItem> itemOverlay = new ItemizedOverlay<OverlayItem>(mark, mMapView);
        //将IteminizedOverlay添加到MapView中  
        mMapView.getOverlays().add(itemOverlay);
        //添加overlay, 当批量添加Overlay时使用addItem(List<OverlayItem>)效率更高  
        itemOverlay.addItem(item1);
        mMapView.refresh();
        //删除overlay .  
        //itemOverlay.removeItem(itemOverlay.getItem(0));  
        //mMapView.refresh();  
        //清除overlay  
        // itemOverlay.removeAll();  
        // mMapView.refresh();
    }
    
    public void locateUser(View view) {
        Log.d("ClickButton", "Click lacate user button");
        mMapController.animateTo(new GeoPoint(
            (int) (locData.latitude * 1e6),
            (int) (locData.longitude * 1e6)));
        
        isCurLocTarget = false;
        routeButton.setEnabled(false);
    }
    
    public void locateTarget(View view) {
        Log.d("ClickButton", "Click lacate target button");  
        mMapController.animateTo(new GeoPoint(targetLat, targetLgt));

        isCurLocTarget = true;
        routeButton.setEnabled(true);
    }
    
    public void handleRoute(View view) {
        Log.d("ClickButton", "Click handle route button");
        if (routeOverlay != null) {
            mMapView.getOverlays().remove(routeOverlay);
            mMapView.refresh();
            routeOverlay = null;
            return;
        }
        MKPlanNode start = new MKPlanNode();  
        start.pt = new GeoPoint((int) (locData.latitude * 1E6), (int) (locData.longitude * 1E6));  
        MKPlanNode end = new MKPlanNode();  
        end.pt = new GeoPoint(targetLat, targetLgt);
        mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST); // 设置驾车路线搜索策略，时间优先、费用最少或距离最短  
        mMKSearch.drivingSearch(null, start, null, end);
    }
    
    public void fenceInOut(View view) {
        Log.d("ClickButton", "Click fenceInOut button");
        int color = (fenceIn)? Color.BLUE : Color.RED;
        circleView.setColor(color);
        circleView.invalidate();
        ImageButton imageButton = (ImageButton)findViewById(R.id.fence_button_fenceinout);
//        imageButton.setBackground(getResources().getDrawable(R.drawable.fenceout));
        int imageRrc = (fenceIn)? R.drawable.fenceout: R.drawable.fencein;
        imageButton.setImageResource(imageRrc);
        fenceIn = !fenceIn;
    }
    
    public void goBack(View view) {
        finish();
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
            // 有时候路径规划会失效，所以每次onResume都初始化一次，希望能解决这个问题
            if (mMKSearch != null) {
                mMKSearch.init(mBMapMan, new MySearchListener());
            }
        }
        super.onResume();
    }
}
