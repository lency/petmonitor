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
    // ��λͼ��
    MyLocationOverlay myLocationOverlay = null;

    boolean isFirstLoc = true;// �Ƿ��״ζ�λ

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

        MKMapStatusChangeListener mylistener = new MKMapStatusChangeListener() {
            public void onMapStatusChange(MKMapStatus mapStatus) {
                float zoom = mapStatus.zoom; // ��ͼ���ŵȼ�
                int overlooking = mapStatus.overlooking; // ��ͼ���ӽǶ�
                int rotate = mapStatus.rotate; // ��ͼ��ת�Ƕ�
                GeoPoint targetGeo = mapStatus.targetGeo; // ���ĵ�ĵ�������
                Point targetScreen = mapStatus.targetScreen; // ���ĵ����Ļ����
                // TODO add your process
                
                Projection projection = mMapView.getProjection();
                int circleLeftX = targetScreen.x * 2 / 10;
                GeoPoint gPoint = projection.fromPixels(circleLeftX, targetScreen.y);
                Log.d("Some", "gPoint is: "+ gPoint);

                double distance = DistanceUtil.getDistance(gPoint, targetGeo);
                Log.d("Some", "distance is: "+distance);
                
            }
        };
        // Ϊ mapview ע���ͼ״̬�����ߡ�
        mMapView.regMapStatusChangeListener(mylistener);
        
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

                CircleView circleView = new CircleView(getApplicationContext());
                circleView.setMinimumWidth(mapWidth);
                circleView.setMinimumHeight(mapHeight);

                circleView.setCenterPixel(centerPoint);
                circleView.setRadius((int)(mapWidth*0.8/2));
                
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
