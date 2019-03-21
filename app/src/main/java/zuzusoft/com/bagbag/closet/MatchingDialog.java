package zuzusoft.com.bagbag.closet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.custom_views.my_listview.MyListView;

/**
 * Created by mukeshnarayan on 19/11/18.
 */

public class MatchingDialog extends DialogFragment {

    public static final String KEY_BUNDLE_MATCHING_DIALOG = "matching_dialog";
    public static final String KEY_BAG_IMAGE1 = "image1";
    public static final String KEY_BAG_IMAGE2 = "image2";
    public static final String KEY_EXCHANGE_BAG_ID = "exchange_bag_id";

    @BindView(R.id.btnLight)
    TextView btnLight;
    @BindView(R.id.btnDark)
    TextView btnDark;
    @BindView(R.id.bagImage1)
    ImageView bagImage1;
    @BindView(R.id.bagImage2)
    ImageView bagImage2;
    @BindView(R.id.btnClose)
    ImageView btnClose;

    private OnMatchListener mListener;
    public interface OnMatchListener{

        void onDarkClicked();
        void onLightClicked();
    }


    public void setOnMatchListener(OnMatchListener mListener){

        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View rootView = inflater.inflate(R.layout.dialog_matching, null);
        initViews(rootView);

        builder.setView(rootView);

        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    private void initViews(View rootView) {

        //init
        btnDark = (TextView) rootView.findViewById(R.id.btnDark);
        btnLight = (TextView) rootView.findViewById(R.id.btnLight);
        bagImage1 = (ImageView) rootView.findViewById(R.id.bagImage1);
        bagImage2 = (ImageView) rootView.findViewById(R.id.bagImage2);
        btnClose = (ImageView) rootView.findViewById(R.id.btnClose);

        btnLightListener();
        btnDarkListener();
        btnCloseListener();

        //update views
        updateViews();
    }

    private void updateViews() {

        HashMap<String, String> dataBag = (HashMap<String, String>) getArguments().getSerializable(KEY_BUNDLE_MATCHING_DIALOG);

        Glide.with(getActivity())
                .load(dataBag.get(KEY_BAG_IMAGE1))
                .into(bagImage1);

        Glide.with(getActivity())
                .load(dataBag.get(KEY_BAG_IMAGE2))
                .into(bagImage2);


    }

    private void btnCloseListener() {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }

    private void btnDarkListener() {

        btnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){

                    mListener.onDarkClicked();
                    dismiss();

                }
            }
        });
    }

    private void btnLightListener() {

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){

                    mListener.onLightClicked();
                    dismiss();

                }
            }
        });
    }
}
