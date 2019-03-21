package zuzusoft.com.bagbag.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.BagDetailsActivity;
import zuzusoft.com.bagbag.closet.BagSelectionActivity;
import zuzusoft.com.bagbag.helper.MknHelper;
import zuzusoft.com.bagbag.helper.custom_views.CustomFontTextView;
import zuzusoft.com.bagbag.home.SearchBagsFragment;
import zuzusoft.com.bagbag.home.models.DataBags;

/**
 * Created by mukeshnarayan on 09/11/18.
 */

public class SearchBagsAdapter extends BaseAdapter {

    private ArrayList<DataBags> dataSet;
    private LayoutInflater inflater;

    private int type;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private Context context;
    private SearchBagsFragment.OnAdClickListener mListener;

    public SearchBagsAdapter(Context context, ArrayList<DataBags> dataSet, SearchBagsFragment.OnAdClickListener mListener){

        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.context = context;
        this.mListener = mListener;

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        if(!dataSet.get(position).isBagBagAd()){

            type = TYPE_ITEM;

        }else if(dataSet.get(position).isBagBagAd()){

            type = TYPE_SEPARATOR;

        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        int type = getItemViewType(position);

        final DataBags bag = (DataBags) getItem(position);

        if(convertView == null){
            mHolder = new ViewHolder();

            if(type == TYPE_ITEM){

                convertView = inflater.inflate(R.layout.row_search_bags, parent, false);

                mHolder.popupMenu = (ImageView) convertView.findViewById(R.id.popupMenu);
                mHolder.btnMatch = convertView.findViewById(R.id.btnMatch);
                mHolder.profileImage = convertView.findViewById(R.id.profileImage);
                mHolder.ownerName = convertView.findViewById(R.id.ownerName);
                mHolder.distance = convertView.findViewById(R.id.distance);
                mHolder.bagImage = convertView.findViewById(R.id.bagImage);
                mHolder.brandName = convertView.findViewById(R.id.brandName);

                mHolder.bagMaterial = (TextView) convertView.findViewById(R.id.bagMaterial);
                mHolder.bagColor = (TextView) convertView.findViewById(R.id.bagColor);
                mHolder.bagSize = (TextView) convertView.findViewById(R.id.bagSize);
                mHolder.bagType = (TextView) convertView.findViewById(R.id.bagType);

            }else if(type == TYPE_SEPARATOR){

                convertView = inflater.inflate(R.layout.row_ad_bagbag, parent, false);

                mHolder.btnAdd = convertView.findViewById(R.id.btnAdd);
                mHolder.bagAdImage = convertView.findViewById(R.id.bagAdImage);
                mHolder.bagTitle = (CustomFontTextView) convertView.findViewById(R.id.bagTitle);
                mHolder.bagDescription = (CustomFontTextView) convertView.findViewById(R.id.bagDescription);
                mHolder.brandName = convertView.findViewById(R.id.brandName);
                mHolder.adCrousels = convertView.findViewById(R.id.adCrousels);
                mHolder.adCrousels.setDuration(1000);


            }

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();
        }


        if(bag != null){

            if(!bag.isBagBagAd()){

                Log.v("Search Bag Image", bag.getBagImages().get(0).getBagUrl());

                Glide.with(context)
                        .load(bag.getBagImages().get(0).getBagUrl())
                        // .placeholder(R.raw.default_profile)
                        .into(mHolder.bagImage);

                Glide.with(context)
                        .load(bag.getOwnerImage())
                        // .placeholder(R.raw.default_profile)
                        .into(mHolder.profileImage);

                mHolder.ownerName.setText(bag.getBagOwnerName());
                mHolder.distance.setText(bag.getDistance());
                mHolder.brandName.setText(bag.getBrandName());
                mHolder.bagMaterial.setText(bag.getBagMaterial());
                mHolder.bagColor.setText(bag.getBagColour());
                mHolder.bagSize.setText(bag.getBagSize());
                mHolder.bagType.setText(bag.getBagType());

                mHolder.popupMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Toast.makeText(context, "Hello!", Toast.LENGTH_SHORT).show();
                        showPopupMenu(mHolder.popupMenu);
                    }
                });

                mHolder.btnMatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, BagSelectionActivity.class);
                        intent.putExtra(BagDetailsActivity.DATA_BAG, bag);
                        context.startActivity(intent);

                    }
                });

            }else{

                Log.d("Search Brand Id", "getView: "+ bag.getBrandId());

                for (TextSliderView sliderView:
                     bag.getAdSliderData()) {
                    mHolder.adCrousels.addSlider(sliderView);
                }

                mHolder.brandName.setText(bag.getBrandName());
                mHolder.bagTitle.setText(bag.getBagTitle());
                mHolder.bagDescription.setText(bag.getBagDescription());

                mHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //go to premium part
                        if(mListener != null){

                            mListener.onAdClicked(bag);

                        }

                    }
                });

                /*convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //go to premium part
                        if(mListener != null){

                            mListener.onAdClicked(bag);

                        }

                    }
                });*/

            }

        }

        return convertView;
    }


    private void showPopupMenu(ImageView view){

        //List

        PowerMenu powerMenu = new PowerMenu.Builder(context)
                //.addItemList(list) // list has "Novel", "Poerty", "Art"
                .addItem(new PowerMenuItem("Report", false))
                .addItem(new PowerMenuItem("Block", false))
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                //.setTextColor(context.getResources().getColor(R.color.md_grey_800))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary))
                //.setOnMenuItemClickListener(onMenuItemClickListener)
                .build();

        powerMenu.showAsDropDown(view);
    }

    class ViewHolder{

        CircleImageView profileImage;
        TextView ownerName, distance, btnMatch, btnAdd, brandName;
        CustomFontTextView bagTitle, bagDescription;
        ImageView bagImage, popupMenu, bagAdImage;
        TextView bagMaterial;
        TextView bagColor;
        TextView bagSize;
        TextView bagType;
        SliderLayout adCrousels;

    }
}
