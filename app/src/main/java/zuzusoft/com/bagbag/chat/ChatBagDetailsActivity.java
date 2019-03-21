package zuzusoft.com.bagbag.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.adapters.BagImagePagerAdapter;
import zuzusoft.com.bagbag.closet.model.DataAddBagImage;
import zuzusoft.com.bagbag.custom_views.circular_indicator.CircleIndicator;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.home.models.chat.MyBag;
import zuzusoft.com.bagbag.home.models.chat.MyBagAllimage;
import zuzusoft.com.bagbag.home.models.chat.RosterBag;

/**
 * Created by mukeshnarayan on 27/11/18.
 */

public class ChatBagDetailsActivity extends BaseActivity {

    public static final String KEY_BUNDLE_BAG_DATA = "bundle_bag_data";
    public static final String KEY_BUNDLE_WHICH_BAG = "which_bag";

    @BindView(R.id.btnNextLeft)
    ImageView btnNextLeft;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.btnNextRight)
    ImageView btnNextRight;

    @BindView(R.id.btnChat)
    FrameLayout btnChat;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.indicator)
    CircleIndicator indicator;

    @BindView(R.id.profileImage)
    CircleImageView profileImage;

    @BindView(R.id.userName)
    TextView userName;

    @BindView(R.id.userDistance)
    TextView userDistance;

    @BindView(R.id.brandName)
    TextView brandName;
    @BindView(R.id.bagMaterial)
    TextView bagMaterial;
    @BindView(R.id.bagColor)
    TextView bagColor;
    @BindView(R.id.bagSize)
    TextView bagSize;
    @BindView(R.id.bagType)
    TextView bagType;
    @BindView(R.id.bagDescreption)
    TextView bagDescreption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bag_detail);

        initViews();
    }


    private void initViews() {

        //spread butter
        ButterKnife.bind(this);

        MyBag myBag = null;
        RosterBag rosterBag = null;
        int which = getIntent().getIntExtra(KEY_BUNDLE_WHICH_BAG, 1);
        if(which == 1){

            rosterBag = (RosterBag) getIntent().getBundleExtra(KEY_MAIN_BUNDLE).getSerializable(KEY_BUNDLE_BAG_DATA);

        }else{

            rosterBag = (RosterBag) getIntent().getBundleExtra(KEY_MAIN_BUNDLE).getSerializable(KEY_BUNDLE_BAG_DATA);

        }

        btnNextLeft.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnNextRight.setOnClickListener(this);
        btnChat.setOnClickListener(this);

        updateViews(which, myBag, rosterBag);
    }

    private void updateViews(int which, MyBag myBag, RosterBag rosterBag) {


        viewPager.setAdapter(new BagImagePagerAdapter(context, getBagImageDataFromRosterBag(rosterBag.getMyBagAllimage()), rosterBag.getMyBagAllimage().size()));
        indicator.setViewPager(viewPager);

        //top bar
        Glide.with(context)
                .load(rosterBag.getUserImage())
                // .placeholder(R.raw.default_profile)
                .into(profileImage);

        userName.setText(rosterBag.getUserName());
        userDistance.setText("a "+"2"+" km distance");
        brandName.setText(rosterBag.getBrandName());
        bagMaterial.setText(rosterBag.getMaterial());
        bagColor.setText(rosterBag.getColour());
        bagSize.setText(rosterBag.getBagSize());
        bagType.setText(rosterBag.getBagType());
        bagDescreption.setText(rosterBag.getBagDescription());


        /*if(which == 1){
            //me
            viewPager.setAdapter(new BagImagePagerAdapter(context, getBagImageDataFromMyBag(myBag.getMyBagAllimage()), myBag.getMyBagAllimage().size()));
            indicator.setViewPager(viewPager);

            //top bar
            Glide.with(context)
                    .load(myBag.getUserImage())
                    // .placeholder(R.raw.default_profile)
                    .into(profileImage);

            userName.setText(myBag.getUserName());
            userDistance.setText("a "+"2"+" km distance");
            brandName.setText(myBag.getBrandName());
            bagMaterial.setText(myBag.getMaterial());
            bagColor.setText(myBag.getColour());
            bagSize.setText(myBag.getBagSize());
            bagType.setText(myBag.getBagType());
            bagDescreption.setText(myBag.getBagDescription());

        }else{
            //roster

            viewPager.setAdapter(new BagImagePagerAdapter(context, getBagImageDataFromRosterBag(rosterBag.getRosterBagAllimage()), rosterBag.getRosterBagAllimage().size()));
            indicator.setViewPager(viewPager);

            //top bar
            Glide.with(context)
                    .load(rosterBag.getUserImage())
                    // .placeholder(R.raw.default_profile)
                    .into(profileImage);

            userName.setText(rosterBag.getUserName());
            userDistance.setText("a "+"2"+" km distance");
            brandName.setText(rosterBag.getBrandName());
            bagMaterial.setText(rosterBag.getMaterial());
            bagColor.setText(rosterBag.getColour());
            bagSize.setText(rosterBag.getBagSize());
            bagType.setText(rosterBag.getBagType());
            bagDescreption.setText(rosterBag.getBagDescription());

        }*/

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnBack:

                finish();

                break;

            case R.id.btnNextLeft:

                viewPager.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int currentPage = viewPager.getCurrentItem();

                        viewPager.setCurrentItem(currentPage-1, true);
                    }
                }, 100);

                break;

            case R.id.btnNextRight:

                viewPager.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int currentPage = viewPager.getCurrentItem();

                        viewPager.setCurrentItem(currentPage+1, true);
                    }
                }, 100);

                break;

            case R.id.btnChat:

                finish();

                break;

        }
    }


    private ArrayList<DataAddBagImage> getBagImageDataFromMyBag(List<MyBagAllimage> bagAllimage) {

        ArrayList<DataAddBagImage> dataSet = new ArrayList<>();

        for (MyBagAllimage bag:
        bagAllimage) {

            DataAddBagImage b1 = new DataAddBagImage();
            b1.setBagImageUrl(bag.getBagUrl());
            dataSet.add(b1);
        }

        return dataSet;
    }

    private ArrayList<DataAddBagImage> getBagImageDataFromRosterBag(List<MyBagAllimage> bagAllimage) {

        ArrayList<DataAddBagImage> dataSet = new ArrayList<>();

        for (MyBagAllimage bag:
                bagAllimage) {

            DataAddBagImage b1 = new DataAddBagImage();
            b1.setBagImageUrl(bag.getBagUrl());
            dataSet.add(b1);
        }

        return dataSet;
    }

}
