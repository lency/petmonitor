package cn.sevensencond.petmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DevicePageActivity extends Activity {

    private TextView mSpotName = null;
    private TextView mSpotNumber = null;
    private TextView mLastLocationTime = null;
    private TextView mLastLocationStatus = null;
    private TextView mLastLocationAddress = null;
    private TextView mSpotPhoneNumber = null;
    private TextView mSpotpageDownarrow = null;
    private TextView mSpotpageUparrow = null;
    private ListView mOperationList = null;
    private LinearLayout mLocationInfo = null;
    private LinearLayout mWaitingLinear = null;
    private ImageView mHeadImage = null;
    private ImageView mSettingImage = null;
    private ImageView mBatteryImage = null;
    private Button mButtonShare = null;
    
    private List<Map<String, Object>> getData()
    {
        ArrayList localArrayList = new ArrayList();
        HashMap localHashMap1 = new HashMap();
        localHashMap1.put("operationImg", Integer.valueOf(R.drawable.listitem_trackobject));
        localHashMap1.put("operationTitle", getString(R.string.location_label));
        localHashMap1.put("operationDesc", getString(R.string.locationdesc_label));
        localHashMap1.put("operationTips", "");
        localArrayList.add(localHashMap1);
        HashMap localHashMap2 = new HashMap();
        localHashMap2.put("operationImg", Integer.valueOf(R.drawable.listitem_history));
        localHashMap2.put("operationTitle", getString(R.string.history));
        localHashMap2.put("operationDesc", getString(R.string.history_desc));
        localHashMap2.put("operationTips", "");
        localArrayList.add(localHashMap2);
        HashMap localHashMap3 = new HashMap();
        localHashMap3.put("operationImg", Integer.valueOf(R.drawable.listitem_fence));
        localHashMap3.put("operationTitle", getString(R.string.fence));
        localHashMap3.put("operationDesc", getString(R.string.fence_desc));
        localHashMap3.put("operationTips", getString(R.string.fencein));
        localArrayList.add(localHashMap3);
        
        HashMap localHashMap9 = new HashMap();
        localHashMap9.put("operationImg", Integer.valueOf(R.drawable.listitem_tracking));
        localHashMap9.put("operationTitle", getString(R.string.following));
        localHashMap9.put("operationDesc", getString(R.string.follow_desc));
        localHashMap9.put("operationTips", "");
        localArrayList.add(localHashMap9);
        
        HashMap localHashMap7 = new HashMap();
        localHashMap7.put("operationImg", Integer.valueOf(R.drawable.listitem_trackormode));
        localHashMap7.put("operationTitle", getString(R.string.trackormode));
        localHashMap7.put("operationDesc", getString(R.string.trackormode_desc));
        localHashMap7.put("operationTips", "");
        localArrayList.add(localHashMap7);

        HashMap localHashMap8 = new HashMap();
        localHashMap8.put("operationImg", Integer.valueOf(R.drawable.listitem_sos));
        localHashMap8.put("operationTitle", getString(R.string.trackorsos));
        localHashMap8.put("operationDesc", getString(R.string.sossetting));
        localHashMap8.put("operationTips", "");
        localArrayList.add(localHashMap8);
        
        HashMap localHashMap4 = new HashMap();
        localHashMap4.put("operationImg", Integer.valueOf(R.drawable.listitem_gps));
        localHashMap4.put("operationTitle", getString(R.string.gps_status));
        localHashMap4.put("operationDesc", getString(R.string.gps_status_desc));
        localHashMap4.put("operationTips", getString(R.string.gps_enable));
        localArrayList.add(localHashMap4);
        
//        HashMap localHashMap5 = new HashMap();
//        localHashMap5.put("operationImg", Integer.valueOf(R.drawable.listitem_deletetrackor));
//        localHashMap5.put("operationTitle", getString(R.string.cancel_share));
//        localHashMap5.put("operationDesc", getString(R.string.cancel_share_desc));
//        localHashMap5.put("operationTips", "");
//        localArrayList.add(localHashMap5);
        HashMap localHashMap6 = new HashMap();
        localHashMap6.put("operationImg", Integer.valueOf(R.drawable.listitem_deletetrackor));
        localHashMap6.put("operationTitle", getString(R.string.delete_label));
        localHashMap6.put("operationDesc", getString(R.string.deletedesc_label));
        localHashMap6.put("operationTips", "");
        localArrayList.add(localHashMap6);
        
        return localArrayList;
    }
    
    View.OnClickListener clickListener = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.spotpage_text_name)
            {
                Log.d("Device", "start device setting");
                Intent localIntent3 = new Intent(DevicePageActivity.this, DeviceSettingActivity.class);
//                localIntent3.putExtra("com.bigbangtech.whereru.clip", true);
//                localIntent3.putExtra("com.bigbangtech.whereru.trackerserial", SpotPage.this.curTracker.serialNumber);
                startActivity(localIntent3);
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DevicePage", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devicepage);
        
        this.mSpotName = ((TextView)findViewById(R.id.spotpage_text_name));
        this.mSpotName.setOnClickListener(this.clickListener);
        this.mSpotNumber = ((TextView)findViewById(R.id.spotpage_text_number));
        this.mLastLocationTime = ((TextView)findViewById(R.id.last_location_time));
        this.mLastLocationStatus = ((TextView)findViewById(R.id.last_location_status));
        this.mLastLocationAddress = ((TextView)findViewById(R.id.last_location_address));
        this.mSpotPhoneNumber = ((TextView)findViewById(R.id.spotpage_phone_number));
        this.mSpotpageDownarrow = ((TextView)findViewById(R.id.spotpage_downarrow));
        this.mSpotpageUparrow = ((TextView)findViewById(R.id.spotpage_uparrow));
        this.mOperationList = ((ListView)findViewById(R.id.spotpage_listview_operation));
        this.mLocationInfo = ((LinearLayout)findViewById(R.id.devicepage_linear_locationInfo));
        this.mWaitingLinear = ((LinearLayout)findViewById(R.id.devicepage_linear_getLocation));
        this.mHeadImage = ((ImageView)findViewById(R.id.spotpage_image_head));
        this.mSettingImage = ((ImageView)findViewById(R.id.spotpage_personal_setting));
//        this.mHeadImage.setOnClickListener(this.clickListener);
//        this.mSettingImage.setOnClickListener(this.clickListener);
        this.mBatteryImage = ((ImageView)findViewById(R.id.spotpage_image_battery));
        this.mButtonShare = ((Button)findViewById(R.id.spotpage_share));
//        this.mButtonShare.setOnClickListener(this.clickListener);
        Bundle localBundle = getIntent().getBundleExtra("cn.sevensencond.petmonitor.devicePage");
        String deviceName = localBundle.getString("cn.sevensencond.petmonitor.devicename");
        mSpotName.setText(deviceName);
        
        mLocationInfo.setVisibility(View.VISIBLE);
        mWaitingLinear.setVisibility(View.GONE);
        
        // stub text first
//        mSpotName.setText("075");
        mSpotPhoneNumber.setText("18800000000");
        mSpotNumber.setText("18600000000");
        mLastLocationTime.setText("2012-12-21 00:00:00");
        mLastLocationStatus.setText("基站定位");
        mLastLocationAddress.setText("上地");
        
        List localList = getData();
        int i = R.layout.operationlistitem;
        String[] arrayOfString = { "operationImg", "operationTitle", "operationDesc", "operationTips" };
        int[] arrayOfInt = new int[4];
        arrayOfInt[0] = R.id.operationlisteitem_img_icon;
        arrayOfInt[1] = R.id.operationlisteitem_text_title;
        arrayOfInt[2] = R.id.operationlisteitem_text_info;
        arrayOfInt[3] = R.id.operationlisteitem_text_tip;
        SimpleAdapter localSimpleAdapter = new SimpleAdapter(this, localList, i, arrayOfString, arrayOfInt);
        this.mOperationList.setAdapter(localSimpleAdapter);

    }

}
