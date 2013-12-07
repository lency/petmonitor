package cn.sevensencond.petmonitor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.Header;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.*;


public class petserver {
	class userinfo {
		String userMail;
		public String password;
	}
	class session {
		public String userID;
		public String sessionID;
	}
	class device {
		public String name;
		public String deviceId;
	}
	
	class action extends session {
		public String action;
	}
	
	public userinfo ui;
	public session sess;
	
	private final Gson gson = new Gson();
	private final static String SERVER = "http://42.96.138.166:8081/ci/app/api/";
	private static String token = "";
	private static String buildUrl(String x)
	{
		return SERVER;
	}
	public void check_login(final serverHandle handle)
	{
		//Get User Information from Cache file
		ui = gson.fromJson("{\"a\":\"b\",\"userMail\":\"chenxing168983@126.com\",\"password\":\"123456\"}", 
				userinfo.class);//new userinfo();
		ui.userMail = "chenxing168983@126.com";
		ui.password = "123456";
		//ui.userId = "u";
		//ui.sessionId = "s";
		//String posts="posts="+gson.toJson(ui)+"&action=login";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("posts", gson.toJson(ui));
		params.put("action", "login");
		final petserver self = this;
		client.post(SERVER, params, new TextHttpResponseHandler() {
		     @Override
		     public void onSuccess(int statusCode, Header[] headers, String responseBody) {
		         System.out.println(responseBody);
		         self.sess = gson.fromJson(responseBody, session.class);
		         handle.onSuccess(self);
		     }
		     
		     @Override
		     public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable error) {
		    	 Log.v("SERVER", "error when login");
		    	 handle.onFailed(self);
		     }
		 });
	}
	public void getuserinfo(final serverHandle handle){
		action act = gson.fromJson(gson.toJson(sess), action.class);
		act.action = "getuserinfo";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("posts", gson.toJson(act));
		params.put("action", act.action);
		final petserver self = this;
		client.post(SERVER, params, new TextHttpResponseHandler() {
			@Override
		     public void onSuccess(int statusCode, Header[] headers, String responseBody) {
		         System.out.println(responseBody);
		         handle.onSuccess(self);
		     }
		     
		     @Override
		     public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable error) {
		    	 Log.v("SERVER", "error when login");
		    	 handle.onFailed(self);
		     }
		});
	}
}

abstract class serverHandle
{
	public abstract void onSuccess(petserver server);
	public abstract void onFailed(petserver server);
}