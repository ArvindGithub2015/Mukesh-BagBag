package zuzusoft.com.bagbag.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.BagDetailsActivity;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.home.adapters.BagColorAdapter;
import zuzusoft.com.bagbag.home.adapters.SearchBagsAdapter;
import zuzusoft.com.bagbag.home.models.DataBags;
import zuzusoft.com.bagbag.home.models.search.Ad;
import zuzusoft.com.bagbag.home.models.search.BagAllimage;
import zuzusoft.com.bagbag.home.models.search.BagColor;
import zuzusoft.com.bagbag.home.models.search.BagImage;
import zuzusoft.com.bagbag.home.models.search.SearchBagResponse;
import zuzusoft.com.bagbag.rest.ApiClient;
import zuzusoft.com.bagbag.rest.ApiInterface;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class SearchBagsFragment extends Fragment {

    public static boolean KEY_REFRESH_SEARCH_BAGS = false;

    @BindView(R.id.spinnerBagType)
    Spinner spinnerBagType;

    @BindView(R.id.spinnerColor)
    Spinner spinnerColor;

    @BindView(R.id.spinnerDistance)
    Spinner spinnerDistance;

    @BindView(R.id.filterContainer)
    LinearLayout filterContainer;

    @BindView(R.id.bagList)
    ListView bagList;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.emptyView)
    LinearLayout emptyView;

    private DialogHelper dialogHelper;

    private boolean justOnce = false;

    private String FILTER_VAL_BAG_TYPE = "All Types";
    private String FILTER_VAL_BAG_COLOUR = "All Colors";
    private String FILTER_VAL_BAG_DISTANCE = "10";

    private OnAdClickListener mListener;
    public interface OnAdClickListener{

        void onAdClicked(DataBags dataBag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (OnAdClickListener) context;

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

        if(KEY_REFRESH_SEARCH_BAGS){

            getAllSearchBags();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_bags, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //spread butter
        ButterKnife.bind(this, view);

        dialogHelper = new DialogHelper(getActivity());
        dialogHelper.setDialogInfo("", "Please Wait...");

        //filter spinners
        updateFilterSpinners();

        //set on scrolling listener for ListView
        bagList.setOnScrollListener(onScrollListener());

        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        getAllSearchBags();
                    }
                });

        showCurrentFilterValues();

        //get all bags when screen loaded
        getAllSearchBags();

    }

    private void updateFilterSpinners() {

        //bag color
        ArrayList<BagColor> bagColorList = getBagColorList();
        BagColorAdapter bagColorAdapter = new BagColorAdapter(getContext(), bagColorList);
        spinnerColor.setAdapter(bagColorAdapter);

    }

    private ArrayList<String> getBagTypeList() {

        ArrayList dataSpinner = new ArrayList();

        dataSpinner.add("All Types");
        dataSpinner.add("Another Type");
        dataSpinner.add("BackPack");
        dataSpinner.add("Beach Bag");
        dataSpinner.add("Crossing Bag");
        dataSpinner.add("Fanny Bag");
        dataSpinner.add("Hand Bag");
        dataSpinner.add("Party Bag");
        dataSpinner.add("Shoulder Bag");
        dataSpinner.add("Short Handle Bag");
        dataSpinner.add("Sports Bag");
        dataSpinner.add("Travel Bag");
        dataSpinner.add("Wallet");

        return dataSpinner;
    }

    private ArrayList<BagColor> getBagColorList() {

        ArrayList dataSpinner = new ArrayList();

        BagColor allColor = new BagColor("All Colors", R.drawable.cl_all_color);
        BagColor yellow = new BagColor("Yellow", R.drawable.cl_yellow);
        BagColor red = new BagColor("Red", R.drawable.cl_red);
        BagColor blue = new BagColor("Blue", R.drawable.cl_blue);
        BagColor black = new BagColor("Black", R.drawable.cl_black);
        BagColor white = new BagColor("White", R.drawable.cl_white);
        BagColor multicolour = new BagColor("Multicolour", R.drawable.cl_multicolour);
        BagColor transparent = new BagColor("Transparent", R.drawable.cl_transparent);
        BagColor gray = new BagColor("Gray", R.drawable.cl_gray);
        BagColor pink = new BagColor("Pink", R.drawable.cl_pink);
        BagColor purple = new BagColor("Purple", R.drawable.cl_purple);
        BagColor orange = new BagColor("Orange", R.drawable.cl_orange);
        BagColor brown = new BagColor("Brown", R.drawable.cl_brown);
        BagColor green = new BagColor("Green", R.drawable.cl_green);
        BagColor silver = new BagColor("Silver", R.drawable.cl_silver);
        BagColor golden = new BagColor("Golden", R.drawable.cl_golden);
        BagColor bronze = new BagColor("Bronze", R.drawable.cl_bronze);
        BagColor beige = new BagColor("Beige", R.drawable.cl_beige);
        BagColor anotherColor = new BagColor("Another color", R.drawable.other_color);


        dataSpinner.add(allColor);
        dataSpinner.add(anotherColor);
        dataSpinner.add(beige);
        dataSpinner.add(black);
        dataSpinner.add(blue);
        dataSpinner.add(bronze);
        dataSpinner.add(brown);
        dataSpinner.add(golden);
        dataSpinner.add(gray);
        dataSpinner.add(green);
        dataSpinner.add(multicolour);
        dataSpinner.add(orange);
        dataSpinner.add(pink);
        dataSpinner.add(purple);
        dataSpinner.add(red);
        dataSpinner.add(silver);
        dataSpinner.add(transparent);
        dataSpinner.add(white);
        dataSpinner.add(yellow);

        return dataSpinner;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bagList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                DataBags dataBag = (DataBags) bagList.getItemAtPosition(position);

                if(!dataBag.isBagBagAd()){

                    Intent intent = new Intent(getActivity(), BagDetailsActivity.class);
                    intent.putExtra(BagDetailsActivity.KEY_CALLED_FROM, BagDetailsActivity.VAL_SEARCH);
                    intent.putExtra(BagDetailsActivity.DATA_BAG, dataBag);
                    getActivity().startActivity(intent);

                }

            }
        });


        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(justOnce ){

                    FILTER_VAL_BAG_COLOUR = ((BagColor)parent.getItemAtPosition(position)).getBagColor();
                    showCurrentFilterValues();
                    getAllSearchBags();
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinnerBagType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(justOnce ) {

                    FILTER_VAL_BAG_TYPE = parent.getItemAtPosition(position).toString();
                    showCurrentFilterValues();
                    getAllSearchBags();
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(justOnce ) {

                    FILTER_VAL_BAG_DISTANCE = getFilterDistance(position);//parent.getItemAtPosition(position).toString();
                    showCurrentFilterValues();
                    getAllSearchBags();
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }

    private void showCurrentFilterValues() {

            Log.v("FILTER_VAL_BAG_TYPE", FILTER_VAL_BAG_TYPE);
            Log.v("FILTER_VAL_BAG_COLOUR", FILTER_VAL_BAG_COLOUR);
            Log.v("FILTER_VAL_BAG_DISTANCE", FILTER_VAL_BAG_DISTANCE);

    }

    public AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem == 0) {
                    // check if we reached the top or bottom of the list
                    View v = bagList.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the top: visible header and footer
                        //Log.i("BAGBAG", "top reached");
                        filterContainer.setVisibility(View.VISIBLE);
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem) {
                    View v = bagList.getChildAt(totalItemCount - 1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the bottom: visible header and footer
                        //Log.i("BAGBAG", "bottom reached!");
                        filterContainer.setVisibility(View.GONE);
                    }
                } else if (totalItemCount - visibleItemCount > firstVisibleItem){
                    // on scrolling
                    filterContainer.setVisibility(View.GONE);
                    //Log.i("BAGBAG", "on scroll");
                }
            }
        };
    }

    public void getAllSearchBags(){

        if(ApiClient.isNetworkAvailable(getActivity())){

            searchBagApi();

        }else{

            Toast.makeText(getActivity(), MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void searchBagApi() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //dialogHelper.showProgressDialog();
        swipeRefresh.setRefreshing(true);

        SessionManager sessionManager = new SessionManager(getContext());
        RequestBody publicKey = createPartFromString(sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC));
        RequestBody privateKey = createPartFromString(sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));
        RequestBody distance = createPartFromString(FILTER_VAL_BAG_DISTANCE);
        RequestBody bagType = createPartFromString(FILTER_VAL_BAG_TYPE);;
        RequestBody bagColor = createPartFromString(FILTER_VAL_BAG_COLOUR);;

        final HashMap<String, RequestBody> map = new HashMap<>();
        map.put("public_key", publicKey);
        map.put("private_key", privateKey);
        map.put("distance", distance);
        map.put("color", bagColor);
        map.put("type", bagType);

        Call<SearchBagResponse> call = apiService.getSeachBags(map);

        call.enqueue(new Callback<SearchBagResponse>() {
            @Override
            public void onResponse(Call<SearchBagResponse> call, Response<SearchBagResponse> response) {

                //dialogHelper.closeDialog();
                swipeRefresh.setRefreshing(false);
                justOnce = true;

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        List<zuzusoft.com.bagbag.home.models.search.Bag> dataSet = response.body().getBag();
                        List<Ad> adList = response.body().getAds();
                        Collections.reverse(dataSet);

                        if(!dataSet.isEmpty()){

                            bagList.setAdapter(new SearchBagsAdapter(getContext(), getGoldenBags(dataSet, adList), mListener));
                            checkViewEmpty(3);

                        }else{

                            checkViewEmpty(2);
                            Toast.makeText(getActivity(), "No Bags Found!", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        checkViewEmpty(2);
                        //Toast.makeText(getActivity(), "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                    }

                }

            }
            @Override
            public void onFailure(Call<SearchBagResponse> call, Throwable t) {

                //dialogHelper.closeDialog();
                swipeRefresh.setRefreshing(false);
                justOnce = true;
                t.printStackTrace();

            }
        });
    }

    private String getFilterDistance(int selectedItemPosition) {

        switch (selectedItemPosition){

            case 0:

                return "10";


            case 1:

                return "50";


            case 2:

                return "150";


            case 3:

                return "All World";


        }

        return "10";
    }

    private ArrayList<DataBags> getGoldenBags(List<zuzusoft.com.bagbag.home.models.search.Bag> bagDataList, List<Ad> adList) {

        int count = 0;
        int adInsertCount = 1;
        int adCount = 0;
        ArrayList<DataBags> dataSet = new ArrayList<>();

        do{

            DataBags b1 = new DataBags();

            if(adInsertCount > 2){

                if(adCount < adList.size() ){

                    //get next ad
                    b1.setAdBagImages(adList.get(adCount).getBagImage());
                    b1.setBagBagAd(true);
                    b1.setBagTitle(adList.get(adCount).getBagTitle());
                    b1.setBrandName(adList.get(adCount).getBrandName());
                    b1.setBagDescription(adList.get(adCount).getDescription());
                    b1.setAdSliderData(getAdSliderData(adList.get(adCount).getBagImage()));
                    b1.setAdId(adList.get(adCount).getAdId());
                    b1.setBrandId(adList.get(adCount).getBrandId());

                    dataSet.add(b1);
                    adInsertCount = 1;
                    adCount++;

                }else{

                    //restart ad count
                    adCount = 0;
                    b1.setAdBagImages(adList.get(adCount).getBagImage());
                    b1.setBagBagAd(true);
                    b1.setBagTitle(adList.get(adCount).getBagTitle());
                    b1.setBrandName(adList.get(adCount).getBrandName());
                    b1.setBagDescription(adList.get(adCount).getDescription());
                    b1.setAdSliderData(getAdSliderData(adList.get(0).getBagImage()));
                    b1.setAdId(adList.get(adCount).getAdId());
                    b1.setBrandId(adList.get(adCount).getBrandId());

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

                dataSet.add(b1);

                adInsertCount++;
                count++;
            }


        }while (count < bagDataList.size());

        return dataSet;
    }

    private List<TextSliderView> getAdSliderData(List<BagImage> adBagImages) {

        List<TextSliderView> sliderDataList = new ArrayList<>();

        for (BagImage imageUrl:
                adBagImages) {

            TextSliderView sliderView = new TextSliderView(getContext());

            sliderView
                    .image(imageUrl.getBagUrl())
                    .setRequestOption(RequestOptions.centerCropTransform())
                    .setProgressBarVisible(true);


            sliderDataList.add(sliderView);
        }


        return sliderDataList;
    }

    private RequestBody createPartFromString(String s) {
        return RequestBody.create(MediaType.parse("text/plain"), s);
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
