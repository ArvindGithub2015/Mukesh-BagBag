package zuzusoft.com.bagbag.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.BagDetailsActivity;
import zuzusoft.com.bagbag.custom_views.my_listview.MyListView;
import zuzusoft.com.bagbag.custom_views.my_listview.OnDetectScrollListener;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.home.adapters.BrandListAdapter;
import zuzusoft.com.bagbag.home.adapters.GoldAdapter;
import zuzusoft.com.bagbag.home.models.DataBags;
import zuzusoft.com.bagbag.home.models.brand.Brand;
import zuzusoft.com.bagbag.home.models.gold.GoldBag;
import zuzusoft.com.bagbag.rest.ApiDataInflation;
import zuzusoft.com.bagbag.rest.ApiDataInterface;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.inflate;

/**
 * Created by mukeshnarayan on 25/11/18.
 */

public class GoldFragment extends Fragment {

    @BindView(R.id.bagList)
    ListView bagList;

    @BindView(R.id.filterContainer)
    LinearLayout filterContainer;

    @BindView(R.id.brandSpinner)
    Spinner brandSpinner;

    @BindView(R.id.emptyView)
    LinearLayout emptyView;

    private DialogHelper dialogHelper;

    private String brandId;

    private boolean justOnce = true;
    private boolean fromSearch = false;
    private DataBags dataBag;
    private List<Brand> brandListData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gold, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //spread butter
        ButterKnife.bind(this, view);

        dialogHelper = new DialogHelper(getActivity());
        dialogHelper.setDialogInfo("", "Please Wait...");

        checkViewEmpty(1);

        brandSpinnerListener();

        //set on scrolling listener for ListView
        bagList.setOnScrollListener(onScrollListener());

        getAllBrands();
    }

    private void brandSpinnerListener() {

        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                zuzusoft.com.bagbag.home.models.brand.Brand  selectedItem = (zuzusoft.com.bagbag.home.models.brand.Brand ) parent.getItemAtPosition(position); //this is your selected item
                if(!fromSearch){
                    brandId = selectedItem.getBrandId();

                    Log.d(TAG, "onItemSelectedmmmmmmkn: "+brandId);

                }else{

                    brandSpinner.setSelection(getSelectedBrandIdIndex());
                }

                Log.d(TAG, "onItemSelected: "+brandId);
                fecthGoldBags();
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getSelectedBrandIdIndex() {

        int index = 0;
        for (Brand brand:
        brandListData) {

            if(brand.getBrandId().equalsIgnoreCase(brandId)){

                break;
            }

            index++;
        }

        return index;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bagList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), BagDetailsActivity.class);
                //getActivity().startActivity(intent);

            }
        });

    }

    private void getAllBrands(){

        SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> dataSet = new HashMap<>();
        dataSet.put(SessionManager.KEY_PUBLIC, sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC));
        dataSet.put(SessionManager.KEY_PRIVATE, sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));

        dialogHelper.showProgressDialog();
        ApiDataInflation dataInflation = new ApiDataInflation(getContext());
        dataInflation.fetchBrandList(dataSet, goldListener);

    }
    private void fecthGoldBags(){

        if(!justOnce){

            dialogHelper.showProgressDialog();

        }
        SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> dataSet = new HashMap<>();
        dataSet.put(SessionManager.KEY_PUBLIC, sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC));
        dataSet.put(SessionManager.KEY_PRIVATE, sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));
        dataSet.put(ApiDataInflation.KEY_BRAND_ID, brandId);

        dialogHelper.showProgressDialog();
        ApiDataInflation dataInflation = new ApiDataInflation(getContext());
        dataInflation.fetchGoldList(dataSet, goldListener);
    }

    public OnScrollListener onScrollListener() {
        return new OnScrollListener() {

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
                        Log.i("BAGBAG", "top reached");
                        filterContainer.setVisibility(View.VISIBLE);
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem) {
                    View v = bagList.getChildAt(totalItemCount - 1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the bottom: visible header and footer
                        Log.i("BAGBAG", "bottom reached!");
                        filterContainer.setVisibility(View.GONE);
                    }
                } else if (totalItemCount - visibleItemCount > firstVisibleItem){
                    // on scrolling
                    filterContainer.setVisibility(View.GONE);
                    Log.i("BAGBAG", "on scroll");
                }
            }
        };
    }

    ApiDataInterface.FetchGoldListener goldListener = new ApiDataInterface.FetchGoldListener() {
        @Override
        public void onFetchGold(List<GoldBag> goldBags) {

            dialogHelper.closeDialog();

            if(goldBags != null && !goldBags.isEmpty()){

                bagList.setAdapter(new GoldAdapter(getContext(), goldBags));
                checkViewEmpty(3);

            }else {

                checkViewEmpty(2);
            }

        }

        @Override
        public void onFetchBrand(List<Brand> brandList) {

            dialogHelper.closeDialog();
            if(justOnce){

                if(brandList != null && !brandList.isEmpty()){

                    brandListData.clear();
                    brandListData = brandList;

                    //update spinner
                    brandSpinner.setAdapter(new BrandListAdapter(getContext(), brandListData));
                    //brandId = brandList.get(0).getBrandId();

                }else{


                }

            }

        }
    };

    private ArrayList<DataBags> getGoldenBags() {

        ArrayList<DataBags> dataSet = new ArrayList<>();

        DataBags b1 = new DataBags();
        b1.setBagFileName("golden1");

        DataBags b2 = new DataBags();
        b2.setBagFileName("golden2");

        DataBags b3 = new DataBags();
        b3.setBagFileName("golden3");

        DataBags b4 = new DataBags();
        b4.setBagFileName("golden4");

        DataBags b5 = new DataBags();
        b5.setBagFileName("golden5");

        DataBags b6 = new DataBags();
        b6.setBagFileName("golden6");

        DataBags b7 = new DataBags();
        b7.setBagFileName("golden7");

        DataBags b8 = new DataBags();
        b8.setBagFileName("golden8");

        dataSet.add(b1);
        dataSet.add(b2);
        dataSet.add(b3);
        dataSet.add(b4);
        dataSet.add(b5);
        dataSet.add(b6);
        dataSet.add(b7);
        dataSet.add(b8);

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

    public void updateNewData(DataBags dataBag){

        fromSearch = true;
        brandId = dataBag.getBrandId();
        this.dataBag = dataBag;
        Log.d(TAG, "updateNewData: "+brandId);

    }

    /*private int getBrandItemIndex() {

        brandSpinner
        String strArr[] = getResources().getStringArray(R.array.color);
        int index = Arrays.asList(strArr).indexOf(previousBagColour);
        return index;
    }*/

}
