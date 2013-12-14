package cn.sevensencond.petmonitor;

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
		         //handle.onSuccess(self);
		     }
		     
		     @Override
		     public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable error) {
		    	 Log.v("SERVER", "error when login");
		    	 //handle.onFailed(self);
		     }
		 });
	}
	public void getuserinfo(final serverHandle handle){
		action act = gson.fromJson(gson.toJson(sess), action.class);
		act.action = "getuserinfo";

		RequestParams params = new RequestParams();
		params.put("posts", gson.toJson(act));
		params.put("action", act.action);
		
		sendRequest(params, handle);

	}
	
	public void login(String username, String password, serverHandle handle)
	{
		RequestParams params = new RequestParams();
		params.put("posts", "{\"userMail\":\""+username+
				"\",\"password\":\""+password+
				"\",\"platForm\":\""+"Android"+
				"\",\"action\":\""+"login"+
				"\"}");
		params.put("action", "login");
		
		sendRequest(params, handle);		
	}
	
	public void newuser(String username, String mobile, String password, serverHandle handle)
	{
		RequestParams params = new RequestParams();
		params.put("posts", "{\"userMail\":\""+username+
				"\",\"Mobile\":\""+mobile+
				"\",\"password\":\""+password+
				"\",\"platForm\":\""+"Android"+
				"\",\"action\":\""+"register"+
				"\"}");
		params.put("action", "register");
		
		sendRequest(params, handle);
	}
	
	public void lastPos(serverHandle handle)
	{
		RequestParams params = new RequestParams();
		params.put("posts", "{\"userID\":\""+sess.userID+
				"\",\"sessionID\":\""+sess.sessionID+

				"\",\"platForm\":\""+"Android"+
				"\",\"action\":\""+"location"+
				"\"}");
		params.put("action", "location");
		
		sendRequest(params, handle);
	}
	
	public void bindDevice(String sn, String devicePassword, serverHandle handle)
	{
		RequestParams params = new RequestParams();
		params.put("posts", "{\"sn\":\""+sn+
				"\",\"password\":\""+devicePassword+
				"\",\"userID\":\""+sess.userID+
				"\",\"sessionID\":\""+sess.sessionID+

				"\",\"platForm\":\""+"Android"+
				"\",\"action\":\""+"bind"+
				"\"}");
		params.put("action", "bind");
		
		sendRequest(params, handle);
	}
	
	private void sendRequest(RequestParams params, final serverHandle handle)
	{
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(SERVER, params, new TextHttpResponseHandler() {
			@Override
		     public void onSuccess(int statusCode, Header[] headers, String responseBody) {
		         Log.v("SERVER", responseBody);
		         handle.onSuccess(responseBody);
		     }
		     
		     @Override
		     public void onFailure(int statusCode, Header[] headers, String responseBody, java.lang.Throwable error) {
		    	 Log.v("SERVER", "error");
		    	 handle.onFailed(responseBody);
		     }
		});
	}
}

abstract class serverHandle
{
	public abstract void onSuccess(String server);
	public abstract void onFailed(String server);
}