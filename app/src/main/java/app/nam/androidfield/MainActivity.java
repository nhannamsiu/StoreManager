package app.nam.androidfield;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    LoginFragment loginFragment;
    String TAG = "SSSS";
    //create connectInfo singleton
    NetworkHelper networkHelper = NetworkHelper.getNetworkHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check to see if current user is available
        SharedPreferences prefs = getSharedPreferences("login_data_food_app", MODE_PRIVATE);
        String token = prefs.getString("token",null);
        String actorId = prefs.getString("actorId",null);
        String expires = prefs.getString("expires",null);

        if (token!=null && actorId!=null && expires!=null){
            networkHelper.connectInfo.token = token;
            networkHelper.connectInfo.actorId = actorId;
            networkHelper.connectInfo.expires = expires;
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Welcome");
        Log.d(TAG, "onCreate: ");
        fm = getFragmentManager();
        loginFragment = new LoginFragment();
        loginFragment.setStyle(loginFragment.STYLE_NORMAL,R.style.dialogTheme);

    }

    public void startedClicked(View v){
        loginFragment.show(fm, "Login Fragment");
    }



}
