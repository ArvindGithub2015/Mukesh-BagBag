package zuzusoft.com.bagbag.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.AddBagActivity;
import zuzusoft.com.bagbag.closet.BagProfileEditionActivity;
import zuzusoft.com.bagbag.closet.BagSelectionActivity;
import zuzusoft.com.bagbag.helper.MknHelper;
import zuzusoft.com.bagbag.home.models.DataBags;
import zuzusoft.com.bagbag.home.models.my_closet.Bag;

/**
 * Created by mukeshnarayan on 17/11/18.
 */

public class MyClosetAdapter extends BaseAdapter {

    private List<Bag> dataSet;
    private LayoutInflater inflater;
    private Context context;

    public MyClosetAdapter(Context context, List<Bag> dataSet){

        this.inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.context = context;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_closet, parent, false);
            mHolder = new ViewHolder();

            mHolder.btnSelectBag = (TextView) convertView.findViewById(R.id.btnSelectBag);
            mHolder.brandName = (TextView) convertView.findViewById(R.id.brandName);
            mHolder.imageContainer = (FrameLayout) convertView.findViewById(R.id.imageContainer);
            mHolder.bagImage = (ImageView) convertView.findViewById(R.id.bagImage);

            mHolder.bagMaterial = (TextView) convertView.findViewById(R.id.bagMaterial);
            mHolder.bagColor = (TextView) convertView.findViewById(R.id.bagColor);
            mHolder.bagSize = (TextView) convertView.findViewById(R.id.bagSize);
            mHolder.bagType = (TextView) convertView.findViewById(R.id.bagType);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();
        }

        final Bag bag = (Bag) getItem(position);

        if(bag != null){

            if(bag.getBagAllimage() != null){

                if(!bag.getBagAllimage().isEmpty()){

                    Glide.with(context)
                            .load(bag.getBagAllimage().get(0).getBagUrl())
                            // .placeholder(R.raw.default_profile)
                            .into(mHolder.bagImage);

                }else{

                    Glide.with(context)
                            .load(MknHelper.getImageDrawable(context, MknHelper.NO_BAG_IMAGE_FILE_NAME))
                            // .placeholder(R.raw.default_profile)
                            .into(mHolder.bagImage);
                }
            }

            mHolder.brandName.setText(bag.getBrandName());
            mHolder.bagMaterial.setText(bag.getMaterial());
            mHolder.bagColor.setText(bag.getColour());
            mHolder.bagSize.setText(bag.getBagSize());
            mHolder.bagType.setText(bag.getBagType());

            mHolder.btnSelectBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!bag.getBagAllimage().isEmpty()){

                        Intent intent = new Intent(context, BagProfileEditionActivity.class);
                        intent.putExtra(BagProfileEditionActivity.KEY_SEIRAL_DATA, bag);
                        intent.putExtra(BagProfileEditionActivity.KEY_FROM_MY_CLOSET, true);
                        context.startActivity(intent);

                    }else{

                        //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            mHolder.imageContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!bag.getBagAllimage().isEmpty()){

                        Intent intent = new Intent(context, BagProfileEditionActivity.class);
                        intent.putExtra(BagProfileEditionActivity.KEY_SEIRAL_DATA, bag);
                        intent.putExtra(BagProfileEditionActivity.KEY_FROM_MY_CLOSET, true);
                        context.startActivity(intent);

                    }else{


                    }

                }
            });
        }

        return convertView;
    }

    class ViewHolder{

        TextView btnSelectBag, brandName;
        FrameLayout imageContainer;
        ImageView bagImage;
        TextView bagMaterial;
        TextView bagColor;
        TextView bagSize;
        TextView bagType;

    }

}
