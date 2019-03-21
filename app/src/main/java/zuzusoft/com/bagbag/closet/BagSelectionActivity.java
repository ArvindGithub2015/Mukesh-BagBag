package zuzusoft.com.bagbag.closet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.home.SearchBagsFragment;
import zuzusoft.com.bagbag.home.models.DataBags;
import zuzusoft.com.bagbag.home.models.my_closet.Bag;
import zuzusoft.com.bagbag.home.models.my_closet.MyClosetResponse;
import zuzusoft.com.bagbag.rest.ApiClient;
import zuzusoft.com.bagbag.rest.ApiDataInflation;
import zuzusoft.com.bagbag.rest.ApiInterface;

/**
 * Created by mukeshnarayan on 19/11/18.
 */

public class BagSelectionActivity extends BaseActivity implements GridBagAdapter.OnBagSelectListener{

    @BindView(R.id.btnClearSelection)
    TextView btnClearSelection;

    @BindView(R.id.btnSubmit)
    TextView btnSubmit;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.ownerBagImage)
    ImageView ownerBagImage;

    @BindView(R.id.ownerProfileImage)
    CircleImageView ownerProfileImage;

    @BindView(R.id.ownerName)
    TextView ownerName;

    @BindView(R.id.ownerDistance)
    TextView ownerDistance;

    @BindView(R.id.brandName)
    TextView brandName;

    @BindView(R.id.txtCounter)
    TextView txtCounter;

    @BindView(R.id.gridView)
    GridView gridView;

    @BindView(R.id.bagMaterial)
    TextView bagMaterial;

    @BindView(R.id.bagColor)
    TextView bagColor;

    @BindView(R.id.bagSize)
    TextView bagSize;

    @BindView(R.id.bagType)
    TextView bagType;

    @BindView(R.id.emptyView)
    LinearLayout emptyView;


    @BindView(R.id.btnAddBag)
    TextView btnAddBag;



    private static int selectBagCount = 0;

    private DataBags dataBags;
    private List<Bag> dataSet = new ArrayList<>();
    private HashMap<Integer, String> offerBagIds = new HashMap<>();

    private SessionManager sessionManager;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bag_selection);

        initView();
    }

    private void initView() {

        //spread butter
        ButterKnife.bind(this);

        sessionManager = new SessionManager(context);

        dialogHelper = new DialogHelper(context);
        dialogHelper.setDialogInfo("", "Please Wait...");

        dataBags = (DataBags) getIntent().getSerializableExtra(BagDetailsActivity.DATA_BAG);

        btnBack.setOnClickListener(this);
        btnClearSelection.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnAddBag.setOnClickListener(this);

        checkViewEmpty(1);
        updateOwnerData();

        loadMyBags();

    }

    private void loadMyBags() {

        if(ApiClient.isNetworkAvailable(context)){

            myClosetApi();

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOwnerData() {

        Glide.with(context)
                .load(dataBags.getBagImages().get(0).getBagUrl())
                // .placeholder(R.raw.default_profile)
                .into(ownerBagImage);

        Glide.with(context)
                .load(dataBags.getOwnerImage())
                // .placeholder(R.raw.default_profile)
                .into(ownerProfileImage);

        ownerName.setText(dataBags.getBagOwnerName());
        ownerDistance.setText("a "+dataBags.getDistance()+" km distance");
        brandName.setText(dataBags.getBrandName());
        bagMaterial.setText(dataBags.getBagMaterial());
        bagColor.setText(dataBags.getBagColour());
        bagSize.setText(dataBags.getBagSize());
        bagType.setText(dataBags.getBagType());

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnBack:

                selectBagCount=0;
                finish();

                break;


            case R.id.btnAddBag:

                Intent intent = new Intent(context, AddBagActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.btnClearSelection:

                gridView.setAdapter(new GridBagAdapter(context, getGoldenBags()));
                selectBagCount = 0;
                txtCounter.setText("0");
                offerBagIds.clear();

                break;

            case R.id.btnSubmit:

                if(offerBagIds.isEmpty() ){

                    Toast.makeText(this, "You have to offer at least one bag in exchange.", Toast.LENGTH_SHORT).show();
                }else{

                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<Integer,String> entry : offerBagIds.entrySet()) {
                    /*System.out.println("Key = " + entry.getKey() +
                            ", Value = " + entry.getValue());*/

                        sb.append(entry.getValue()+",");

                    }

                    String myBagIds = sb.toString().replaceAll(",$", "");
                    String requestUserId = dataBags.getOwnerUserId();
                    String requestBagId = dataBags.getOwnerBagId();

                    HashMap<String, String> dataSet = new HashMap<>();
                    dataSet.put(ApiDataInflation.KEY_OFFER_BAG_IDS, myBagIds);
                    dataSet.put(ApiDataInflation.KEY_OWNER_USER_ID, requestUserId);
                    dataSet.put(ApiDataInflation.KEY_OWNER__BAGE_ID, requestBagId);

                    dialogHelper.showProgressDialog();
                    ApiDataInflation dataInflation = new ApiDataInflation(context);
                    dataInflation.sendBagChangeRequest(dataSet);

                }

                break;
        }
    }


    private ArrayList<DataBags> getGoldenBags() {

        ArrayList<DataBags> dataSet = new ArrayList<>();

        for (Bag bag:
             this.dataSet) {

            DataBags b1 = new DataBags();
            b1.setBagFileName(bag.getBagAllimage().get(0).getBagUrl());
            b1.setOwnerBagId(bag.getBagAllimage().get(0).getBagId());

            dataSet.add(b1);

        }

        return dataSet;
    }


    private void myClosetApi() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        dialogHelper.showProgressDialog();

        SessionManager sessionManager = new SessionManager(context);
        String publicKey = sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC);
        String privateKey = sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE);

        Call<MyClosetResponse> call = apiService.getMyCloset(publicKey, privateKey);

        call.enqueue(new Callback<MyClosetResponse>() {
            @Override
            public void onResponse(Call<MyClosetResponse> call, Response<MyClosetResponse> response) {

                dialogHelper.closeDialog();

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                            dataSet.clear();
                            dataSet = response.body().getResponse().getBag();

                            if(!dataSet.isEmpty()){

                                gridView.setAdapter(new GridBagAdapter(context, getGoldenBags()));
                                checkViewEmpty(3);

                            }else{

                                Toast.makeText(context, "Your Closet is Empty!", Toast.LENGTH_SHORT).show();
                                checkViewEmpty(2);

                            }




                    } else {

                        //Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Your Closet is Empty!", Toast.LENGTH_SHORT).show();
                        checkViewEmpty(2);
                    }

                }

            }
            @Override
            public void onFailure(Call<MyClosetResponse> call, Throwable t) {

                dialogHelper.closeDialog();
                t.printStackTrace();

            }
        });
    }


    private void checkViewEmpty(int mode){

        switch (mode){

            case 1:
                //default, hide both listview and empty view
                gridView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);

                break;


            case 2:
                //when my closet empty
                gridView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

                break;


            case 3:
                //when my closet not empty
                gridView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        selectBagCount = 0;
    }

    @Override
    public void onBackPressed() {

        selectBagCount = 0;

        super.onBackPressed();
    }

    @Override
    public void onSelect(int position, String bagId) {

        selectBagCount++;
        txtCounter.setText(selectBagCount+"");

        offerBagIds.put(position, bagId);

    }

    @Override
    public void onUnSelect(int position, String bagId) {

        selectBagCount--;
        txtCounter.setText(selectBagCount+"");

        offerBagIds.remove(position);
    }


    @Override
    public void onBagChangeRequestSend(boolean status) {
        super.onBagChangeRequestSend(status);

        dialogHelper.closeDialog();
        if(status){

            //set refresh flag for search bags
            SearchBagsFragment.KEY_REFRESH_SEARCH_BAGS = true;
            finish();

        }else{

            Toast.makeText(this, "Failed to send petition.", Toast.LENGTH_SHORT).show();
        }


    }

}
