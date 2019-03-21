package zuzusoft.com.bagbag.home;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuzusoft.com.bagbag.MainActivity;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.custom_views.bottom_navigation.BottomBarHolderActivity;
import zuzusoft.com.bagbag.custom_views.bottom_navigation.NavigationPage;
import zuzusoft.com.bagbag.helper.xmpp.MknXmppConstants;
import zuzusoft.com.bagbag.helper.xmpp.MknXmppHelper;
import zuzusoft.com.bagbag.helper.xmpp.MknXmppService;
import zuzusoft.com.bagbag.home.models.DataBags;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class HomeActivity extends BottomBarHolderActivity implements SearchBagsFragment.OnAdClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_near_me);

        initViews();

        checkUserInfo();

    }

    private void checkUserInfo() {

        HashMap<String, String> userData = sessionManager.getUserDetails();

        for (Map.Entry<String, String> entry : userData.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
    }

    private void initViews() {

        // four navigation pages that would be displayed as four tabs
        // contains title, icon and fragment instance
        NavigationPage page1 = new NavigationPage("My Closet", ContextCompat.getDrawable(this, R.drawable.my_closet), new MyClosetFragment());
        NavigationPage page2 = new NavigationPage("Search", ContextCompat.getDrawable(this, R.drawable.search_gray), new SearchBagsFragment());
        NavigationPage page3 = new NavigationPage("Petitions", ContextCompat.getDrawable(this, R.drawable.peticiones_gray), new PetitionsFragment());
        NavigationPage page4 = new NavigationPage("Chats", ContextCompat.getDrawable(this, R.drawable.chats_gray), new MyChatsFragment());
        NavigationPage page5 = new NavigationPage("Gold", ContextCompat.getDrawable(this, R.drawable.crow_on), new GoldFragment());

        // add them in a list
        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);
        navigationPages.add(page5);

        // pass them to super method
        super.setupBottomBarHolderActivity(navigationPages);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseMessaging.getInstance().subscribeToTopic("bagbag")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("TAG", msg);
                        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        //login chat service
        startJobService();
    }

    private void startJobService() {

        ComponentName componentName = new ComponentName(this, MknXmppService.class);
        JobInfo jobInfo = new JobInfo.Builder(MknXmppConstants.MKN_XMPP_JOB_ID, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {

            Log.d(MknXmppConstants.MKN_XMPP_CHAT_TAG, "startJobService: True");

        } else {

            Log.d(MknXmppConstants.MKN_XMPP_CHAT_TAG, "startJobService: False");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (IS_FRAGMENT_UPDATE) {

            updateFragmentOnDemand(FRAGMENT_INDEX);

        }

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle("BagBag");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //do nothing
            }
        });

        builder.create().show();

    }

    @Override
    public void onAdClicked(DataBags dataBag) {

        FRAGMENT_INDEX = 4;
        Fragment fragment = updateFragmentOnDemand(FRAGMENT_INDEX);

        if (fragment instanceof GoldFragment) {

            ((GoldFragment) fragment).updateNewData(dataBag);
        }

    }
}
