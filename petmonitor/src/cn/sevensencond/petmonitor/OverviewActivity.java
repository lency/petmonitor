package cn.sevensencond.petmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import cn.sevensencond.petmonitor.ScrollLayout.OnScreenChangeListener;
import cn.sevensencond.petmonitor.ScrollLayout.OnScreenChangeListenerDataLoad;

public class OverviewActivity extends Activity {
    public static final int UPDATE_STICKER = 1;

    private DBManager dbManager = null; 
    
    ListView devicesListView = null;
    String[] deviceNames = { "Google Plus", "Twitter", "Windows", "Bing", "Sun",
            "Oracle" };
    String[] ownerNames = { "主控号码：13716468641", "android手机", "unipro分享", "No Info", "No Info", "No Info" };

    private View mDevicesNoneTips = null;
    private View mDevicesViewTips = null;
    private View mDevicesUpArrow = null;
    private View mDevicesDownArrow = null;
    private int mDevicesCount = 0;
    
    private AbsListView.OnScrollListener devicesListViewScrollListener = new AbsListView.OnScrollListener() {
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.d("ListView Scroll", "firstVisibleItem: "+firstVisibleItem);
            Log.d("ListView Scroll", "visibleItemCount: "+visibleItemCount);
            Log.d("ListView Scroll", "totalItemCount: "+totalItemCount);
            
            if (firstVisibleItem > 0) {
                mDevicesUpArrow.setVisibility(View.VISIBLE);
            } else {
                mDevicesUpArrow.setVisibility(View.INVISIBLE);
            }
            if (firstVisibleItem + visibleItemCount < totalItemCount ) {
                mDevicesDownArrow.setVisibility(View.VISIBLE);
            } else {
                mDevicesDownArrow.setVisibility(View.INVISIBLE);
            }
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d("ListView Scroll", "Scroll state changed");
            
        }
    };
    
    private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View paramView, int position, long id) {
            ArrayAdapter<String> parentAdapter = (ArrayAdapter<String>)parent.getAdapter();
            Log.d("List", "onListItemClick,position = " + position
                    + " count = " + parentAdapter.getCount());
            
            Intent intent = new Intent(OverviewActivity.this, DevicePageActivity.class);
            startActivity(intent);
        }
    };
    
    private ImageView mImgNavDev = null;
    private ImageView mImgNavFriend = null;
    
    ImageView mMyselfHead = null;
    TextView mUserNameText = null;
    
    private boolean isDevicesUpdating = false;
    private boolean isDownloadingUserSticker = false;
    private String mUserName = "";
    
    private ExecutorService downloadService = null;
    
    Bitmap mBitmapUserSticker = null;
    
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.d("OverviewActivity", "handleMessage " + msg.what);
            switch (msg.what) {
            case UPDATE_STICKER:
                if (mBitmapUserSticker != null) {
                    int roundPx = (int)(mBitmapUserSticker.getWidth()*0.2);
                    Bitmap localBitmap = Util.toRoundCorner(mBitmapUserSticker, roundPx);
                    mMyselfHead.setImageBitmap(localBitmap);
                }
                break;

            default:
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);
        
        mMyselfHead = (ImageView)findViewById(R.id.main_img_myselfhead);
        mUserNameText = ((TextView)findViewById(R.id.main_text_myselfname));
        ((TextView)findViewById(R.id.main_text_myselfnumber)).setText(R.string.logged);
        
        mImgNavDev = (ImageView)findViewById(R.id.imgnav_dev);
        mImgNavFriend = (ImageView)findViewById(R.id.imgnav_friend);
        mImgNavDev.setImageResource(R.drawable.nav1);
        mImgNavFriend.setImageResource(R.drawable.nav2);

        ScrollLayout scrollLayout = (ScrollLayout) findViewById(R.id.main_viewflipper);
        scrollLayout.setOnScreenChangeListener(new OnScreenChangeListener() {
            @Override
            public void onScreenChange(int currentIndex) {
                // TODO Auto-generated method stub
                if (currentIndex == 0) {
                    mImgNavDev.setImageResource(R.drawable.nav1);
                    mImgNavFriend.setImageResource(R.drawable.nav2);
                } else {
                    mImgNavDev.setImageResource(R.drawable.nav2);
                    mImgNavFriend.setImageResource(R.drawable.nav1);
                }
                
            }
        });
        scrollLayout.setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {
            @Override
            public void onScreenChange(int currentIndex) {

            }
        });
        
        dbManager = new DBManager(this);
        
        genListData();

        List<Device> devices = dbManager.query();
        CustomList adapter = new CustomList(this, devices);
        
//        final DBHelper helper = new DBHelper(this);
//        Cursor c = helper.query();
//        String[] from = { "name", "phoneNumber"};
//        int[] to = { R.id.mylistitem_text_name, R.id.mylistitem_text_number };
//        CustomCursorAdapter adapter = new CustomCursorAdapter(OverviewActivity.this, c, from, to);
//        ListView listView = getListView();  
//        listView.setAdapter(adapter);
        
//        CustomList adapter = new CustomList(OverviewActivity.this, web, imageId);
        devicesListView = (ListView) findViewById(R.id.main_listview_devices);
        devicesListView.setAdapter(adapter);
//        devicesListView.setOnItemClickListener(listViewItemClickListener);
        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CustomCursorAdapter parentAdapter = (CustomCursorAdapter)parent.getAdapter();
//                Log.d("List", "onListItemClick,position = " + position
//                        + " count = " + parentAdapter.getCount());
//                
//                Cursor cursor = parentAdapter.getCursor();
//                int nameIndex = cursor.getColumnIndex("name");
//                String name = cursor.getString(nameIndex);
                CustomList adapter = (CustomList)parent.getAdapter();
                Device device = adapter.getItem(position);
                
                Intent intent = new Intent(OverviewActivity.this, DevicePageActivity.class);
                Bundle localBundle = new Bundle();
                localBundle.putString("cn.sevensencond.petmonitor.devicename", device.name);
                localBundle.putString("cn.sevensencond.petmonitor.deviceserial", device.serialNumber);
                intent.putExtra("cn.sevensencond.petmonitor.devicePage", localBundle);
                startActivity(intent);
                
            }
        });
//        helper.close();
        
        mDevicesNoneTips = findViewById(R.id.devices_none_tips);
        mDevicesViewTips = findViewById(R.id.devices_view_tips);
        mDevicesUpArrow = findViewById(R.id.devices_view_uparrow);
        mDevicesDownArrow = findViewById(R.id.devices_view_downarrow);
        mDevicesNoneTips.setVisibility(View.GONE);
        mDevicesViewTips.setVisibility(View.GONE);
//        mDevicesUpArrow.setVisibility(View.GONE);
//        mDevicesDownArrow.setVisibility(View.GONE);
        
        // init arrowUp and arrowDown
        mDevicesCount = deviceNames.length;
        if (devicesListView.getFirstVisiblePosition() > 0) {
            mDevicesUpArrow.setVisibility(View.VISIBLE);
        }
        if (devicesListView.getLastVisiblePosition() < mDevicesCount - 1) {
            mDevicesDownArrow.setVisibility(View.VISIBLE);
        }
        
        devicesListView.setOnScrollListener(devicesListViewScrollListener);
        
        downloadService = Executors.newSingleThreadExecutor();
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        mUserName  = "xiaopeng";
        mUserNameText.setText(mUserName);
        if (!isDownloadingUserSticker )
        {
            updateUserSticker();
        }
//        Util.sendMessage(this.mHandler, 73, null);
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        downloadService.shutdown();
        dbManager.closeDB();
    }
    
    private void updateUserSticker() {
        if (downloadService.isShutdown())
            return;
        downloadService.submit(new Runnable() {
            public void run() {
                isDownloadingUserSticker = true;
                // TODO test url here
                String str = "http://t10.baidu.com/it/u=1707069858,2169216855&fm=21&gp=0.jpg";
                if (str.length() == 0)
                    return;

                byte[] arrayOfByte = null;
                try {
                    if (Util.checkNetwork(OverviewActivity.this)) {
                        arrayOfByte = Util.getImage(str);
                        if (arrayOfByte == null)
                            return;
                        mBitmapUserSticker = BitmapFactory
                                .decodeByteArray(arrayOfByte, 0,
                                        arrayOfByte.length);
                        Util.saveUserSticker(mBitmapUserSticker);
                        isDownloadingUserSticker = false;
                        Util.sendMessage(mHandler, UPDATE_STICKER, null);
                        return;
                    }
                } catch (Exception localException) {
                    localException.printStackTrace();
                    isDownloadingUserSticker = false;
                    return;
                }

            }
        });
    }
    
    public void switchUser(View view) {
        ScrollLayout scrollLayout = (ScrollLayout) findViewById(R.id.main_viewflipper);
        int snapToScreenIndex = (scrollLayout.getCurScreen() == 0)? 1 : 0;
        scrollLayout.snapToScreen(snapToScreenIndex);
    }

    public class CustomList extends ArrayAdapter<Device> {
        private List<Device> devices;
        private LayoutInflater inflater;

        public CustomList(Context context, List<Device> devices) {
            super(context, R.layout.list_single, devices);
            this.inflater = LayoutInflater.from(context);
            this.devices = devices;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View rowView = inflater.inflate(R.layout.list_single, null, true);
            TextView txtTitle = (TextView)rowView.findViewById(R.id.mylistitem_text_name);
            Device device = devices.get(position);
            txtTitle.setText(device.name);
            TextView phoneNumberTextView = (TextView)rowView.findViewById(R.id.mylistitem_text_number);
            phoneNumberTextView.setText(device.ownerName);
            ImageButton imageButton = (ImageButton)rowView.findViewById(R.id.mylistitem_button_call);
            Log.d("deviceinfo", device.serialNumber + " " + device.phoneNumber);
            if (device.phoneNumber.length() == 0) {
                imageButton.setImageResource(R.drawable.locator_phone_disable);
            }
            return rowView;
        }
    }
    
    public class CustomCursorAdapter extends SimpleCursorAdapter {
        private LayoutInflater mInflater; 
        
        public CustomCursorAdapter(Context context, Cursor c, String[] from, int[] to) {
            super(context, R.layout.list_single, c, from, to, 0);
        }
        
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // TODO Auto-generated method stub
            mInflater = LayoutInflater.from(context);
            View view = mInflater.inflate(R.layout.list_single, null, true);
            return view;
        }
        
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // TODO Auto-generated method stub
//            super.bindView(view, context, cursor);
            
            int nameIndex = cursor.getColumnIndex("name");
            int phoneIndex = cursor.getColumnIndex("phoneNumber");
            
            TextView txtTitle = (TextView)view.findViewById(R.id.mylistitem_text_name);
            txtTitle.setText(cursor.getString(nameIndex));
            TextView phoneNumberTextView = (TextView)view.findViewById(R.id.mylistitem_text_number);
            phoneNumberTextView.setText(cursor.getString(phoneIndex));
            ImageButton imageButton = (ImageButton)view.findViewById(R.id.mylistitem_button_call);
            if (cursor.getPosition() == 3) {
                imageButton.setImageResource(R.drawable.locator_phone_disable);
            }
        }
    }
    
    public void genListData() {
        Log.d("database", "count" + dbManager.query().size());
//        List<Device> devices = dbManager.query();
//        for (int i = 0; i < devices.size(); i++) {
//            Device device = devices.get(i);
//            dbManager.del(device);
//        }
        if (dbManager.query().size() == 0) {
            for (int i = 0; i < deviceNames.length; i++) {
                ContentValues values = new ContentValues();
                values.put("name", deviceNames[i]);
                values.put("ownerName", ownerNames[i]);
                values.put("serialNumber", "10000000" + String.valueOf(i));
                String phoneString = "";
                if (i != 2) {
                    phoneString = "1860000000" + String.valueOf(i);
                }
                values.put("phoneNumber", phoneString);
                dbManager.add(values);
            }
        }
    }

}
