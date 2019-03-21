package zuzusoft.com.bagbag.custom_views.bottom_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.home.HelpandAssistanceActivity;
import zuzusoft.com.bagbag.home.HomeActivity;
import zuzusoft.com.bagbag.home.PrivacyPolicyActivity;
import zuzusoft.com.bagbag.home.TosActivity;

public class BottomBarHolderActivity extends BaseActivity implements BottomNavigationBar.BottomNavigationMenuClickListener {

    // helper class for handling UI and click events of bottom-nav-bar
    private BottomNavigationBar mBottomNav;

    // list of Navigation pages to be shown
    private List<NavigationPage> mNavigationPageList = new ArrayList<>();

    private ImageView btnShare;

    private FrameLayout toolbarContainer;
    private ImageView brandLogo;

    @BindView(R.id.btnDrawer)
    ImageView btnDrawer;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar_holder);

        initViews();

    }

    private void initViews() {

        //spread butter
        ButterKnife.bind(this);

        btnShare = (ImageView) findViewById(R.id.btnShare);
        btnShareListener();

        toolbarContainer = findViewById(R.id.toolbarContainer);
        brandLogo = findViewById(R.id.brandLogo);
        toolbarContainer.setBackgroundColor(getResources().getColor(R.color.gold_black));
        brandLogo.setImageResource(R.drawable.gold_logo_128);

        btnDrawer.setOnClickListener(this);

        drawerNavigationListener();

    }

    private void drawerNavigationListener() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // set item as selected to persist highlight
                //menuItem.setChecked(true);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here
                switch (menuItem.getItemId()){

                    case R.id.navShareBagBag:

                        shareAppLink();

                        break;

                    case R.id.navHelpAssistance:

                        gotoActivity(HelpandAssistanceActivity.class, null);

                        break;


                    case R.id.navNotification:

                        Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();

                        break;


                    case R.id.navTos:

                        gotoActivity(TosActivity.class, null);

                        break;



                    case R.id.navPrivacy:

                        gotoActivity(PrivacyPolicyActivity.class, null);

                        break;


                    case R.id.navLogout:

                            showLogoutDialog();

                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){

            drawerLayout.closeDrawer(Gravity.LEFT);

        }else {

            super.onBackPressed();

        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnDrawer:

                //open drawer
                drawerLayout.openDrawer(Gravity.LEFT, true);

                break;

        }
    }

    private void btnShareListener() {

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareAppLink();

            }
        });
    }

    private void shareAppLink(){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "BagBag App Under Progress.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    /**
     * initializes the BottomBarHolderActivity with sent list of Navigation pages
     * @param pages
     */
    public void setupBottomBarHolderActivity(List<NavigationPage> pages) {

        // throw error if pages does not have 4 elements
        if (pages.size() != 5) {
            throw new RuntimeException("List of NavigationPage must contain 4 members.");
        } else {
            mNavigationPageList = pages;
            mBottomNav = new BottomNavigationBar(this, pages, this);
            setupFragments();
        }

    }

    /**
     * sets up the fragments with initial view
     */
    private void setupFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, mNavigationPageList.get(0).getFragment());
        fragmentTransaction.commit();

        toolbarContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        brandLogo.setImageResource(R.drawable.bagbag_logo);

    }

    /**
     * handling onclick events of bar items
     * @param menuType
     */
    @Override
    public void onClickedOnBottomNavigationMenu(int menuType) {

        // finding the selected fragment
        Fragment fragment = null;
        switch (menuType) {
            case BottomNavigationBar.MENU_BAR_5:
                fragment = mNavigationPageList.get(4).getFragment();

                toolbarContainer.setBackgroundColor(getResources().getColor(R.color.gold_black));
                brandLogo.setImageResource(R.drawable.gold_logo_128);

                break;
            case BottomNavigationBar.MENU_BAR_1:
                fragment = mNavigationPageList.get(0).getFragment();

                toolbarContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                brandLogo.setImageResource(R.drawable.bagbag_logo);

                break;
            case BottomNavigationBar.MENU_BAR_2:
                fragment = mNavigationPageList.get(1).getFragment();

                toolbarContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                brandLogo.setImageResource(R.drawable.bagbag_logo);

                break;
            case BottomNavigationBar.MENU_BAR_3:
                fragment = mNavigationPageList.get(2).getFragment();

                toolbarContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                brandLogo.setImageResource(R.drawable.bagbag_logo);

                break;
            case BottomNavigationBar.MENU_BAR_4:
                fragment = mNavigationPageList.get(3).getFragment();

                toolbarContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                brandLogo.setImageResource(R.drawable.bagbag_logo);

                break;
        }

        // replacing fragment with the current one
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        }

    }

    public Fragment updateFragmentOnDemand(int index){

        Fragment fragment = null;

        if(index == 0){

        }else if(index == 1){


        }else if(index == 2){


        }else if(index == 3){

            fragment = mNavigationPageList.get(3).getFragment();

            toolbarContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            brandLogo.setImageResource(R.drawable.bagbag_logo);

            mBottomNav.updateBottomMenuOnDemand(3);

        }else if(index == 4){

            fragment = mNavigationPageList.get(4).getFragment();

            toolbarContainer.setBackgroundColor(getResources().getColor(R.color.gold_black));
            brandLogo.setImageResource(R.drawable.gold_logo_128);

            mBottomNav.updateBottomMenuOnDemand(4);

        }


        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();

            return fragment;

        }else{

            return null;
        }

    }
}
