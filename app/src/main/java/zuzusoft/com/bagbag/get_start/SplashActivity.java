package zuzusoft.com.bagbag.get_start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.home.HomeActivity;

/**
 * Created by mukeshnarayan on 17/12/18.
 */

public class SplashActivity extends BaseActivity {

    // Splash screen timer
    public static final String KEY_BUNDLE_NOTIFICATION_DATA = "notification_data";
    private static int SPLASH_TIME_OUT = 2000;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(this);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if(sessionManager.isLoggedIn()){

                    // If a notification message is tapped, any data accompanying the notification
                    // message is available in the intent extras. In this sample the launcher
                    // intent is fired when the notification is tapped, so any accompanying data would
                    // be handled here. If you want a different intent fired, set the click_action
                    // field of the notification message to the desired intent. The launcher intent
                    // is used when no click_action is specified.
                    //
                    // Handle possible data accompanying notification message.
                    // [START handle_data_extras]
                    /*if (getIntent().getExtras() != null) {
                        for (String key : getIntent().getExtras().keySet()) {
                            Object value = getIntent().getExtras().get(key);
                            Log.d("Splash", "Key: " + key + " Value: " + value);
                        }
                    }*/
                    // [END handle_data_extras]

                    //Bundle args = new Bundle();
                    //args.putBundle(KEY_BUNDLE_NOTIFICATION_DATA, args);
                    gotoActivity(HomeActivity.class, null);

                }else{

                    Intent i = new Intent(context, GetStartActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();

            }

        }, SPLASH_TIME_OUT);
    }
}
