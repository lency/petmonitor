package cn.sevensencond.petmonitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cn.sevensencond.petmonitor.ScrollLayout.OnScreenChangeListener;
import cn.sevensencond.petmonitor.ScrollLayout.OnScreenChangeListenerDataLoad;

public class OverviewActivity extends Activity {

    ListView list = null;
    String[] web = { "Google Plus", "Twitter", "Windows", "Bing", "Sun", "Oracle" };
    Integer[] imageId = { R.drawable.smallheader, R.drawable.smallheader,
            R.drawable.smallheader, R.drawable.smallheader, R.drawable.smallheader, R.drawable.smallheader};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        ScrollLayout scrollLayout = (ScrollLayout)findViewById(R.id.main_viewflipper);
        scrollLayout.setOnScreenChangeListener(new OnScreenChangeListener() {
            
            @Override
            public void onScreenChange(int currentIndex) {
                // TODO Auto-generated method stub
                
            }
        });
        scrollLayout.setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {
            @Override
            public void onScreenChange(int currentIndex) {
                
            }
        });
//        CustomList adapter = new CustomList(OverviewActivity.this, web, imageId);
//        list = (ListView) findViewById(R.id.main_listview_devices);
//        list.setAdapter(adapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                    int position, long id) {
//                Toast.makeText(OverviewActivity.this,
//                        "You Clicked at " + web[+position], Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });
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
            TextView txtTitle = (TextView) rowView.findViewById(R.id.mylistitem_text_name);
            txtTitle.setText(web[position]);
            ImageButton imageButton = (ImageButton) rowView.findViewById(R.id.mylistitem_button_call);
            if (position == 3) {
                imageButton.setImageResource(R.drawable.locator_phone_disable);
            }
            return rowView;
        }
    }

}
