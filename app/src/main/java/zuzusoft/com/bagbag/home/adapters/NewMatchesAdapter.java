package zuzusoft.com.bagbag.home.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.home.models.chat.NewMatch;

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.ViewHolder> {

    private List<NewMatch> newMatchList;
    private Context context;
    private OnThumbnailClickListener mListener;

    public interface ItemClickListener {

        void onClick(View view, int position, boolean isLongClick);
    }

    public interface OnThumbnailClickListener {

        void onThumbnailClick(NewMatch bagData, int position);
    }

    public NewMatchesAdapter(Context context, List<NewMatch> newMatchList) {
        //super();
        this.context = context;

        this.newMatchList = newMatchList;
        //this.mListener = (OnThumbnailClickListener)context;

    }

    public void setOnThumbnailClickListener(OnThumbnailClickListener thumbClick){

        this.mListener = thumbClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_new_match, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final NewMatch chatData = newMatchList.get(i);

        //update all new matches
        Log.v("Bag Image123", "image: "+chatData.getRosterBag().getMyBagAllimage().get(0).getBagUrl());
        Glide.with(context).load(chatData.getRosterBag().getMyBagAllimage().get(0).getBagUrl()).into(viewHolder.bagImage);
        if(chatData.getChatStatus() == 2){

            //hide indicator
            viewHolder.indicator.setVisibility(View.GONE);

        }else if(chatData.getChatStatus() == 1){

            viewHolder.indicator.setVisibility(View.VISIBLE);

        }

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                //trigger actions related to image
                if(mListener != null){

                    mListener.onThumbnailClick(chatData, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newMatchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView bagImage, indicator;
        private ItemClickListener clickListener;

        private ViewHolder(View itemView) {
            super(itemView);
            bagImage = (ImageView) itemView.findViewById(R.id.bagImage);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
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