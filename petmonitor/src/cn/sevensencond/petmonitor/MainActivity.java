package cn.sevensencond.petmonitor;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		petserver server = new petserver();
		server.check_login(new serverHandle(){
			@Override
			public void onSuccess(petserver server)
			{
				petserver.userinfo ui = server.ui;
				Log.v("a", ui.userMail);
				setContentView(R.layout.welcome);
				
				server.getuserinfo(new serverHandle(){
					@Override
					public void onSuccess(petserver server)
					{
						Log.v("server","getuserinfo ok");
					}
					@Override
					public void onFailed(petserver server)
					{
						
					}
				});
				
			}
			@Override
			public void onFailed(petserver server)
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

}
