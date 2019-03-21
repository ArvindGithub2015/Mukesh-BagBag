package zuzusoft.com.bagbag.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.PetitionReceivedDialog;
import zuzusoft.com.bagbag.closet.model.Response;
import zuzusoft.com.bagbag.get_start.model.RegisterResponse;
import zuzusoft.com.bagbag.helper.notification.Config;
import zuzusoft.com.bagbag.helper.notification.FCMPayload;
import zuzusoft.com.bagbag.helper.notification.NotificationUtils;
import zuzusoft.com.bagbag.rest.ApiDataInterface;


/**
 * Created by mukeshnarayan on 14/06/18.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener, ApiDataInterface {

    public static final String KEY_MAIN_BUNDLE = "main_bundle";
    public static boolean IS_FRAGMENT_UPDATE = false;
    public static int FRAGMENT_INDEX = 0;

    public SwipeRefreshLayout progressView;
    public SessionManager sessionManager;

    public Context context;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("");
            Log.d("Received data : ", data);
        }
    };

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        sessionManager = new SessionManager(context);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // check session
                    if (sessionManager.isLoggedIn()) {

                        FCMPayload payload = (FCMPayload) intent.getSerializableExtra("payload");
                        PetitionReceivedDialog prd = new PetitionReceivedDialog();
                        Bundle args = new Bundle();
                        args.putSerializable("payload", payload);
                        prd.setArguments(args);
                        prd.show(getSupportFragmentManager(), "PRD");
                    }

                }
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void initProgressView(Activity activity, int resId) {

        progressView = (SwipeRefreshLayout) activity.findViewById(resId);
        progressView.setOnRefreshListener(null);
    }

    public void gotoActivity(Class<?> className, Bundle args) {

        Intent intent = new Intent(this, className);

        if (args != null) {

            intent.putExtra(KEY_MAIN_BUNDLE, args);

        }

        startActivity(intent);

    }

    public String getCurrentDate() {

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            String format = simpleDateFormat.format(new Date());

            return format;

        } catch (Exception e) {

            return "NA";
        }
    }

    public String getCurrentDate1() {

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String format = simpleDateFormat.format(new Date());

            return format;

        } catch (Exception e) {

            return "NA";
        }
    }

    public String SaveImage(Bitmap finalBitmap, String name) {

        Calendar now = Calendar.getInstance();
        String date = now.get(Calendar.MONTH) + "_" + now.get(Calendar.DAY_OF_MONTH) + "_" + now.get(Calendar.YEAR);

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/myqr");
        myDir.mkdirs();

        String fname = name + "_" + date + ".jpg";

        String filePath = root + "/myqr/" + fname;

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public String SaveGif(File finalBitmap, String name) {

        Calendar now = Calendar.getInstance();
        String date = now.get(Calendar.MONTH) + "_" + now.get(Calendar.DAY_OF_MONTH) + "_" + now.get(Calendar.YEAR);

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/myqr");
        myDir.mkdirs();

        String fname = name + "_" + date + ".gif";

        String filePath = root + "/myqr/" + fname;

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            //finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalBitmap.getAbsolutePath();
    }

    public String SavePDF(File pdf) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/myqr");
        myDir.mkdirs();

        String fname = "pdf" + System.currentTimeMillis() + ".pdf";

        String filePath = root + "/myqr/" + fname;

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            //finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pdf.getAbsolutePath();
    }

    public void showLogoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure you wand to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String socialProvider = sessionManager.getUserDetails().get(SessionManager.KEY_SOCIAL_PROVIDER);

                if (socialProvider.equalsIgnoreCase("Facebook")) {

                    LoginManager.getInstance().logOut();

                } else if (socialProvider.equalsIgnoreCase("Google")) {

                    FirebaseAuth.getInstance().signOut();

                    // Configure Google Sign In
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            //.requestIdToken(activity.getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    // Google sign out
                    GoogleSignIn.getClient(context, gso).signOut().addOnCompleteListener(BaseActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    //updateUI(null);

                                }
                            });

                }

                sessionManager.logoutUser(false);
                finishAffinity();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        builder.create().show();
    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }

    @Override
    public void onProfileData(RegisterResponse response) {

    }

    @Override
    public void onAddBagData(Response response) {

    }

    @Override
    public void onAddBagImageData(Response response) {

    }

    @Override
    public void onDeleteBagImage(Response response) {

    }

    @Override
    public void onBagDelete(boolean isDelete) {

    }

    @Override
    public void onBagChangeRequestSend(boolean status) {

    }


}
