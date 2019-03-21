package zuzusoft.com.bagbag.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.MatchingDialog;

/**
 * Created by mukeshnarayan on 27/11/18.
 */

public class BagExchangeDialog extends DialogFragment {

    private TextView btnDark, btnLight;
    private Button btnYes, btnNo;

    private OnBagListener mListener;
    public interface OnBagListener{

        void onBagExchange();
        void onBagExchangeCancel();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{

            mListener = (OnBagListener) activity;

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View rootView = inflater.inflate(R.layout.dialog_bag_exchange, null);



        initViews(rootView);
        builder.setView(rootView);

        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    private void initViews(View rootView) {

        btnYes = (Button) rootView.findViewById(R.id.btnYes);
        btnNo  = (Button) rootView.findViewById(R.id.btnNo);


        btnLightListener();
        btnDarkListener();

    }

    private void btnDarkListener() {

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){

                    mListener.onBagExchange();

                }
            }
        });
    }

    private void btnLightListener() {

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){

                    mListener.onBagExchangeCancel();

                }
            }
        });
    }
}