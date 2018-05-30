package app.nam.androidfield;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NetworkHelper {

    //initialize singleton
    private static NetworkHelper networkHelper = null;
    private NetworkHelper(){
        connectInfo =  new BravaConnectData();
        client =  new OkHttpClient();
    }

    public static NetworkHelper getNetworkHelper(){
        if (networkHelper == null){
            synchronized (NetworkHelper.class){
                if (networkHelper == null){
                    networkHelper = new NetworkHelper();
                }
            }
        }
        return networkHelper;
    }

    public class BravaConnectData
    {
        public String token;
        public String expires;
        public String actorId;
        public String user;
        public String mobile;
        /* not used */
        public ArrayList<String> roles;
        public ArrayList<String> links;

        public String toString(){
            return "token : " + token + " expires : " + expires + " actorId " + actorId;
        }

    }

    //"https://0627a3c5-9a12-41e4-8f09-4c04932a8a3e.mock.pstmn.io/auth.testserver.com/token"
    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
    public static final String AUTH_SERVER_URL_BASE    = "https://auth.network.com";
    public static final String AUTH_SERVER_URL_MOBILE =  "https://0627a3c5-9a12-41e4-8f09-4c04932a8a3e.mock.pstmn.io/auth.testserver.com/token";
    public  OkHttpClient client;
    public  BravaConnectData connectInfo;



    public void login(String email, String password) throws BravaNetworkException
    {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url( AUTH_SERVER_URL_MOBILE )
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new BravaNetworkException("Unexpected code " + response, BravaNetworkException.ErrorType.RESPONSE_FAIL);
            try {
                String jsonData = response.body().string();
                JSONObject Jobject = new JSONObject(jsonData);

                BravaConnectData cData = new BravaConnectData();

                try {
                    cData.token = Jobject.getString("token");
                } catch (JSONException e) {
                    throw new BravaNetworkException("No token in Json", BravaNetworkException.ErrorType.BAD_TOKEN);
                }

                try {
                    cData.actorId = Jobject.getString("actorId");
                } catch (JSONException e) {
                }
                try {
                    cData.expires = Jobject.getString("expires");
                } catch (JSONException e) {
                }
                connectInfo = cData;
            } catch(JSONException e){
                throw new BravaNetworkException("Bad JSON data", BravaNetworkException.ErrorType.BAD_JSON);
            } catch(IOException e){
            }
        } catch(IOException e){
        }
    }

    public BravaConnectData  getUserInfo() throws IOException {
        BravaConnectData cData = new BravaConnectData();

        Request request = new Request.Builder()
                .url(AUTH_SERVER_URL_MOBILE)
                .header("Authorization", "BRAVA-TOKEN " + connectInfo.token)
                .addHeader("Accept", "application/json; q=0.5")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new BravaNetworkException("Unexpected code " + response, BravaNetworkException.ErrorType.RESPONSE_FAIL);
            try {
                String jsonData = response.body().string();

                JSONObject Jobject = new JSONObject(jsonData);
                try {
                    cData.token = Jobject.getString("token");
                } catch (JSONException e) {
                }

                try {
                    cData.actorId = Jobject.getString("actorId");
                } catch (JSONException e) {
                }

                try {
                    cData.expires = Jobject.getString("expires");
                } catch (JSONException e) {
                }


            } catch (Exception e) {


            }

        } catch (Exception e) {


        } finally {

            return cData;
        }

    }


    public BravaConnectData  logout()  throws IOException
    {
        BravaConnectData cData = new BravaConnectData();
        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(AUTH_SERVER_URL_MOBILE)
                .header("Authorization", "BRAVA-TOKEN " + connectInfo.token)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            try {
                String jsonData = response.body().string();
                System.out.println("done here");
            }
            catch(Exception e){
                e.printStackTrace();;
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            connectInfo = null;
            return   cData;
        }

    }



}
