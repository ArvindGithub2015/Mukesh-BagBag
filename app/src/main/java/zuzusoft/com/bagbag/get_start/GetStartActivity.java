package zuzusoft.com.bagbag.get_start;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import locationprovider.davidserrano.com.LocationProvider;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.get_start.model.RegisterResponse;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.helper.social.GoogleLoginHelper;
import zuzusoft.com.bagbag.helper.social.SocialLoginHelper;
import zuzusoft.com.bagbag.home.HomeActivity;
import zuzusoft.com.bagbag.rest.ApiDataInterface;

/**
 * Created by mukeshnarayan on 16/11/18.
 */

public class GetStartActivity extends BaseActivity implements SocialLoginHelper.SocialLoginListeners, ApiDataInterface{

    @BindView(R.id.btnFaceBook)
    TextView btnFaceBook;
    @BindView(R.id.btnGoogle)
    TextView btnGoogle;

    private boolean isFbLogin = false;
    private CallbackManager callbackManager;
    private SocialLoginHelper socialHelper;

    private LocationProvider locationProvider;
    public static String lat, lng;
    private boolean isLatLngSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_start);

        initViews();

    }

    private void initViews() {

        //spread butter
        ButterKnife.bind(this);

        //init progress view
        initProgressView(this, R.id.progressView);

        //initialise an instance with the two required parameters
        locationProvider = new LocationProvider.Builder()
                .setContext(this)
                .setListener(callback)
                .create();

        //check location permissions
        checkLocationPermission();

        //get Firebase notification token
        getFirebaseMessageToken();

        callbackManager = CallbackManager.Factory.create();
        socialHelper = new SocialLoginHelper(this, callbackManager);

        btnFaceBook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
    }

    private void getFirebaseMessageToken() {

        String token = FirebaseInstanceId.getInstance().getToken();;

        if(token != null){

            Log.v("Token", token);

        }else{

            Log.v("Token", "NA");
        }
    }

    private void checkLocationPermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                        //permission granted good;
                        //start getting location
                        locationProvider.requestLocation();

                    }

                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {

                        //permission denied, close the app;
                        finish();

                    }

                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}

                }).check();
    }

    @Override
    protected void onResume() {
        super.onResume();

        isFbLogin = false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnFaceBook:

                isFbLogin = true;

                progressView.setRefreshing(true);
                socialHelper.loginWithFb();
                enableSocialButtons(false);

                break;


            case R.id.btnGoogle:

                isFbLogin = false;
                progressView.setRefreshing(true);
                socialHelper.loginWithGoogleStep1();
                enableSocialButtons(false);

                //gotoActivity(HomeActivity.class, null);

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(isFbLogin){

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleLoginHelper.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                socialHelper.loginWithGoogleStep2(account);
                //firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google Login Tag", "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }




    //create a callback
    LocationProvider.LocationCallback callback = new LocationProvider.LocationCallback() {
        @Override
        public void onNewLocationAvailable(float lat, float lon) {
            //location update
            Log.v("Location New", lat+", "+ lon);
            GetStartActivity.this.lat = lat+"";
            GetStartActivity.this.lng = lon+"";
            isLatLngSet = true;
            try {
                getGeoAddress((double)lat, (double)lon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void locationServicesNotEnabled() {
            //failed finding a location
        }

        @Override
        public void updateLocationInBackground(float lat, float lon) {
            //if a listener returns after the main locationAvailable callback, it will go here
            Log.v("Location In background", lat+", "+ lon);
            GetStartActivity.this.lat = lat+"";
            GetStartActivity.this.lng = lon+"";
            isLatLngSet = true;
            try {
                getGeoAddress((double)lat, (double)lon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void networkListenerInitialised() {
            //when the library switched from GPS only to GPS & network
        }
    };


    private String getGeoAddress(double latitude, double longitude) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // if available else return null

        String strAddress = address+", "+city+", "+state+", "+country+", "+postalCode;
        Log.v("Address", strAddress);

        return strAddress;

    }

    private void enableSocialButtons(boolean enable){

        btnFaceBook.setEnabled(enable);
        btnGoogle.setEnabled(enable);
    }

    @Override
    public void onSuccessFb() {

    }

    @Override
    public void onCancelFb() {

        progressView.setRefreshing(false);
        enableSocialButtons(true);
    }

    @Override
    public void onErrorFb() {

        progressView.setRefreshing(false);
        enableSocialButtons(true);
    }

    @Override
    public void onSuccessGoogle() {

    }

    @Override
    public void onErrorGoogle() {

        progressView.setRefreshing(false);
        enableSocialButtons(true);
    }

    @Override
    public void onProfileData(RegisterResponse response) {

        progressView.setRefreshing(false);
        gotoActivity(HomeActivity.class, null);
        finishAffinity();

    }
}
