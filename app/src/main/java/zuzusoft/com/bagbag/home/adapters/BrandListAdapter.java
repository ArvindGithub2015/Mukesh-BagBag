package zuzusoft.com.bagbag.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zuzusoft.com.bagbag.home.models.brand.Brand;

public class BrandListAdapter extends BaseAdapter {

    private List<Brand> goldBags;
    private LayoutInflater inflater;

    public BrandListAdapter(Context context, List<Brand> goldBags){

        this.goldBags = goldBags;
        this.inflater = LayoutInflater.from(context);

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

            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            mHolder = new ViewHolder();

            mHolder.text1 = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();
        }

        Brand brand = (Brand) getItem(position);

        if(brand != null){

            mHolder.text1.setText(brand.getBrandName());

        }

        return convertView;
    }

    class ViewHolder{

        TextView text1;
    }
}
