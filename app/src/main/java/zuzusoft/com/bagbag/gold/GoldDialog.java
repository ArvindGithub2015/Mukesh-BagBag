package zuzusoft.com.bagbag.gold;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zuzusoft.com.bagbag.R;

/**
 * Created by mukeshnarayan on 25/11/18.
 */

public class GoldDialog extends DialogFragment {

    private TextView btnGold;
    private ImageView btnClose;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View rootView = inflater.inflate(R.layout.dialog_gold, null);



        initViews(rootView);
        builder.setView(rootView);

        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    private void initViews(View rootView) {

        btnGold = (TextView) rootView.findViewById(R.id.btnGold);
        btnClose = (ImageView) rootView.findViewById(R.id.btnClose);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }
}
