package zuzusoft.com.bagbag.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.Date;
import java.util.List;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.helper.badge.MaterialBadgeTextView;
import zuzusoft.com.bagbag.home.models.chat.ChatList;

/**
 * Created by mukeshnarayan on 17/11/18.
 */

public class ChatListAdapter extends BaseAdapter {

    private List<ChatList> dataSet;
    private LayoutInflater inflater;
    private Context context;

    public ChatListAdapter(Context context, List<ChatList> dataSet){

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
        return position;//dataSet.indexOf(dataSet.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.row_chat_list, parent, false);
            mHolder = new ViewHolder();

            mHolder.bagImage =  convertView.findViewById(R.id.bagImage);
            mHolder.ownerName = convertView.findViewById(R.id.ownerName);
            mHolder.lastMessage = convertView.findViewById(R.id.lastMessage);
            mHolder.chatCount = convertView.findViewById(R.id.chatCount);
            mHolder.messageTime = convertView.findViewById(R.id.messageTime);

            convertView.setTag(mHolder);

        }else{

            mHolder = (ViewHolder) convertView.getTag();
        }

        ChatList chat = (ChatList) getItem(position);

        if(chat != null){

            /*Glide.with(context)
                    .load(chat.getRosterBag().getMyBagAllimage().get(0).getBagUrl())
                    // .placeholder(R.raw.default_profile)
                    .into(mHolder.bagImage);*/

            mHolder.ownerName.setText(chat.getRosterBag().getUserName());
            mHolder.lastMessage.setText("Hello there, How you doing?");
            mHolder.chatCount.setText("");
            //mHolder.messageTime.setText("");
            mHolder.messageTime.setReferenceTime(new Date().getTime());

            mHolder.bagImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ChatBagDetailsActivity.class);
                    //context.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    class ViewHolder{

        TextView ownerName, lastMessage;
        MaterialBadgeTextView chatCount;
        RelativeTimeTextView messageTime;
        ImageView bagImage;
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "raw", context.getPackageName());

        return drawableResourceId;
    }
}
