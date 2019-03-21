package zuzusoft.com.bagbag.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.BagDetailsActivity;
import zuzusoft.com.bagbag.closet.BagSelectionActivity;
import zuzusoft.com.bagbag.closet.BagSelectionPetitonChangeActivity;
import zuzusoft.com.bagbag.closet.MatchingDialog;
import zuzusoft.com.bagbag.helper.MknHelper;
import zuzusoft.com.bagbag.home.HomeActivity;
import zuzusoft.com.bagbag.home.models.DataBags;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class BagListAdapter extends BaseAdapter {

    private ArrayList<DataBags> dataSet;
    private LayoutInflater inflater;
    private Context context;

    private PetitionListener mListener;
    public interface PetitionListener{

        void onPetitionAccept(DataBags bagData, int position);
        void onPetitionHide(DataBags bagData, int position);
    }

    public BagListAdapter(Context context, ArrayList<DataBags> dataSet){

        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.context = context;

    }

    public void setOnPetitionListener(PetitionListener listener){

        this.mListener = listener;
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
        return dataSet.indexOf(dataSet.get(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_patetion, parent, false);
            mHolder = new ViewHolder();

            mHolder.popupMenu = (ImageView) convertView.findViewById(R.id.popupMenu);
            mHolder.btnAcceptChange = convertView.findViewById(R.id.btnAcceptChange);
            mHolder.profileImage = convertView.findViewById(R.id.profileImage);
            mHolder.ownerName = convertView.findViewById(R.id.ownerName);
            mHolder.distance = convertView.findViewById(R.id.distance);
            mHolder.bagImage = convertView.findViewById(R.id.bagImage);
            mHolder.exchangeBage = convertView.findViewById(R.id.exchangeBage);
            mHolder.brandName = convertView.findViewById(R.id.brandName);

            mHolder.bagMaterial = (TextView) convertView.findViewById(R.id.bagMaterial);
            mHolder.bagColor = (TextView) convertView.findViewById(R.id.bagColor);
            mHolder.bagSize = (TextView) convertView.findViewById(R.id.bagSize);
            mHolder.bagType = (TextView) convertView.findViewById(R.id.bagType);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();
        }

        final DataBags bag = (DataBags) getItem(position);

        if(bag != null){

            Glide.with(context)
                    .load(bag.getBagImages().get(0).getBagUrl())
                    // .placeholder(R.raw.default_profile)
                    .into(mHolder.bagImage);

            Glide.with(context)
                    .load(bag.getExchangeBagImage())
                    // .placeholder(R.raw.default_profile)
                    .into(mHolder.exchangeBage);

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

                    showPopupMenu(mHolder.popupMenu, bag, position);
                }
            });

            mHolder.btnAcceptChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener != null){

                        mListener.onPetitionAccept(bag, position);

                    }else {

                        Toast.makeText(context, "mListener Null ACCEPT", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        return convertView;
    }


    class ViewHolder{

        CircleImageView profileImage;
        TextView ownerName, distance, btnAcceptChange, brandName;
        ImageView bagImage, popupMenu, exchangeBage;
        TextView bagMaterial;
        TextView bagColor;
        TextView bagSize;
        TextView bagType;
    }


    PowerMenu pm;
    private void showPopupMenu(ImageView view, final DataBags bag, final int bagPosition){

        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {

                if(mListener != null){

                    mListener.onPetitionHide(bag, bagPosition);

                }else {

                    Toast.makeText(context, "mListener Null Menu", Toast.LENGTH_SHORT).show();
                }

                pm.dismiss();
            }
        };

        final PowerMenu.Builder powerMenu = new PowerMenu.Builder(context);
        powerMenu.addItem(new PowerMenuItem("Hide", false));
        powerMenu.setAnimation(MenuAnimation.SHOWUP_TOP_LEFT); // Animation start point (TOP | LEFT)
        powerMenu.setMenuRadius(10f);
        powerMenu.setMenuShadow(10f);
        powerMenu.setSelectedTextColor(Color.WHITE);
        powerMenu.setMenuColor(Color.WHITE);
        powerMenu.setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary));
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        pm = powerMenu.build();
        pm.showAsDropDown(view);

    }

}
