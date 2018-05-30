package app.nam.androidfield;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginFragment extends DialogFragment {

    EditText email;
    EditText password;
    static final String TAG = "SSS";
    NetworkHelper networkHelper;

    public LoginFragment() {
        // Required empty public constructor
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        String mTAG = "myAsyncTask";

        @Override
        protected Void doInBackground(Void... arg) {
            try{
                networkHelper.login(email.getText().toString(),password.getText().toString());
            } catch (BravaNetworkException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void arg) {
            super.onPostExecute(arg);
            //store data of networkHelper.connectInfo to sharedPref
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("login_data_food_app",Context.MODE_PRIVATE).edit();
            editor.putString("token", networkHelper.connectInfo.token);
            editor.putString("actorId", networkHelper.connectInfo.actorId);
            editor.putString("expires",networkHelper.connectInfo.expires);
            editor.apply();

            //start activity
            Intent intent = new Intent(getActivity(),HomeActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkHelper = networkHelper.getNetworkHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        //init views
        email = v.findViewById(R.id.emailEdit);
        password = v.findViewById(R.id.passwordEdit);
        Button signInBtn = v.findViewById(R.id.signInBtn);
        TextView forgotText = v.findViewById(R.id.forgotText);
        TextView createAccountText = v.findViewById(R.id.createAccountText);

        //set default texts
        email.setText("mail@gmail.com");
        password.setText("PASSWORD");

        //assign listeners
        signInBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAsyncTask loginTask = new MyAsyncTask();
                loginTask.execute();
            }
        });


        forgotText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //lead to forgot
                Intent intent = new Intent(getActivity(),ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        createAccountText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //lead to sign up
                Intent intent = new Intent(getActivity(),SignUpActivity.class);
                startActivity(intent);
            }
        });

        setDialogLayout();

        return v;
    }

    public void setDialogLayout(){
        //set dialog at bottom
        WindowManager.LayoutParams  params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM|Gravity.CENTER;
        getDialog().getWindow().setAttributes( params);
        //set animation
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogTheme;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        //set inteface object = null
    }



}
