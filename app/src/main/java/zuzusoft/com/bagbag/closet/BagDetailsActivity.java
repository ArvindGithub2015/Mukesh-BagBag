package zuzusoft.com.bagbag.closet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.adapters.BagImagePagerAdapter;
import zuzusoft.com.bagbag.closet.model.DataAddBagImage;
import zuzusoft.com.bagbag.custom_views.circular_indicator.CircleIndicator;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.home.PetitionsFragment;
import zuzusoft.com.bagbag.home.models.DataBags;
import zuzusoft.com.bagbag.home.models.search.BagAllimage;

/**
 * Created by mukeshnarayan on 19/11/18.
 */

public class BagDetailsActivity extends BaseActivity {

    public static final String KEY_CALLED_FROM = "from";
    public static final String VAL_SEARCH = "search";
    public static final String VAL_PETITION = "petition";
    public static final String DATA_BAG = "bag_data";

    @BindView(R.id.btnNextLeft)
    ImageView btnNextLeft;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.btnNextRight)
    ImageView btnNextRight;

    @BindView(R.id.btnSelectBag)
    FrameLayout btnSelectBag;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.indicator)
    CircleIndicator indicator;

    @BindView(R.id.profileImage)
    CircleImageView profileImage;

    @BindView(R.id.bagOwnerName)
    TextView bagOwnerName;

    @BindView(R.id.bagDistance)
    TextView bagDistance;

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

    @BindView(R.id.brandName)
    TextView brandName;


    private String calledFrom;

    private DataBags dataBags;

    private int VIEWPAGER_ITEM_COUNT = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_details);

        initViews();
    }

    private void initViews() {

        //spread butter
        ButterKnife.bind(this);

        calledFrom = getIntent().getStringExtra(KEY_CALLED_FROM);

        if(calledFrom.equalsIgnoreCase(VAL_SEARCH)){

            dataBags = (DataBags) getIntent().getSerializableExtra(DATA_BAG);

        }else if(calledFrom.equalsIgnoreCase(VAL_PETITION)){

            dataBags = (DataBags) getIntent().getSerializableExtra(DATA_BAG);

        }else{

            finish();
        }

        //viewPager.setAdapter(new BagImagePagerAdapter(context, getBagData()));
        indicator.setViewPager(viewPager);

        btnNextLeft.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnNextRight.setOnClickListener(this);
        btnSelectBag.setOnClickListener(this);

        updateViews();
    }

    private void updateViews() {

        //set view pager
        ArrayList<DataAddBagImage> dataSet = getBagData();
        VIEWPAGER_ITEM_COUNT = dataSet.size();
        viewPager.setAdapter(new BagImagePagerAdapter(context, getBagData(), VIEWPAGER_ITEM_COUNT));
        indicator.setViewPager(viewPager);

        //set profile image
        Glide.with(context)
                .load(dataBags.getBagOwnerImage())
                // .placeholder(R.raw.default_profile)
                .into(profileImage);

        //set other fields
        brandName.setText(dataBags.getBrandName());
        bagOwnerName.setText(dataBags.getBagOwnerName());
        bagDistance.setText(dataBags.getDistance());
        bagMaterial.setText(dataBags.getBagMaterial());
        bagColor.setText(dataBags.getBagColour());
        bagSize.setText(dataBags.getBagSize());
        bagType.setText(dataBags.getBagType());
        bagDescreption.setText(dataBags.getBagDescription());

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

            case R.id.btnSelectBag:

                //check search or petition
                if(calledFrom.equalsIgnoreCase(VAL_PETITION)){

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(DATA_BAG, getIntent().getSerializableExtra(DATA_BAG));
                    returnIntent.putExtra(PetitionsFragment.KEY_BAG_POSITION, getIntent().getIntExtra(PetitionsFragment.KEY_BAG_POSITION, -1));
                    setResult(PetitionsFragment.REQUEST_CODE_FOR_RESULT,returnIntent);
                    finish();

                }else if(calledFrom.equalsIgnoreCase(VAL_SEARCH)){

                    Intent intent = new Intent(context, BagSelectionActivity.class);
                    intent.putExtra(BagDetailsActivity.DATA_BAG, dataBags);
                    context.startActivity(intent);
                    finish();

                }

                break;

        }
    }


    private ArrayList<DataAddBagImage> getBagData() {

        ArrayList<DataAddBagImage> dataSet = new ArrayList<>();

        for (BagAllimage bag:
        dataBags.getBagImages()) {

            DataAddBagImage b1 = new DataAddBagImage();
            b1.setBagImageId(bag.getImgId());
            b1.setBagImageUrl(bag.getBagUrl());
            b1.setBagId(bag.getBagId());
            b1.setHasBagData(true);

            dataSet.add(b1);
        }

        return dataSet;
    }

}
