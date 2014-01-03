package cn.sevensencond.petmonitor;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import cn.sevensencond.petmonitor.ScrollLayout.OnScreenChangeListener;
import cn.sevensencond.petmonitor.ScrollLayout.OnScreenChangeListenerDataLoad;

public class OverviewActivity extends Activity {

    ListView devicesListView = null;
    String[] web = { "Google Plus", "Twitter", "Windows", "Bing", "Sun",
            "Oracle" };
    Integer[] imageId = { R.drawable.smallheader, R.drawable.smallheader,
            R.drawable.smallheader, R.drawable.smallheader,
            R.drawable.smallheader, R.drawable.smallheader };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);
        
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
        
        genListData();

        final DBHelper helpter = new DBHelper(this);
        Cursor c = helpter.query();
        String[] from = { "name", "phoneNumber"};
        int[] to = { R.id.mylistitem_text_name, R.id.mylistitem_text_number };
        CustomCursorAdapter adapter = new CustomCursorAdapter(OverviewActivity.this, c, from, to);
//        ListView listView = getListView();  
//        listView.setAdapter(adapter);
        
//        CustomList adapter = new CustomList(OverviewActivity.this, web, imageId);
        devicesListView = (ListView) findViewById(R.id.main_listview_devices);
        devicesListView.setAdapter(adapter);
//        devicesListView.setOnItemClickListener(listViewItemClickListener);
        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SimpleCursorAdapter parentAdapter = (SimpleCursorAdapter)parent.getAdapter();
//                Log.d("List", "onListItemClick,position = " + position
//                        + " count = " + parentAdapter.getCount());
                
                Intent intent = new Intent(OverviewActivity.this, DevicePageActivity.class);
                startActivity(intent);
                
            }
        });
        
        mDevicesNoneTips = findViewById(R.id.devices_none_tips);
        mDevicesViewTips = findViewById(R.id.devices_view_tips);
        mDevicesUpArrow = findViewById(R.id.devices_view_uparrow);
        mDevicesDownArrow = findViewById(R.id.devices_view_downarrow);
        mDevicesNoneTips.setVisibility(View.GONE);
        mDevicesViewTips.setVisibility(View.GONE);
//        mDevicesUpArrow.setVisibility(View.GONE);
//        mDevicesDownArrow.setVisibility(View.GONE);
        
        // init arrowUp and arrowDown
        mDevicesCount = web.length;
        if (devicesListView.getFirstVisiblePosition() > 0) {
            mDevicesUpArrow.setVisibility(View.VISIBLE);
        }
        if (devicesListView.getLastVisiblePosition() < mDevicesCount - 1) {
            mDevicesDownArrow.setVisibility(View.VISIBLE);
        }
        
        devicesListView.setOnScrollListener(devicesListViewScrollListener);
    }
    
    public void switchUser(View view) {
        ScrollLayout scrollLayout = (ScrollLayout) findViewById(R.id.main_viewflipper);
        int snapToScreenIndex = (scrollLayout.getCurScreen() == 0)? 1 : 0;
        scrollLayout.snapToScreen(snapToScreenIndex);
    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;
        private final String[] web;
        private final Integer[] imageId;

        public CustomList(Activity context, String[] web, Integer[] imageId) {
            super(context, R.layout.list_single, web);
            this.context = context;
            this.web = web;
            this.imageId = imageId;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_single, null, true);
            TextView txtTitle = (TextView) rowView
                    .findViewById(R.id.mylistitem_text_name);
            txtTitle.setText(web[position]);
            ImageButton imageButton = (ImageButton) rowView
                    .findViewById(R.id.mylistitem_button_call);
            if (position == 3) {
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
        DBHelper helper = new DBHelper(getApplicationContext());
        if (helper.query().getCount() == 0) {
            for (int i = 0; i < web.length; i++) {
                ContentValues values = new ContentValues();  
                values.put("name", web[i]);  
                values.put("ownerName", web[i]);  
                values.put("serialNumber", "11110000"); 
                values.put("phoneNumber", "18600000000");
                helper.insert(values);
            }
        }
        helper.close();
    }

}
