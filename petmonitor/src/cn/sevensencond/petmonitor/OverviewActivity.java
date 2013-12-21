package cn.sevensencond.petmonitor;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;
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
    
    private ImageView mImgNavDev = null;
    private ImageView mImgNavFriend = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);
        
        mImgNavDev = (ImageView)findViewById(R.id.imgnav_dev);
        mImgNavFriend = (ImageView)findViewById(R.id.imgnav_friend);

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

        CustomList adapter = new CustomList(OverviewActivity.this, web, imageId);
        devicesListView = (ListView) findViewById(R.id.main_listview_devices);
        devicesListView.setAdapter(adapter);
        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(OverviewActivity.this,
                        "You Clicked at " + web[+position], Toast.LENGTH_SHORT)
                        .show();
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

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.overview, menu);
    // return true;
    // }

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

}
