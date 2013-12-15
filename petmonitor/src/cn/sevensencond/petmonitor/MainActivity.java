package cn.sevensencond.petmonitor;

import cn.sevensencond.petmonitor.MapActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		final petserver server = new petserver();
		server.check_login(new serverHandle(){
			@Override
			public void onSuccess(String result)
			{
				petserver.userinfo ui = server.ui;
				Log.v("a", ui.userMail);
				setContentView(R.layout.activity_main);
				
				server.getuserinfo(new serverHandle(){
					@Override
					public void onSuccess(String result)
					{
						Log.v("server","getuserinfo ok");
					}
					@Override
					public void onFailed(String result)
					{
						
					}
				});
				
			}
			@Override
			public void onFailed(String result)
			{
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendMessage(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
	
	public void callOverview(View view) {
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }
}
