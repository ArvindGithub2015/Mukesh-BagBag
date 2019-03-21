package zuzusoft.com.bagbag.closet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itextpdf.text.pdf.parser.Line;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.helper.custom_views.CustomFontTextView;
import zuzusoft.com.bagbag.helper.notification.FCMPayload;

public class PetitionReceivedDialog extends DialogFragment {

    private TextView btnCheckNow, btnClose;
    private CustomFontTextView userName, messageText;
    private ImageView myBagImage, inExchangeBagImage;
    private LinearLayout bagContainer;
    private ImageView chatIcon;

    private PetitionReceivedListener mListener;
    public interface PetitionReceivedListener{

        void onPetitionCheckNow(boolean checkNow);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mListener = (PetitionReceivedListener) context;

        }catch (Exception e){

            e.getStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_petition_received, null);
        initView(rootView);

        builder.setView(rootView);


        return builder.create();
    }

    private void initView(View rootView) {

        FCMPayload payload = (FCMPayload) getArguments().getSerializable("payload");

        btnCheckNow  = (TextView) rootView.findViewById(R.id.btnCheckNow);
        btnClose  = (TextView) rootView.findViewById(R.id.btnClose);
        userName = (CustomFontTextView) rootView.findViewById(R.id.userName);
        messageText = (CustomFontTextView) rootView.findViewById(R.id.messageText);
        myBagImage = (ImageView) rootView.findViewById(R.id.myBagImage);
        inExchangeBagImage = (ImageView) rootView.findViewById(R.id.inExchangeBagImage);
        bagContainer = (LinearLayout) rootView.findViewById(R.id.bagContainer);
        chatIcon = (ImageView) rootView.findViewById(R.id.chatIcon);

        if(payload != null){

            if(payload.getScore().equalsIgnoreCase("1")){
                //petition received
                userName.setText("Hi "+payload.getInUserName()+",");
                messageText.setText("You have a new petition request from "+payload.getMyName());

                Glide.with(getContext())
                        .load(MknUtils.URL_BAG_IMAGE+payload.getMyBagImage())
                        // .placeholder(R.raw.default_profile)
                        .into(myBagImage);

                Glide.with(getContext())
                        .load(MknUtils.URL_BAG_IMAGE+payload.getInBagImage())
                        // .placeholder(R.raw.default_profile)
                        .into(inExchangeBagImage);
                bagContainer.setVisibility(View.VISIBLE);
                chatIcon.setVisibility(View.GONE);


            }else{

                SessionManager sessionManager = new SessionManager(getContext());
                userName.setText("Hi "+sessionManager.getUserDetails().get(SessionManager.KEY_NAME)+",");
                messageText.setText("Your Petition has been accepted. Check your chats. ");

                bagContainer.setVisibility(View.GONE);
                chatIcon.setVisibility(View.VISIBLE);

            }

        }

        btnCheckNowListener();
        btnCloseListener();

    }

    private void btnCloseListener() {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){

                    mListener.onPetitionCheckNow(true);
                }

                dismiss();
            }
        });
    }

    private void btnCheckNowListener() {

        btnCheckNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){

                    mListener.onPetitionCheckNow(false);
                    dismiss();
                }

            }
        });

    }
}
