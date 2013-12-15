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
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.RouteOverlay;
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
    // ��λͼ��
    MyLocationOverlay myLocationOverlay = null;

    boolean isFirstLoc = true;// �Ƿ��״ζ�λ
    
    // ������������·���滮
    MKSearch mMKSearch = null;
    RouteOverlay routeOverlay = null;
    // Target Location, �Ժ�Ҫ�ĳ���ʵ����
    int targetLat = 40057031;
    int targetLgt = 116307852;
    
    boolean fenceIn = false;
    CircleView circleView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init("MBwMkvhVTIRMlnsqlCeXZybo", null);
        // ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        // �����������õ����ſؼ�
        mMapController = mMapView.getController();
        // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
        // GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
        // �ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
        // mMapController.setCenter(point);//���õ�ͼ���ĵ�
        mMapController.setZoom(14);// ���õ�ͼzoom����

        mLocClient = new LocationClient(this);
        locData = new LocationData();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// ��gps
        option.setCoorType("bd09ll"); // ������������
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        // mLocClient.requestLocation();

        // ��λͼ���ʼ��
        myLocationOverlay = new MyLocationOverlay(mMapView);
        // ���ö�λ����
        myLocationOverlay.setData(locData);
        // ��Ӷ�λͼ��
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        // �޸Ķ�λ���ݺ�ˢ��ͼ����Ч
        mMapView.refresh();
        
        MKMapViewListener mapViewListener = new MKMapViewListener() {    
            
            @Override    
            public void onMapMoveFinish() {    
                // �˴�����ʵ�ֵ�ͼ�ƶ�����¼���״̬����    
            }    
                                   
            @Override    
            public void onClickMapPoi(MapPoi arg0) {    
                // �˴���ʵ�ֵ������ͼ�ɵ��עʱ�ļ���    
            }    
          
            @Override  
            public void onGetCurrentMap(Bitmap b) {  
                //��MapView.getCurrentMap()�����ͼ���ڴ˴����ͼ���.    
            }  
          
            @Override  
            public void onMapAnimationFinish() {  
            /** 
             *  ��ͼ��ɴ������Ĳ�������: animationTo()���󣬴˻ص������� 
             */  
            }  
          
            @Override  
            public void onMapLoadFinish() {  
                //��ͼ��ʼ�����ʱ���˻ص�������. 
                int mapWidth = mMapView.getWidth();
                int mapHeight = mMapView.getHeight();
                Log.d("MapView", "width is "+mapWidth+" height is "+mapHeight);
                Point centerPoint = mMapView.getCenterPixel();

                circleView = new CircleView(getApplicationContext());
                circleView.setMinimumWidth(mapWidth);
                circleView.setMinimumHeight(mapHeight);

                circleView.setCenterPixel(centerPoint);
                circleView.setRadius((int)(mapWidth*0.9/2));
                
                circleView.setColor(Color.RED);
                
                MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    // �ؼ���,�̳���ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    // �ؼ���,�̳���ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    // ʹ�ؼ��̶���ĳ������λ��
                    // pt,
                    0, 0,
                    // �ؼ����뷽ʽ
                    MapView.LayoutParams.TOP);
                // ���View��MapView��
                mMapView.addView(circleView, layoutParam);
            }
        };    
        mMapView.regMapViewListener(mBMapMan, mapViewListener);  //ע����� 
        
        mMKSearch = new MKSearch();  
        mMKSearch.init(mBMapMan, new MySearchListener());//ע�⣬MKSearchListenerֻ֧��һ���������һ������Ϊ׼
        
        addItemOverlay();
    }
    
    public class MySearchListener implements MKSearchListener {    
        @Override    
        public void onGetAddrResult(MKAddrInfo result, int iError) {    
               //���ص�ַ��Ϣ�������    
        }    
        @Override    
        public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {    
            //���ؼݳ�·���������    
            if (result == null) {  
                return;  
            }  
            routeOverlay = new RouteOverlay(MapActivity.this, mMapView);  // �˴���չʾһ��������Ϊʾ��  
            routeOverlay.setData(result.getPlan(0).getRoute(0));  
            mMapView.getOverlays().add(routeOverlay);
            mMapView.refresh();
        }    
        @Override    
        public void onGetPoiResult(MKPoiResult result, int type, int iError) {    
                //����poi�������    
        }    
        @Override    
        public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {    
                //���ع����������    
        }    
        @Override    
        public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {    
                //���ز���·���������    
        }    
        @Override        
        public void onGetBusDetailResult(MKBusLineResult result, int iError) {    
                //���ع�����������Ϣ�������    
        }    
        @Override    
        public void onGetSuggestionResult(MKSuggestionResult result, int iError) {    
                //�����������Ϣ�������    
        }  
        @Override   
        public void onGetShareUrlResult(MKShareUrlResult result , int type, int error) {  
               //�ڴ˴���̴����󷵻ؽ��.   
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
            p.setColor(color);
            p.setStyle(Paint.Style.STROKE); 
            p.setStrokeWidth(3f);
            // opacity
            //p.setAlpha(0x80); //
            canvas.drawCircle(x, y, r, p);
            
            Paint p2 = new Paint();
            p2.setAntiAlias(true);
            p2.setColor(color);
            p2.setStyle(Paint.Style.FILL);
            p2.setAlpha(0x20);
            canvas.drawCircle(x, y, r, p2);
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
     * ��λSDK��������
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;

            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            // �������ʾ��λ����Ȧ����accuracy��ֵΪ0����
            locData.accuracy = location.getRadius();
            // �˴��������� locData�ķ�����Ϣ, �����λ SDK δ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
            locData.direction = location.getDerect();
            // ���¶�λ����
            myLocationOverlay.setData(locData);
            // ����ͼ������ִ��ˢ�º���Ч
            mMapView.refresh();
            // ���ֶ�����������״ζ�λʱ���ƶ�����λ��
            if (isFirstLoc) {
                // �ƶ���ͼ����λ��
                Log.d("LocationOverlay", "receive location, animate to it");
                mMapController.animateTo(new GeoPoint(
                        (int) (locData.latitude * 1e6),
                        (int) (locData.longitude * 1e6)));
                // myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
            }
            // �״ζ�λ���
            isFirstLoc = false;
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }
    
    public void addItemOverlay() {
        //׼��overlayͼ�����ݣ�����ʵ������޸�  
        Drawable mark= getResources().getDrawable(R.drawable.point2);
        //��OverlayItem׼��Overlay����  
        OverlayItem item1 = new OverlayItem(new GeoPoint(targetLat, targetLgt),"item1","item1");
        item1.setAnchor(OverlayItem.ALING_CENTER);
        //����IteminizedOverlay  
        ItemizedOverlay<OverlayItem> itemOverlay = new ItemizedOverlay<OverlayItem>(mark, mMapView);
        //��IteminizedOverlay��ӵ�MapView��  
        mMapView.getOverlays().add(itemOverlay);
        //���overlay, ���������Overlayʱʹ��addItem(List<OverlayItem>)Ч�ʸ���  
        itemOverlay.addItem(item1);
        mMapView.refresh();
        //ɾ��overlay .  
        //itemOverlay.removeItem(itemOverlay.getItem(0));  
        //mMapView.refresh();  
        //���overlay  
        // itemOverlay.removeAll();  
        // mMapView.refresh();
    }
    
    public void locateUser(View view) {
        Log.d("ClickButton", "Click lacate user button");
        mMapController.animateTo(new GeoPoint(
            (int) (locData.latitude * 1e6),
            (int) (locData.longitude * 1e6)));
    }
    
    public void locateTarget(View view) {
        Log.d("ClickButton", "Click lacate target button");  
        mMapController.animateTo(new GeoPoint(targetLat, targetLgt));
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
        mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST); // ���üݳ�·���������ԣ�ʱ�����ȡ��������ٻ�������  
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
