package app.nam.androidfield;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    TextView tokenText;
    TextView rawText;
    NetworkHelper networkHelper = NetworkHelper.getNetworkHelper();
    static final String TAG = "sss";

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        String mTAG = "myAsyncTask";

        @Override
        protected Void doInBackground(Void... arg) {
            try{
                networkHelper.logout();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void arg) {
            super.onPostExecute(arg);
            //remove fields from sharedPref
            SharedPreferences settings = getSharedPreferences("login_data_food_app", Context.MODE_PRIVATE);
            settings.edit().remove("token").commit();
            settings.edit().remove("actorId").commit();
            settings.edit().remove("expires").commit();
            //current back stack is Main>Home, after this it will be Main instead of Main>Home>Main
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");

        //initialize views
        tokenText = findViewById(R.id.tokenText);
        rawText = findViewById(R.id.rawText);

        //set values
        tokenText.setText(networkHelper.connectInfo.token);
        rawText.setText(networkHelper.connectInfo.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOutItem:
                //do logout work
                MyAsyncTask logoutTask = new MyAsyncTask();
                logoutTask.execute();
                return true;

            case R.id.infoItem:
                //display dialog to show info about app
                FragmentManager fm = getFragmentManager();
                InfoDialog infoDialog = new InfoDialog();
                infoDialog.show(fm,"aaa");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }







}
