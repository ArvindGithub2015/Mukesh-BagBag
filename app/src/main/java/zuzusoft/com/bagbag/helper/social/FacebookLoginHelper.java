package zuzusoft.com.bagbag.helper.social;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.RequestBody;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.get_start.GetStartActivity;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.rest.ApiDataInflation;

/**
 * Created by mukeshnarayan on 05/12/18.
 */

public class FacebookLoginHelper {

    private SocialLoginHelper.SocialLoginListeners mListener;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private Activity activity;

    public FacebookLoginHelper(Activity activity, CallbackManager callbackManager){

        this.mListener = (SocialLoginHelper.SocialLoginListeners) activity;
        this.activity = activity;
        this.callbackManager = callbackManager;
        loginManager = LoginManager.getInstance();

        //register callback
        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        getUserProfile(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        if(mListener != null){

                            mListener.onCancelFb();

                        }

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        if(mListener != null){

                            mListener.onErrorFb();

                        }
                    }
                });
    }

    public void loginFb(){

        loginManager.logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));
    }


    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {

                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            final URL profilePicture = new URL(image_url);

                            final HashMap<String, String> dataSet = new HashMap<>();
                            dataSet.put(SessionManager.KEY_SOCIAL_ID, id);
                            dataSet.put(SessionManager.KEY_NAME, first_name+" "+last_name);
                            dataSet.put(SessionManager.KEY_EMAIL, email);
                            dataSet.put(SessionManager.KEY_PROFILE_IMAGE, profilePicture.toString());
                            dataSet.put(SessionManager.KEY_PHONE_NUMBER, "000000000");
                            dataSet.put(SessionManager.KEY_SOCIAL_PROVIDER, "Facebook");
                            dataSet.put(SessionManager.KEY_FCM_TOKEN, getFirebaseMessageToken());
                            dataSet.put(SessionManager.KEY_GENDER, "Female");
                            dataSet.put(SessionManager.KEY_LAT_LAN, GetStartActivity.lat+","+GetStartActivity.lng);

                            //Add user to server
                            final ApiDataInflation apiHelper = new ApiDataInflation(activity);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {

                                        HttpsURLConnection conn1 = (HttpsURLConnection) profilePicture.openConnection();
                                        HttpsURLConnection.setFollowRedirects(true);
                                        conn1.setInstanceFollowRedirects(true);
                                        final Bitmap fb_img = BitmapFactory.decodeStream(conn1.getInputStream());

                                        //save it in temp file

                                        if(fb_img != null){

                                            Log.v("Profile Image", fb_img.getByteCount()+"");
                                            apiHelper.getUserProfile(dataSet, fb_img);

                                        }else{

                                            //get drawable in byte
                                            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
                                                    R.drawable.profile);
                                            apiHelper.getUserProfile(dataSet, bitmap);
                                            Log.v("Profile Image", "profile image is null");
                                        }


                                        //apiHelper.getUserProfile(dataSet, fb_img);

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).start();


                            Log.v("First Name", first_name);
                            Log.v("Last Name", last_name);
                            Log.v("Email", email);
                            Log.v("Id", id);
                            Log.v("Image Url", image_url);

                            //txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                           // txtEmail.setText(email);
                           // Picasso.with(MainActivity.this).load(image_url).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private String getFirebaseMessageToken() {

        String token = FirebaseInstanceId.getInstance().getToken();;

        if(token != null){

            Log.v("Token", token);
            return token;

        }else{

            Log.v("Token", "NA");
            return "NA";
        }
    }

}
