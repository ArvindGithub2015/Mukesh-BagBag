package zuzusoft.com.bagbag.closet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import zuzusoft.com.bagbag.MainActivity;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.model.DataAddBagImage;
import zuzusoft.com.bagbag.helper.MknHelper;

/**
 * Created by mukeshnarayan on 17/01/19.
 */

public class AddBagImageAdapter extends RecyclerView.Adapter<AddBagImageAdapter.ViewHolder> {

    private ArrayList<DataAddBagImage> bagImageDataList;
    private Context context;
    private OnThumbnailClickListener mListener;

    public interface ItemClickListener {

        void onClick(View view, int position, boolean isLongClick);
    }

    public interface OnThumbnailClickListener {

        void onThumbnailClick(DataAddBagImage bagData, int position);
    }

    public AddBagImageAdapter(Context context, ArrayList<DataAddBagImage> bagImageDataList) {
        super();
        this.context = context;
        this.bagImageDataList = bagImageDataList;
        this.mListener = (OnThumbnailClickListener)context;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_add_bag_image, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final DataAddBagImage imageData = bagImageDataList.get(i);

        if(imageData.isHasBagData()){

            viewHolder.imgThumbnail.setPadding(0,0,0,0);
            Glide.with(context).load(imageData.getBagImageUrl()).into(viewHolder.imgThumbnail);

        }else{

            viewHolder.imgThumbnail.setPadding(40,40,40,40);
            Glide.with(context).load(MknHelper.getImageDrawable(context, "camera")).into(viewHolder.imgThumbnail);
        }



        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                //trigger actions related to image
                if(mListener != null){

                    mListener.onThumbnailClick(imageData, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bagImageDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView imgThumbnail;
        private ItemClickListener clickListener;

        private ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.bagImage1);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

}