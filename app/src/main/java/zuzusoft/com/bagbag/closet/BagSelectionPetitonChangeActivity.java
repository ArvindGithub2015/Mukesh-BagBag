package zuzusoft.com.bagbag.closet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.home.models.DataBags;

/**
 * Created by mukeshnarayan on 19/11/18.
 */

public class BagSelectionPetitonChangeActivity extends BaseActivity implements GridBagAdapter.OnBagSelectListener {

    //just one bag selected

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.btnChange)
    TextView btnChange;

    @BindView(R.id.gridView)
    GridView gridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bag_selection_petition);

        initViews();

    }

    private void initViews() {

        //spread butter
        ButterKnife.bind(this);

        //gridView= (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new GridBagAdapter(context, getGoldenBags()));

        btnBack.setOnClickListener(this);
        btnChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.btnBack:

                finish();

                break;

            case R.id.btnChange:

                MatchingDialog md = new MatchingDialog();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                md.show(fragmentManager, "MD");

                break;

        }
    }


    private ArrayList<DataBags> getGoldenBags() {

        ArrayList<DataBags> dataSet = new ArrayList<>();

        DataBags b6 = new DataBags();
        b6.setBagFileName("gold_bag6");

        DataBags b7 = new DataBags();
        b7.setBagFileName("gold_bag7");

        DataBags b8 = new DataBags();
        b8.setBagFileName("gold_bag8");

        DataBags b9 = new DataBags();
        b9.setBagFileName("gold_bag9");

        DataBags b10 = new DataBags();
        b10.setBagFileName("gold_bag10");

        dataSet.add(b6);
        dataSet.add(b7);
        dataSet.add(b8);
        dataSet.add(b9);
        dataSet.add(b10);

        return dataSet;
    }

    @Override
    public void onSelect(int position, String bagId) {

    }

    @Override
    public void onUnSelect(int position, String bagId) {

    }

}
