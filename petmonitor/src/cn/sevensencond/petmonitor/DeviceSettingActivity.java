package cn.sevensencond.petmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DeviceSettingActivity extends Activity {

    private Button mButtonBack = null;
    private ListView mSettingList = null;
    
    private List<Map<String, Object>> getSettingList()
    {
        ArrayList localArrayList = new ArrayList();
        HashMap localHashMap1 = new HashMap();
        localHashMap1.put("leftlabelstring", getString(R.string.locator_control) + ":");
        localHashMap1.put("rightlabelstring", "18600000000");
        localHashMap1.put("rightimage", null);
        localArrayList.add(localHashMap1);
        HashMap localHashMap2 = new HashMap();
        localHashMap2.put("leftlabelstring", getString(R.string.locator_serial) + ":");
        localHashMap2.put("rightlabelstring", "201212210000");
        localHashMap2.put("rightimage", null);
        localArrayList.add(localHashMap2);
        HashMap localHashMap3 = new HashMap();
        localHashMap3.put("leftlabelstring", getString(R.string.locator_number) + ":");
        localHashMap3.put("rightlabelstring", "18800000000");
        localHashMap3.put("rightimage", Integer.valueOf(R.drawable.arrow_right));
        localArrayList.add(localHashMap3);
        HashMap localHashMap4 = new HashMap();
        localHashMap4.put("leftlabelstring", getString(R.string.locator_name) + ":");
        localHashMap4.put("rightlabelstring", "075");
        localHashMap4.put("rightimage", Integer.valueOf(R.drawable.arrow_right));
        localArrayList.add(localHashMap4);
        HashMap localHashMap5 = new HashMap();
        localHashMap5.put("leftlabelstring", getString(R.string.advancesetting));
        localHashMap5.put("rightlabelstring", "");
        localHashMap5.put("rightimage", Integer.valueOf(R.drawable.arrow_right));
        localArrayList.add(localHashMap5);
        return localArrayList;
    }
    
    private void refreshListView()
    {
        List localList = getSettingList();
        int i = R.layout.settingitem;
        String[] arrayOfString = { "leftlabelstring", "rightlabelstring", "rightimage" };
        int[] arrayOfInt = new int[3];
        arrayOfInt[0] = R.id.settingitem_text_left;
        arrayOfInt[1] = R.id.settingitem_text_right;
        arrayOfInt[2] = R.id.settingitem_image;
        SimpleAdapter localSimpleAdapter = new SimpleAdapter(this, localList, i, arrayOfString, arrayOfInt);
        mSettingList.setAdapter(localSimpleAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DeviceSetting", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        
        mSettingList = ((ListView)findViewById(R.id.setting_listview));
//        mSettingList.setOnItemClickListener(this.listener);
        mButtonBack = ((Button)findViewById(R.id.setting_back));
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.device_setting, menu);
//        return true;
//    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        refreshListView();
    }
}
