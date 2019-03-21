package zuzusoft.com.bagbag.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import zuzusoft.com.bagbag.auth.LoginActivity;
import zuzusoft.com.bagbag.get_start.GetStartActivity;

/**
 * Created by edu on 12/4/17.
 */

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "bagbag";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    //Roll no
    public static final String KEY_USER_ID = "id";

    public static final String KEY_NAME = "name";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_PROFILE_IMAGE = "profile_image";

    public static final String KEY_PHONE_NUMBER = "phone";

    public static final String KEY_PUBLIC = "public";

    public static final String KEY_PRIVATE = "private";

    public static final String KEY_SOCIAL_ID = "social_id";

    public static final String KEY_SOCIAL_PROVIDER = "social_provider";

    public static final String KEY_FCM_TOKEN = "token";

    public static final String KEY_GENDER = "gender";

    public static final String KEY_LAT_LAN = "latlan";

    public static final String KEY_CURRENCY = "currency";

    public static final String KEY_CERTIFICATE = "certificate";

    public static final String KEY_PAID_EXAM = "paid_exam";

    public static final String KEY_ADMISSION_DATE = "admission_date";

    public static final String KEY_EXAM_EXPIRY = "exam_expiry";

    public static final String KEY_ADDRESS = "key_address";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(HashMap<String, String> userInfo){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, userInfo.get(KEY_NAME));

        // Storing email in pref
        editor.putString(KEY_EMAIL, userInfo.get(KEY_EMAIL));

        // Storing id in pref
        editor.putString(KEY_USER_ID, userInfo.get(KEY_USER_ID));

        // Storing account blance in pref
        editor.putString(KEY_SOCIAL_ID, userInfo.get(KEY_SOCIAL_ID));

        // Storing profile image in pref
        editor.putString(KEY_PROFILE_IMAGE, userInfo.get(KEY_PROFILE_IMAGE));

        // Storing phone in pref
        editor.putString(KEY_PHONE_NUMBER, userInfo.get(KEY_PHONE_NUMBER));

        // Storing tags in pref
        editor.putString(KEY_SOCIAL_PROVIDER, userInfo.get(KEY_SOCIAL_PROVIDER));

        // Storing public key in pref
        editor.putString(KEY_PUBLIC, userInfo.get(KEY_PUBLIC));

        // Storing private key in pref
        editor.putString(KEY_PRIVATE, userInfo.get(KEY_PRIVATE));

        // Storing currency key in pref
        editor.putString(KEY_CURRENCY, userInfo.get(KEY_CURRENCY));

        // Storing certificate key in pref
        editor.putString(KEY_CERTIFICATE, userInfo.get(KEY_CERTIFICATE));

        // Storing paid ibps key in pref
        editor.putString(KEY_PAID_EXAM, userInfo.get(KEY_PAID_EXAM));

        // Storing paid ibps key in pref
        editor.putString(KEY_FCM_TOKEN, userInfo.get(KEY_FCM_TOKEN));

        // Storing paid ibps key in pref
        editor.putString(KEY_GENDER, userInfo.get(KEY_GENDER));

        // Storing paid ibps key in pref
        editor.putString(KEY_ADMISSION_DATE, userInfo.get(KEY_ADMISSION_DATE));

        // Storing paid ibps key in pref
        editor.putString(KEY_LAT_LAN, userInfo.get(KEY_LAT_LAN));
        // Storing paid ibps key in pref
        editor.putString(KEY_ADDRESS, userInfo.get(KEY_ADDRESS));

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user id
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

        // user account blance
        user.put(KEY_SOCIAL_PROVIDER, pref.getString(KEY_SOCIAL_PROVIDER, null));

        // user profile image
        user.put(KEY_PROFILE_IMAGE, pref.getString(KEY_PROFILE_IMAGE, null));

        // user password
        user.put(KEY_SOCIAL_ID, pref.getString(KEY_SOCIAL_ID, null));

        // user phone
        user.put(KEY_PHONE_NUMBER, pref.getString(KEY_PHONE_NUMBER, null));


        // Storing public key in pref
        user.put(KEY_PUBLIC, pref.getString(KEY_PUBLIC, null));

        // Storing private key in pref
        user.put(KEY_PRIVATE, pref.getString(KEY_PRIVATE, null));


        // Storing private key in pref
        user.put(KEY_CURRENCY, pref.getString(KEY_CURRENCY, null));

        // Storing private key in pref
        user.put(KEY_CERTIFICATE, pref.getString(KEY_CERTIFICATE, null));

        // Storing private key in pref
        user.put(KEY_PAID_EXAM, pref.getString(KEY_PAID_EXAM, null));

        // Storing private key in pref
        user.put(KEY_GENDER, pref.getString(KEY_GENDER, null));

        // Storing private key in pref
        user.put(KEY_FCM_TOKEN, pref.getString(KEY_FCM_TOKEN, null));

        // Storing private key in pref
        user.put(KEY_ADMISSION_DATE, pref.getString(KEY_ADMISSION_DATE, null));

        // Storing private key in pref
        user.put(KEY_LAT_LAN, pref.getString(KEY_LAT_LAN, null));

        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(boolean clearAppData){

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, GetStartActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        i.getBooleanExtra("IS_FROM_SPLASH", false);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
