package zuzusoft.com.bagbag.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.home.models.search.BagColor;

public class BagColorAdapter extends BaseAdapter {

    private ArrayList<BagColor> dataSet;
    private LayoutInflater inflater;

    public BagColorAdapter(Context context, ArrayList<BagColor> dataSet){

        this.dataSet = dataSet;
        this.inflater = LayoutInflater.from(context);
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

            convertView = inflater.inflate(R.layout.row_bag_color, parent, false);
            mHolder = new ViewHolder();

            mHolder.bagColor = (TextView) convertView.findViewById(R.id.bagColor);
            mHolder.bagColorBubble = (ImageView) convertView.findViewById(R.id.bagColorBubble);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();

        }

        BagColor bagColorData = (BagColor) getItem(position);

        if(bagColorData != null){

            mHolder.bagColor.setText(bagColorData.getBagColor());
            mHolder.bagColorBubble.setImageResource(bagColorData.getColorCode());

        }

        return convertView;
    }

    class ViewHolder {

        TextView bagColor;
        ImageView bagColorBubble;

    }
}
