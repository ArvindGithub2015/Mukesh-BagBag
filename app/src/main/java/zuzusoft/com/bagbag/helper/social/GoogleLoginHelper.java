package zuzusoft.com.bagbag.helper.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.get_start.GetStartActivity;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.rest.ApiDataInflation;

/**
 * Created by mukeshnarayan on 05/12/18.
 */

public class GoogleLoginHelper {

    public static final int RC_SIGN_IN = 9001;
    private final String TAG = "Google_login";

    private Activity activity;

    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    private SocialLoginHelper.SocialLoginListeners mListener;


    public GoogleLoginHelper(Activity activity){

        this.mListener = (SocialLoginHelper.SocialLoginListeners) activity;
        this.activity = activity;

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestIdToken("269519596117-74o7aiffov8bgp5p9cqp44da8n33otnh.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    public void loginGoogle(){

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    // [START auth_with_google]
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]

        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.v("Name: ", user.getProviderData().get(0).getDisplayName());
                            Log.v("Email: ", user.getEmail());
                            String photo = user.getProviderData().get(0).getPhotoUrl().toString();
                            Log.v("Photo: ", photo);
                            Log.v("uid: ", user.getUid());
                            //Log.v("Name: ", user.getPhoneNumber());


                            final HashMap<String, String> dataSet = new HashMap<>();
                            dataSet.put(SessionManager.KEY_SOCIAL_ID, user.getUid());
                            dataSet.put(SessionManager.KEY_NAME, user.getDisplayName());
                            dataSet.put(SessionManager.KEY_EMAIL, user.getEmail());
                            dataSet.put(SessionManager.KEY_PROFILE_IMAGE, photo);
                            dataSet.put(SessionManager.KEY_PHONE_NUMBER, "000000000");
                            dataSet.put(SessionManager.KEY_SOCIAL_PROVIDER, "Google");
                            dataSet.put(SessionManager.KEY_FCM_TOKEN, getFirebaseMessageToken());
                            dataSet.put(SessionManager.KEY_GENDER, "Female");
                            dataSet.put(SessionManager.KEY_LAT_LAN, GetStartActivity.lat+","+GetStartActivity.lng);

                            //Add user to server
                            final ApiDataInflation apiHelper = new ApiDataInflation(activity);

                            Glide.with(activity)
                                    .asBitmap()
                                    .load((photo.contains("https://"))?photo:"https://lh3.googleusercontent.com"+photo)
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                                            if(resource != null){

                                                Log.v("Profile Image", resource.getByteCount()+"");
                                                apiHelper.getUserProfile(dataSet, resource);

                                            }else{

                                                Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),
                                                        R.drawable.profile);
                                                apiHelper.getUserProfile(dataSet, bitmap);
                                                Log.v("Profile Image", "profile image is null");
                                            }

                                        }
                                    });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if(mListener != null){

                                mListener.onErrorGoogle();

                            }
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

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


    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //updateUI(null);

                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //updateUI(null);

                    }
                });
    }


}
