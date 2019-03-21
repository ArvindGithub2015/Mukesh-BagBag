package zuzusoft.com.bagbag.helper.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.rest.ApiClient;
import zuzusoft.com.bagbag.rest.ApiInterface;


/**
 * Created by mukeshnarayan on 19/05/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    private SessionManager sessionManager;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        sessionManager = new SessionManager(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        MknUtils.saveToken(this, MknUtils.READ_TOKEN, refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    public void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);


        if(ApiClient.isNetworkAvailable(this)){

            //makeAPiCall(token, this);

        }else{

            //Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }


    public void sendRegistrationToServer1(Context context) {
        // sending gcm token to server
        // Log.e(TAG, "From preferance: " + getToken());


        if(ApiClient.isNetworkAvailable(context) && MknUtils.readToken(context, MknUtils.READ_TOKEN, null) != null ){

            makeAPiCall(MknUtils.readToken(context, MknUtils.READ_TOKEN, null), context);

        }else{

            //Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }


    private void makeAPiCall(String token, Context context) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog


        String URL = "http://quazar.club/mkn/rest.php?apicall=usertoken";

        Log.v("URL", URL);

        String userId = sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID);

       /* Call<LoginResponse> call = apiService.updateUserToken(URL, token, userId);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());


                    } else {


                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                //hide progress dialog
                t.printStackTrace();

            }
        });*/

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }


}
