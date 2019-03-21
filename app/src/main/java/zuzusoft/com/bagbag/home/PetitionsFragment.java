package zuzusoft.com.bagbag.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.BagDetailsActivity;
import zuzusoft.com.bagbag.closet.MatchingDialog;
import zuzusoft.com.bagbag.custom_views.my_listview.MyListView;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.home.adapters.BagListAdapter;
import zuzusoft.com.bagbag.home.models.DataBags;
import zuzusoft.com.bagbag.home.models.search.Ad;
import zuzusoft.com.bagbag.home.models.search.Bag;
import zuzusoft.com.bagbag.home.models.search.SearchBagResponse;
import zuzusoft.com.bagbag.rest.ApiClient;
import zuzusoft.com.bagbag.rest.ApiDataInflation;
import zuzusoft.com.bagbag.rest.ApiDataInterface;
import zuzusoft.com.bagbag.rest.ApiInterface;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class PetitionsFragment extends Fragment {

    public static boolean KEY_REFRESH_PETITION = false;
    public static final String KEY_BAG_POSITION = "bag_position";
    public static final int REQUEST_CODE_FOR_RESULT = 666;

    @BindView(R.id.bagList)
    MyListView bagList;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.emptyView)
    LinearLayout emptyView;

    private DialogHelper dialogHelper;
    private BagListAdapter petitionAdapter;
    private ArrayList<DataBags> dataSet;

    @Override
    public void onResume() {
        super.onResume();

        if(KEY_REFRESH_PETITION){

            getAllPetitions();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_petitions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //spread butter
        ButterKnife.bind(this, view);

        dialogHelper = new DialogHelper(getActivity());
        dialogHelper.setDialogInfo("", "Please Wait...");

        checkViewEmpty(1);
        dataSet = new ArrayList<>();
        petitionAdapter = new BagListAdapter(getContext(), dataSet);
        bagList.setAdapter(petitionAdapter);
        setPetitionListener();

        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getAllPetitions();
                    }
                }
        );

        getAllPetitions();

    }

    private void setPetitionListener() {

        petitionAdapter.setOnPetitionListener(new BagListAdapter.PetitionListener() {
            @Override
            public void onPetitionAccept(DataBags bagData, int position) {

                alertOnPetitionAccept(bagData, position);

            }

            @Override
            public void onPetitionHide(DataBags bagData, int position) {

                alertOnPetitionHide(bagData, position);
            }
        });
    }

    private void alertOnPetitionHide(final DataBags bagData, final int position) {

        String title = "Alert";
        String message = "Are you sure you want to hide this petition?";

        DialogHelper.showMessageDialog(getContext(), title, message, new DialogHelper.MyDialogListener() {
            @Override
            public void onClickOk() {

                //make api call
                SessionManager sessionManager = new SessionManager(getContext());
                HashMap<String, String> postParams = new HashMap<>();
                postParams.put(SessionManager.KEY_PUBLIC, sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC));
                postParams.put(SessionManager.KEY_PRIVATE, sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));
                postParams.put(SessionManager.KEY_USER_ID, bagData.getOwnerUserId());
                postParams.put(ApiDataInflation.KEY_BAG_ID, bagData.getOwnerBagId());

                //dialogHelper.showProgressDialog();
                ApiDataInflation dataInflation = new ApiDataInflation(getContext());
                dataInflation.hidePetition(postParams);

                dataSet.remove(position);
                petitionAdapter.notifyDataSetChanged();

            }

            @Override
            public void onClickNo() {


            }
        });
    }

    private void alertOnPetitionAccept(final DataBags bagData, final int position) {

        String title = "Alert";
        String message = "Do you want to accept this petition request?";

        DialogHelper.showMessageDialog(getContext(), title, message, new DialogHelper.MyDialogListener() {
            @Override
            public void onClickOk() {

                SessionManager sessionManager = new SessionManager(getContext());

                HashMap<String, String> dataSet = new HashMap<>();
                dataSet.put(SessionManager.KEY_PUBLIC, sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC));
                dataSet.put(SessionManager.KEY_PRIVATE, sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));
                dataSet.put(SessionManager.KEY_USER_ID, bagData.getOwnerUserId());
                dataSet.put(ApiDataInflation.KEY_BAG_ID, bagData.getOwnerBagId());
                dataSet.put(KEY_BAG_POSITION, position+"");
                dataSet.put(MatchingDialog.KEY_BAG_IMAGE1, bagData.getBagImages().get(0).getBagUrl());
                dataSet.put(MatchingDialog.KEY_BAG_IMAGE2, bagData.getExchangeBagImage());
                dataSet.put(MatchingDialog.KEY_EXCHANGE_BAG_ID, bagData.getExchangeBagId()+"");

                dialogHelper.showProgressDialog();
                ApiDataInflation dataInflation = new ApiDataInflation(getActivity());
                dataInflation.acceptPetitionRequest(dataSet, mListener);

            }

            @Override
            public void onClickNo() {

                Toast.makeText(getContext(), "You clicked no.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * Petition listener after api call
     */
    private ApiDataInterface.PetitionListener mListener = new ApiDataInterface.PetitionListener() {
        @Override
        public void onPetitionAccept(boolean status, HashMap<String, String> dataBag) {

            dialogHelper.closeDialog();
            if(status){

                //hide petition, and notify data set change
                dataSet.remove(Integer.parseInt(dataBag.get(KEY_BAG_POSITION)));
                petitionAdapter.notifyDataSetChanged();

                //show matching dialog
                MatchingDialog md = new MatchingDialog();
                md.setOnMatchListener(matchingDialogListener);
                Bundle args = new Bundle();
                args.putSerializable(MatchingDialog.KEY_BUNDLE_MATCHING_DIALOG, dataBag);
                md.setArguments(args);
                android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                md.show(fragmentManager, "MD");

            }else{

                Toast.makeText(getContext(), "Error while accepting petition request. Please Try again!", Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * Matching dialog listener
     */
    private MatchingDialog.OnMatchListener matchingDialogListener = new MatchingDialog.OnMatchListener() {
        @Override
        public void onDarkClicked() {

            //open chat window
            Toast.makeText(getContext(), "Open Chat Window", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onLightClicked() {

            //do nothing
            Toast.makeText(getContext(), "Keep Scrolling", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bagList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                DataBags dataBag = (DataBags) bagList.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), BagDetailsActivity.class);
                intent.putExtra(BagDetailsActivity.KEY_CALLED_FROM, BagDetailsActivity.VAL_PETITION);
                intent.putExtra(BagDetailsActivity.DATA_BAG, dataBag);
                intent.putExtra(KEY_BAG_POSITION, position);
                startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DataBags dataBag = (DataBags) data.getSerializableExtra(BagDetailsActivity.DATA_BAG);
        int position = data.getIntExtra(KEY_BAG_POSITION, -1);
        if(position == -1){

            Toast.makeText(getContext(), "Error code -1", Toast.LENGTH_SHORT).show();

        }else{

            alertOnPetitionAccept(dataBag, position);

        }

    }

    /**
     * Get All Petition api call
     */
    private void getAllPetitions() {

        if(ApiClient.isNetworkAvailable(getActivity())){

            allPetitionsApi();

        }else{

            Toast.makeText(getActivity(), MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void allPetitionsApi() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //dialogHelper.showProgressDialog();
        swipeRefresh.setRefreshing(true);

        SessionManager sessionManager = new SessionManager(getContext());
        String publicKey = sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC);
        String privateKey = sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE);

        Call<SearchBagResponse> call = apiService.getAllPetitions(publicKey, privateKey);

        call.enqueue(new Callback<SearchBagResponse>() {
            @Override
            public void onResponse(Call<SearchBagResponse> call, Response<SearchBagResponse> response) {

                //dialogHelper.closeDialog();
                swipeRefresh.setRefreshing(false);

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        dataSet.clear();
                        dataSet.addAll(getGoldenBags(response.body().getBag(), response.body().getAds()));
                        //Collections.reverse(dataSet);

                        Log.d("TAG", "onResponse: "+dataSet.size());

                        if(!dataSet.isEmpty()){

                            petitionAdapter.notifyDataSetChanged();
                            checkViewEmpty(3);

                        }else{

                            checkViewEmpty(2);
                            Toast.makeText(getActivity(), "No Petitions Found!", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        checkViewEmpty(2);
                    }

                }

            }
            @Override
            public void onFailure(Call<SearchBagResponse> call, Throwable t) {

                swipeRefresh.setRefreshing(false);
                t.printStackTrace();

            }
        });
    }


    private ArrayList<DataBags> getGoldenBags(List<zuzusoft.com.bagbag.home.models.search.Bag> bagDataList, List<Ad> adList) {

        int count = 0;
        int adInsertCount = 1;
        int adCount = 0;
        ArrayList<DataBags> dataSet = new ArrayList<>();

        /*do{

            DataBags b1 = new DataBags();

            if(adInsertCount > 2){

                if(adCount < adList.size() ){

                    //get next ad
                    b1.setBagAdImage(adList.get(adCount).getBagImage());
                    b1.setBagBagAd(true);
                    b1.setBagTitle(adList.get(adCount).getBagTitle());
                    b1.setBrandName(adList.get(adCount).getBrandName());
                    b1.setBagDescription(adList.get(adCount).getDescription());

                    dataSet.add(b1);
                    adInsertCount = 1;
                    adCount++;

                }else{

                    //restart ad count
                    adCount = 0;
                    b1.setBagAdImage(adList.get(adCount).getBagImage());
                    b1.setBagBagAd(true);
                    b1.setBagTitle(adList.get(adCount).getBagTitle());
                    b1.setBrandName(adList.get(adCount).getBrandName());
                    b1.setBagDescription(adList.get(adCount).getDescription());

                    dataSet.add(b1);
                    adInsertCount = 1;
                    adCount++;

                }

            }else{

                b1.setBagImages(bagDataList.get(count).getBagAllimage());
                b1.setBagOwnerName(bagDataList.get(count).getUserName());
                b1.setBagBagAd(false);
                b1.setDistance("a "+bagDataList.get(count).getDistance()+" km distance");
                b1.setOwnerImage(bagDataList.get(count).getUserImage());
                b1.setBrandName(bagDataList.get(count).getBrandName());
                b1.setOwnerUserId(bagDataList.get(count).getUserId());
                b1.setOwnerBagId(bagDataList.get(count).getBagId());
                b1.setBagMaterial(bagDataList.get(count).getMaterial());
                b1.setBagColour(bagDataList.get(count).getColour());
                b1.setBagType(bagDataList.get(count).getBagType());
                b1.setBagSize(bagDataList.get(count).getBagSize());
                b1.setBagOwnerImage(bagDataList.get(count).getUserImage());
                b1.setBagDescription(bagDataList.get(count).getBagDescription());
                b1.setExchangeUserId(bagDataList.get(count).getExchangeUserId());
                b1.setExchangeBagId(bagDataList.get(count).getExchangeBagId());
                b1.setExchangeBagImage(bagDataList.get(count).getExchangeBagImage());

                dataSet.add(b1);

                adInsertCount++;
                count++;
            }


        }while (count < bagDataList.size());*/


        for (zuzusoft.com.bagbag.home.models.search.Bag bag:
                bagDataList) {

            DataBags b1 = new DataBags();
            b1.setBagImages(bag.getBagAllimage());
            b1.setBagOwnerName(bag.getUserName());
            b1.setBagBagAd(false);
            b1.setDistance("a "+bag.getDistance()+" km distance");
            b1.setOwnerImage(bag.getUserImage());
            b1.setBrandName(bag.getBrandName());
            b1.setOwnerUserId(bag.getUserId());
            b1.setOwnerBagId(bag.getBagId());
            b1.setBagMaterial(bag.getMaterial());
            b1.setBagColour(bag.getColour());
            b1.setBagType(bag.getBagType());
            b1.setBagSize(bag.getBagSize());
            b1.setBagOwnerImage(bag.getUserImage());
            b1.setBagDescription(bag.getBagDescription());
            b1.setExchangeUserId(bag.getExchangeUserId());
            b1.setExchangeBagId(bag.getExchangeBagId());
            b1.setExchangeBagImage(bag.getExchangeBagImage());

            dataSet.add(b1);

        }

        return dataSet;
    }

    private void checkViewEmpty(int mode){

        switch (mode){

            case 1:
                //default, hide both listview and empty view
                bagList.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);

                break;


            case 2:
                //when my closet empty
                bagList.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

                break;


            case 3:
                //when my closet not empty
                bagList.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                break;
        }

    }

}
