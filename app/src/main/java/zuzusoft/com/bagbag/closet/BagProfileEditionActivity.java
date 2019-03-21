package zuzusoft.com.bagbag.closet;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.adapters.AddBagImageAdapter;
import zuzusoft.com.bagbag.closet.adapters.BagImagePagerAdapter;
import zuzusoft.com.bagbag.closet.model.DataAddBagImage;
import zuzusoft.com.bagbag.closet.model.Response;
import zuzusoft.com.bagbag.custom_views.circular_indicator.CircleIndicator;
import zuzusoft.com.bagbag.custom_views.crop_view.CropImage;
import zuzusoft.com.bagbag.custom_views.crop_view.CropImageView;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.MyGlideEngine;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.home.MyClosetFragment;
import zuzusoft.com.bagbag.home.models.my_closet.Bag;
import zuzusoft.com.bagbag.home.models.my_closet.BagAllimage;
import zuzusoft.com.bagbag.rest.ApiDataInflation;

/**
 * Created by mukeshnarayan on 19/11/18.
 */

public class BagProfileEditionActivity extends BaseActivity implements AddBagImageAdapter.OnThumbnailClickListener {

    //constants for bag data
    public static final String MAP_BRAND_NAME = "brand_name";
    public static final String MAP_BAG_IMAGE1 = "bag_image1";
    public static final String MAP_BAG_IMAGE2 = "bag_image2";
    public static final String MAP_BAG_IMAGE3 = "bag_image3";
    public static final String MAP_BAG_IMAGE4 = "bag_image4";
    public static final String MAP_BAG_MATERIAL = "bag_material";
    public static final String MAP_BAG_COLOUR = "bag_colour";
    public static final String MAP_BAG_SIZE = "bag_size";
    public static final String MAP_BAG_TYPE = "bag_type";
    public static final String MAP_BAG_DESCRIPTION = "bag_descp";
    public static final int INTENT_REQUEST_CODE_UPDATE_BAG = 262;

    public static final String KEY_SEIRAL_DATA = "serial_data";
    public static final String KEY_FROM_MY_CLOSET = "from_closet";


    @BindView(R.id.btnEdit)
    LinearLayout btnEdit;

    @BindView(R.id.btnDelete)
    LinearLayout btnDelete;

    @BindView(R.id.btnChat)
    LinearLayout btnChat;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.btnNextLeft)
    ImageView btnNextLeft;

    @BindView(R.id.btnNextRight)
    ImageView btnNextRight;

    @BindView(R.id.btnDeleteOneBag)
    ImageView btnDeleteOneBag;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.indicator)
    CircleIndicator indicator;

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

    @BindView(R.id.thumbnailRecyclerView)
    RecyclerView mThumbnailRecyclerView;

    private ImagePicker imagePicker;
    private List<Uri> mSelected = new ArrayList<>();
    private Uri resultUri;
    private boolean isCamera = false;
    private int REQUEST_CODE_CHOOSE = 999;
    private boolean permission = false;

    private zuzusoft.com.bagbag.closet.model.Response response;
    private zuzusoft.com.bagbag.home.models.my_closet.Bag bag;
    private boolean fromCloset;
    private String bagId;
    private HashMap<String, String> bagData = new HashMap<>();

    private DialogHelper dialogHelper;

    private ArrayList<DataAddBagImage> bagImageDataList = new ArrayList();
    private RecyclerView.LayoutManager mLayoutManager;
    private int VIEWPAGER_ITEM_COUNT = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_profile_edition);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) { permission = true;/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) { permission = false;/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        initViews();
    }

    private void initViews() {

        //spread butter
        ButterKnife.bind(this);

        dialogHelper = new DialogHelper(context);
        dialogHelper.setDialogInfo("", "Please Wait...");

        fromCloset = getIntent().getBooleanExtra(KEY_FROM_MY_CLOSET, false);

        if(fromCloset){

            bag = (zuzusoft.com.bagbag.home.models.my_closet.Bag) getIntent().getSerializableExtra(KEY_SEIRAL_DATA);
            bagId = bag.getId();

            bagData.put(MAP_BAG_IMAGE1, bag.getBagAllimage().get(0).getBagUrl());
            bagData.put(MAP_BAG_IMAGE2, (bag.getBagAllimage().size() == 2)? bag.getBagAllimage().get(1).getBagUrl() : "null");
            bagData.put(MAP_BAG_IMAGE3, (bag.getBagAllimage().size() == 3)? bag.getBagAllimage().get(2).getBagUrl() : "null");
            bagData.put(MAP_BAG_IMAGE4, (bag.getBagAllimage().size() == 4)? bag.getBagAllimage().get(3).getBagUrl() : "null");

            bagData.put(MAP_BAG_MATERIAL, bag.getMaterial());
            bagData.put(MAP_BAG_COLOUR, bag.getColour());
            bagData.put(MAP_BAG_SIZE, bag.getBagSize());
            bagData.put(MAP_BAG_TYPE, bag.getBagType());

            bagData.put(MAP_BRAND_NAME, bag.getBrandName());
            bagData.put(MAP_BAG_DESCRIPTION, bag.getBagDescription());

            populateBagImageDataList(fromCloset, bag, response);


        }else{

            response = (Response) getIntent().getSerializableExtra(KEY_SEIRAL_DATA);
            bagId = response.getBag().get(0).getId();

            bagData.put(MAP_BAG_IMAGE1, response.getBag().get(0).getBagAllimage().get(0).getBagUrl());
            bagData.put(MAP_BAG_IMAGE2, (response.getBag().get(0).getBagAllimage().size() == 2)? response.getBag().get(0).getBagAllimage().get(1).getBagUrl() : "null");
            bagData.put(MAP_BAG_IMAGE3, (response.getBag().get(0).getBagAllimage().size() == 3)? response.getBag().get(0).getBagAllimage().get(2).getBagUrl() : "null");
            bagData.put(MAP_BAG_IMAGE4, (response.getBag().get(0).getBagAllimage().size() == 4)? response.getBag().get(0).getBagAllimage().get(3).getBagUrl() : "null");

            bagData.put(MAP_BAG_MATERIAL, response.getBag().get(0).getMaterial());
            bagData.put(MAP_BAG_COLOUR, response.getBag().get(0).getColour());
            bagData.put(MAP_BAG_SIZE, response.getBag().get(0).getBagSize());
            bagData.put(MAP_BAG_TYPE, response.getBag().get(0).getBagType());

            bagData.put(MAP_BRAND_NAME, response.getBag().get(0).getBrandName());
            bagData.put(MAP_BAG_DESCRIPTION, response.getBag().get(0).getBagDescription());

            populateBagImageDataList(fromCloset, bag, response);

        }

        //----------------------------------------------------
        imagePicker = new ImagePicker();
        imagePicker.setTitle("Camera");
        imagePicker.setCropImage(false);
        //-----------------------------------------------------

        //init recycler view
        mThumbnailRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mThumbnailRecyclerView.setLayoutManager(mLayoutManager);

        btnEdit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnNextLeft.setOnClickListener(this);
        btnNextRight.setOnClickListener(this);
        btnDeleteOneBag.setOnClickListener(this);

        updateViews();

    }

    private void populateBagImageDataList(boolean fromCloset, Bag bagData, Response response) {

        bagImageDataList.clear();
        VIEWPAGER_ITEM_COUNT = 0;

        if(fromCloset){

            List<BagAllimage> bagImageList = bagData.getBagAllimage();
            for (int i = 0; i < 4; i++) {

                if(bagImageList.size()>i){

                    BagAllimage bi = bagImageList.get(i);
                    DataAddBagImage dataBagImage = new DataAddBagImage();
                    dataBagImage.setBagId(bi.getBagId());
                    dataBagImage.setBagImageId(bi.getImgId());
                    dataBagImage.setBagImageUrl(bi.getBagUrl());
                    dataBagImage.setHasBagData(true);

                    bagImageDataList.add(dataBagImage);
                    VIEWPAGER_ITEM_COUNT++;

                }else{

                    //set empty data
                    DataAddBagImage dataBagImage = new DataAddBagImage();
                    dataBagImage.setBagId(null);
                    dataBagImage.setBagImageId(null);
                    dataBagImage.setBagImageUrl(null);
                    dataBagImage.setHasBagData(false);

                    bagImageDataList.add(dataBagImage);

                }

            }

        }else{

            List<zuzusoft.com.bagbag.closet.model.BagAllimage> bagImageList = response.getBag().get(0).getBagAllimage();
            for (int i = 0; i < 4; i++) {

                if(bagImageList.size()>i){

                    zuzusoft.com.bagbag.closet.model.BagAllimage bi = bagImageList.get(i);
                    DataAddBagImage dataBagImage = new DataAddBagImage();
                    dataBagImage.setBagId(bi.getBagId());
                    dataBagImage.setBagImageId(bi.getImgId());
                    dataBagImage.setBagImageUrl(bi.getBagUrl());
                    dataBagImage.setHasBagData(true);

                    bagImageDataList.add(dataBagImage);
                    VIEWPAGER_ITEM_COUNT++;

                }else{

                    //set empty data
                    DataAddBagImage dataBagImage = new DataAddBagImage();
                    dataBagImage.setBagId(null);
                    dataBagImage.setBagImageId(null);
                    dataBagImage.setBagImageUrl(null);
                    dataBagImage.setHasBagData(false);

                    bagImageDataList.add(dataBagImage);

                }

            }
        }
    }


    /**
     * This function update views in four cases
     * 1 When activity load first time
     * 2 When switch between multiple images
     * 3 When image add
     * 4 When image removed
     *
     */
    private void updateViews() {

        //set brand name on header
        brandName.setText(bagData.get(MAP_BRAND_NAME));
        bagMaterial.setText(bagData.get(MAP_BAG_MATERIAL));
        bagColor.setText(bagData.get(MAP_BAG_COLOUR));
        bagSize.setText(bagData.get(MAP_BAG_SIZE));
        bagType.setText(bagData.get(MAP_BAG_TYPE));
        bagDescreption.setText(bagData.get(MAP_BAG_DESCRIPTION));

        //update view pager
        updateViewPager();

        //update thumbnail recycler
        updateThumbnailRecycler();

    }

    private void updateViewPager() {

        viewPager.setAdapter(new BagImagePagerAdapter(context, bagImageDataList, VIEWPAGER_ITEM_COUNT));
        indicator.setViewPager(viewPager);

    }

    private void updateThumbnailRecycler() {

        RecyclerView.Adapter mAdapter = new AddBagImageAdapter(context, bagImageDataList);
        mThumbnailRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnEdit:

                Intent editIntent = new Intent(context, UpdateBagDetailsActivity.class);

                editIntent.putExtra(BagProfileEditionActivity.MAP_BRAND_NAME, bagData.get(MAP_BRAND_NAME));
                editIntent.putExtra(BagProfileEditionActivity.MAP_BAG_DESCRIPTION, bagData.get(MAP_BAG_DESCRIPTION));
                editIntent.putExtra(BagProfileEditionActivity.MAP_BAG_MATERIAL, bagData.get(MAP_BAG_MATERIAL));
                editIntent.putExtra(BagProfileEditionActivity.MAP_BAG_COLOUR, bagData.get(MAP_BAG_COLOUR));
                editIntent.putExtra(BagProfileEditionActivity.MAP_BAG_SIZE, bagData.get(MAP_BAG_SIZE));
                editIntent.putExtra(BagProfileEditionActivity.MAP_BAG_TYPE, bagData.get(MAP_BAG_TYPE));
                editIntent.putExtra(ApiDataInflation.KEY_BAG_ID, bagId);

                startActivityForResult(editIntent, INTENT_REQUEST_CODE_UPDATE_BAG);

                break;

            case R.id.btnBack:

                if(!fromCloset){

                    MyClosetFragment.KEY_REFRESH_MY_CLOSET = true;
                }
                finish();

                break;

            case R.id.btnDelete:

                showDeleteBagConfirmaion();

                break;

            case R.id.btnChat:

                IS_FRAGMENT_UPDATE = true;
                FRAGMENT_INDEX = 3;
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

            case R.id.btnDeleteOneBag:

                showDeleteBagImageConfirmaion();

                break;

        }
    }

    private void updatePagerItem(final int index) {

        viewPager.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                viewPager.setCurrentItem(index, true);
            }
        }, 100);

    }

    private void showDeleteBagConfirmaion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure, do you want to delete this bag?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogHelper.showProgressDialog();
                ApiDataInflation dataInflation = new ApiDataInflation(context);
                dataInflation.deleteBag(bagId);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }


    private void showDeleteBagImageConfirmaion() {

        //Log.v("BagImageId", bagImageDataList.get(viewPager.getCurrentItem()).getBagImageId());
        //Log.v("Public", sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC));
        //Log.v("Private", sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure, do you want to delete this bag image?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(bagImageDataList.size() == 1){


                }
                deleteBagImage(bagImageDataList.get(viewPager.getCurrentItem()).getBagImageId());

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



            }
        });

        builder.create().show();
    }

    private void pickAction() {

        final CharSequence colors[] = new CharSequence[] {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Pick a color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                if(which == 0){

                    //camera
                    isCamera = true;

                    imagePicker.startCamera(BagProfileEditionActivity.this, new ImagePicker.Callback() {

                        @Override
                        public void onPickImage(Uri imageUri) {

                            Log.v("Camera image uri: ", imageUri.getPath());

                            CropImage.activity(imageUri)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(BagProfileEditionActivity.this);

                        }

                    });

                }else{

                    //gallery
                    Matisse.from(BagProfileEditionActivity.this)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                            .countable(true)
                            .maxSelectable(1)
                            //.gridExpectedSize(getResources().getDimensionPixelSize(16))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new MyGlideEngine())
                            .forResult(REQUEST_CODE_CHOOSE);

                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_REQUEST_CODE_UPDATE_BAG){

            //set refresh flag for my close screen
            MyClosetFragment.KEY_REFRESH_MY_CLOSET = true;

            //update new bag info
            brandName.setText(data.getStringExtra(ApiDataInflation.KEY_BRAND_NAME));
            bagMaterial.setText(data.getStringExtra(ApiDataInflation.KEY_MATERIAL));
            bagColor.setText(data.getStringExtra(ApiDataInflation.KEY_COLOUR));
            bagSize.setText(data.getStringExtra(ApiDataInflation.KEY_BAG_SIZE));
            bagType.setText(data.getStringExtra(ApiDataInflation.KEY_BAG_TYPE));
            bagDescreption.setText(data.getStringExtra(ApiDataInflation.KEY_DESCP));

        }else{

            onCameraResultIntent(requestCode, resultCode, data);
        }

    }

    private void onCameraResultIntent(int requestCode, int resultCode, Intent data) {

        if(isCamera){

            isCamera = false;

            imagePicker.onActivityResult(this, requestCode, resultCode, data);

        }else{

            if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

                mSelected = Matisse.obtainResult(data);

                //go to crop activity

                CropImage.activity(mSelected.get(0))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }


            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    Uri resultUri = result.getUri();

                    this.resultUri = resultUri;

                    Log.v("Image Crop", resultUri.getPath());
                    Intent intent = new Intent(context, BagProfileEditionActivity.class);
                    //startActivity(intent);

                    addNewBagImage();

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();
                    error.printStackTrace();

                }
            }

        }
    }


    private void deleteBagImage(String bagImageId){

        dialogHelper.showProgressDialog();
        ApiDataInflation dataInflation = new ApiDataInflation(context);
        dataInflation.deleteBagImage(bagImageId, bagId);
    }

    private void addNewBagImage() {

        HashMap<String, String> dataSet = new HashMap<>();
        dataSet.put(ApiDataInflation.KEY_USER_ID, sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID));
        dataSet.put(ApiDataInflation.KEY_BAG_ID, bagId);
        dataSet.put(ApiDataInflation.KEY_BAG_IMAGE, resultUri.getPath());

        dialogHelper.showProgressDialog();
        ApiDataInflation dataInflation = new ApiDataInflation(context);
        dataInflation.addBagImage(dataSet);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(!fromCloset){
            MyClosetFragment.KEY_REFRESH_MY_CLOSET = true;
        }
    }

    @Override
    public void onBagDelete(boolean isDelete) {

        if(isDelete){

            dialogHelper.closeDialog();
            Toast.makeText(context, "Bag deleted successfully", Toast.LENGTH_SHORT).show();
            MyClosetFragment.KEY_REFRESH_MY_CLOSET = true;
            finish();

        }
    }

    @Override
    public void onDeleteBagImage(Response response) {
        super.onDeleteBagImage(response);

        dialogHelper.closeDialog();

        //check response null
        if(response != null){

            //refresh my closet
            MyClosetFragment.KEY_REFRESH_MY_CLOSET = true;

            //populated data
            populateBagImageDataList(false, null, response);

            //update recycler and view pager
            updateThumbnailRecycler();
            updateViews();

            Toast.makeText(context, "Image delete successfully", Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(this, "Fail to delete image", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAddBagImageData(Response response) {
        super.onAddBagImageData(response);

        dialogHelper.closeDialog();

        //check response null
        if(response != null){

            //refresh my closet
            MyClosetFragment.KEY_REFRESH_MY_CLOSET = true;

            //populated data
            populateBagImageDataList(false, null, response);

            //update recycler and view pager
            updateThumbnailRecycler();
            updateViews();


        }else{

            Toast.makeText(this, "Fail to update image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onThumbnailClick(DataAddBagImage bagData, int position) {

        if(bagData.isHasBagData()){

            //set thumbnail image to viewpager
            updatePagerItem(position);


        }else{

            //add new bag image
            pickAction();
        }
    }
}
