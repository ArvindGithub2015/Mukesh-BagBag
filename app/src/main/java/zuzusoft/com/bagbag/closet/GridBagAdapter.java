package zuzusoft.com.bagbag.closet;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.home.models.DataBags;

/**
 * Created by mukeshnarayan on 19/11/18.
 */

public class GridBagAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;

    private ArrayList<DataBags> dataSet;

    private OnBagSelectListener mListener;
    public interface OnBagSelectListener{

        void onSelect(int position, String bagId);
        void onUnSelect(int position, String bagId);
    }

    public GridBagAdapter(Context context, ArrayList<DataBags> dataSet){

        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.dataSet = dataSet;
        this.mListener = (OnBagSelectListener) context;
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

            convertView = inflater.inflate(R.layout.row_select_bag, parent, false);
            mHolder = new ViewHolder();

            mHolder.bagImage = convertView.findViewById(R.id.bagImage);
            mHolder.selectIndicator = convertView.findViewById(R.id.selectIndicator);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();
        }


        final DataBags bag = (DataBags) getItem(position);

        if(bag != null){

            Glide.with(context)
                    .load(bag.getBagFileName())
                    // .placeholder(R.raw.default_profile)
                    .into(mHolder.bagImage);

            if(bag.isSelect()){

                mHolder.selectIndicator.setVisibility(View.VISIBLE);

            }else{

                mHolder.selectIndicator.setVisibility(View.GONE);
            }

            mHolder.bagImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(bag.isSelect()){

                        bag.setSelect(false);
                        mHolder.selectIndicator.setVisibility(View.GONE);

                        if(mListener != null){

                            mListener.onUnSelect(position, bag.getOwnerBagId());
                        }

                    }else{

                        bag.setSelect(true);
                        mHolder.selectIndicator.setVisibility(View.VISIBLE);

                        if(mListener != null){

                            mListener.onSelect(position, bag.getOwnerBagId());
                        }
                    }

                }
            });

        }

        return convertView;
    }

    class ViewHolder{

        ImageView bagImage;
        ImageView selectIndicator;
    }

    private int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "raw", context.getPackageName());

        return drawableResourceId;
    }

}
