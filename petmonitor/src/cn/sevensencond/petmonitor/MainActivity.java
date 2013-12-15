package cn.sevensencond.petmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends Activity {

    final private petserver server = new petserver();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        server.check_login(new serverHandle() {
            @Override
            public void onSuccess(String result) {
                setContentView(R.layout.login);
                return;
                
//                petserver.userinfo ui = server.ui;
//                Log.v("a", ui.userMail);
//                setContentView(R.layout.activity_main);
//
//                server.getuserinfo(new serverHandle() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.v("server", "getuserinfo ok");
//                    }
//
//                    @Override
//                    public void onFailed(String result) {
//
//                    }
//                });

            }

            @Override
            public void onFailed(String result) {
                setContentView(R.layout.login);
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
    
    public void onLogin(View view) {
        EditText editText = (EditText)findViewById(R.id.login_edit_userName);
        String name = editText.getText().toString();
        editText = (EditText)findViewById(R.id.login_edit_password);
        String pass = editText.getText().toString();
        Log.v("Login", "name is: " + name + " pass is: " + pass);
        final MainActivity contentActivity = this;
        server.login(name, pass, new serverHandle() {
            
            @Override
            public void onSuccess(String responseText) {
                // TODO Auto-generated method stub
                Log.v("Login", "Got login success:" + responseText);
                Gson gson =  new Gson();
                LoginResponse loginResponse = gson.fromJson(responseText, LoginResponse.class);
                if (loginResponse.success == 0) {
                    Intent intent = new Intent(contentActivity, OverviewActivity.class);
                    startActivity(intent);
                } else {
                    onFailed(responseText);
                }
                
            }
            
            @Override
            public void onFailed(String responseText) {
                // TODO Auto-generated method stub
                Log.v("Login", "Got login failed:" + responseText);
                
            }
        });
    }
    
    public class LoginResponse {
        public int success;
    }
}
