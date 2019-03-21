package zuzusoft.com.bagbag.closet.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.model.DataAddBagImage;
import zuzusoft.com.bagbag.helper.MknHelper;
import zuzusoft.com.bagbag.home.models.DataBags;

/**
 * Created by mukeshnarayan on 26/11/18.
 */

public class BagImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<DataAddBagImage> dataBags;
    private int VIEWPAGER_ITEM_COUNT = 0;

    public BagImagePagerAdapter(Context context, ArrayList<DataAddBagImage> dataSet, int VIEWPAGER_ITEM_COUNT) {
        mContext = context;
        this.dataBags = dataSet;
        this.VIEWPAGER_ITEM_COUNT = VIEWPAGER_ITEM_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        DataAddBagImage dataBag = dataBags.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_pager_bag, collection, false);

        ImageView bagImage = layout.findViewById(R.id.bagImage);

        if(dataBag.isHasBagData()){

            Glide.with(mContext)
                    .load(dataBag.getBagImageUrl())
                    // .placeholder(R.raw.default_profile)
                    .into(bagImage);

        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return VIEWPAGER_ITEM_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //DataBags dataBag = dataBags.get(position);
        return "Bag";
    }

}