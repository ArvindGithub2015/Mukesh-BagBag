package zuzusoft.com.bagbag.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.home.models.chat.NewMatch;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<NewMatch> newMatchList;
    private Context mContext;

    public RecyclerViewAdapter(Context context, List<NewMatch> newMatchList) {

        mContext = context;
        this.newMatchList = newMatchList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        NewMatch chatData = newMatchList.get(position);
        final String bagImageUrl = chatData.getRosterBag().getMyBagAllimage().get(0).getBagUrl();
        Log.v(TAG, bagImageUrl);
        Glide.with(mContext)
                .asBitmap()
                .load(bagImageUrl)
                .into(holder.bagImage);

        holder.bagImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + bagImageUrl);
                Toast.makeText(mContext, bagImageUrl, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.newMatchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView bagImage, indicator;

        public ViewHolder(View itemView) {
            super(itemView);
            bagImage = itemView.findViewById(R.id.bagImage);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}
