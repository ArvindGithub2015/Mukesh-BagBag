package zuzusoft.com.bagbag.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.gold.GoldDialog;
import zuzusoft.com.bagbag.helper.MknHelper;
import zuzusoft.com.bagbag.home.HomeActivity;
import zuzusoft.com.bagbag.home.models.DataBags;
import zuzusoft.com.bagbag.home.models.gold.GoldBag;

/**
 * Created by mukeshnarayan on 25/11/18.
 */

public class GoldAdapter extends BaseAdapter {

    private List<GoldBag> goldBags;
    private LayoutInflater inflater;

    private Context context;

    public GoldAdapter(Context context, List<GoldBag> goldBags){

        this.inflater = LayoutInflater.from(context);
        this.goldBags = goldBags;
        this.context = context;

    }

    @Override
    public int getCount() {
        return goldBags.size();
    }

    @Override
    public Object getItem(int position) {
        return goldBags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return goldBags.indexOf(goldBags.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_gold, parent, false);
            mHolder = new ViewHolder();

            mHolder.btnMatch = convertView.findViewById(R.id.btnMatch);
            mHolder.bagImage = convertView.findViewById(R.id.bagImage);
            mHolder.brandName = convertView.findViewById(R.id.brandName);
            mHolder.bagMaterial = convertView.findViewById(R.id.bagMaterial);
            mHolder.bagColor = convertView.findViewById(R.id.bagColor);
            mHolder.bagSize = convertView.findViewById(R.id.bagSize);
            mHolder.bagType = convertView.findViewById(R.id.bagType);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();
        }


        final GoldBag gold = (GoldBag) getItem(position);
        final GoldBag bag = (GoldBag) getItem(position);

        Log.v("Bag Name", "Image: "+gold.getBagAllimage().get(0).getBagUrl());

        if(gold != null){

            Glide.with(context)
                    .load(gold.getBagAllimage().get(0).getBagUrl())
                    // .placeholder(R.raw.default_profile)
                    .into(mHolder.bagImage);

            mHolder.brandName.setText(gold.getBrandName());
            mHolder.bagMaterial.setText(gold.getMaterial());
            mHolder.bagColor.setText(gold.getColour());
            mHolder.bagSize.setText(gold.getBagSize());
            mHolder.bagType.setText(gold.getBagType());

            mHolder.btnMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gold.getAdUrl()));
                    context.startActivity(browserIntent);
                    GoldDialog md = new GoldDialog();
                    android.app.FragmentManager fragmentManager = ((HomeActivity)context).getFragmentManager();
                    //md.show(fragmentManager, "MD");

                }
            });

        }

        return convertView;
    }


    class ViewHolder{

        TextView btnMatch, brandName, bagMaterial, bagColor, bagSize, bagType;
        ImageView bagImage;
    }

}