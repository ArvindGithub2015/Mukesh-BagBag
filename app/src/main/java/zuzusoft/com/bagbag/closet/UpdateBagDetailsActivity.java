package zuzusoft.com.bagbag.closet;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.model.Response;
import zuzusoft.com.bagbag.custom_views.crop_view.CropImage;
import zuzusoft.com.bagbag.custom_views.crop_view.CropImageView;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.MyGlideEngine;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.rest.ApiDataInflation;

/**
 * Created by mukeshnarayan on 16/01/19.
 */

public class UpdateBagDetailsActivity extends BaseActivity {

    private ImagePicker imagePicker;
    private List<Uri> mSelected = new ArrayList<>();
    private Uri resultUri;
    private boolean isCamera = false;
    private int REQUEST_CODE_CHOOSE = 999;
    private boolean permission = false;

    private SessionManager sessionManager;

    private Validator validator;

    @BindView(R.id.btnBagDetails)
    TextView btnBagDetails;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.spinnerMaterial)
    Spinner spinnerMaterial;

    @BindView(R.id.spinnerColor)
    Spinner spinnerColor;

    @BindView(R.id.spinnerBagType)
    Spinner spinnerBagType;

    @BindView(R.id.spinnerSize)
    Spinner spinnerSize;

    @NotEmpty
    @BindView(R.id.bagDescription)
    EditText bagDescription;

    @NotEmpty
    @BindView(R.id.brandName)
    EditText brandName;

    @BindView(R.id.blurOverlay)
    FrameLayout blurOverlay;

    private DialogHelper dialogHelper;

    private String previousBrandName;
    private String previousDescription;
    private String previousBagMaterial;
    private String previousBagColour;
    private String previousBagSize;
    private String previousBagType;
    private String bagId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_bag_details);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) { permission = true;/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) { permission = false;/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        initViews();
    }

    private void initViews() {

        //spread butter
        ButterKnife.bind(this);

        dialogHelper = new DialogHelper(context);
        dialogHelper.setDialogInfo("", "Please Wait...");

        //init progress view
        initProgressView(this, R.id.progressView);
        sessionManager = new SessionManager(context);

        //get intent data
        previousBrandName = getIntent().getStringExtra(BagProfileEditionActivity.MAP_BRAND_NAME);
        previousDescription = getIntent().getStringExtra(BagProfileEditionActivity.MAP_BAG_DESCRIPTION);
        previousBagMaterial = getIntent().getStringExtra(BagProfileEditionActivity.MAP_BAG_MATERIAL);
        previousBagColour = getIntent().getStringExtra(BagProfileEditionActivity.MAP_BAG_COLOUR);
        previousBagSize = getIntent().getStringExtra(BagProfileEditionActivity.MAP_BAG_SIZE);
        previousBagType = getIntent().getStringExtra(BagProfileEditionActivity.MAP_BAG_TYPE);
        bagId  = getIntent().getStringExtra(ApiDataInflation.KEY_BAG_ID);

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnBagDetails.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //update bag Details
        setPreviousBagDetails();

    }

    private void setPreviousBagDetails() {

        //set previous spinners item
        spinnerColor.setSelection(getColourItemIndex());
        spinnerBagType.setSelection(getBagTypeItemIndex());
        spinnerMaterial.setSelection(getMaterialItemIndex());
        spinnerSize.setSelection(getBagSizeItemIndex());

        //set previous brand name and description
        brandName.setText(previousBrandName);
        bagDescription.setText(previousDescription);

    }

    private int getBagSizeItemIndex() {

        String strArr[] = getResources().getStringArray(R.array.tamano);
        int index = Arrays.asList(strArr).indexOf(previousBagSize);
        return index;
    }

    private int getMaterialItemIndex() {

        String strArr[] = getResources().getStringArray(R.array.texture);
        int index = Arrays.asList(strArr).indexOf(previousBagMaterial);
        return index;
    }


    private int getBagTypeItemIndex() {

        String strArr[] = getResources().getStringArray(R.array.tipo);
        int index = Arrays.asList(strArr).indexOf(previousBagType);
        return index;
    }

    private int getColourItemIndex() {

        String strArr[] = getResources().getStringArray(R.array.color);
        int index = Arrays.asList(strArr).indexOf(previousBagColour);
        return index;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.btnBack:

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();

                break;


            case R.id.btnBagDetails:

                validator.validate();

                break;
        }
    }



    @Override
    public void onValidationSucceeded() {

        HashMap<String, String> dataSet = new HashMap<>();
        dataSet.put(ApiDataInflation.KEY_USER_ID, sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID));
        dataSet.put(ApiDataInflation.KEY_MATERIAL, (String)spinnerMaterial.getSelectedItem());
        dataSet.put(ApiDataInflation.KEY_COLOUR, (String)spinnerColor.getSelectedItem());
        dataSet.put(ApiDataInflation.KEY_BAG_TYPE, (String)spinnerBagType.getSelectedItem());
        dataSet.put(ApiDataInflation.KEY_BAG_SIZE, (String)spinnerSize.getSelectedItem());
        dataSet.put(ApiDataInflation.KEY_BRAND_NAME, brandName.getText().toString());
        dataSet.put(ApiDataInflation.KEY_DESCP, bagDescription.getText().toString());
        dataSet.put(ApiDataInflation.KEY_BAG_ID, bagId);

        dialogHelper.showProgressDialog();
        ApiDataInflation dataInflation = new ApiDataInflation(context);
        dataInflation.updateBagDetails(dataSet);
    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

        super.onBackPressed();
    }

    @Override
    public void onAddBagData(Response response) {
        super.onAddBagData(response);

        //progressView.setRefreshing(false);
        dialogHelper.closeDialog();

        Intent returnIntent = new Intent();

        returnIntent.putExtra(ApiDataInflation.KEY_MATERIAL, (String)spinnerMaterial.getSelectedItem());
        returnIntent.putExtra(ApiDataInflation.KEY_COLOUR, (String)spinnerColor.getSelectedItem());
        returnIntent.putExtra(ApiDataInflation.KEY_BAG_TYPE, (String)spinnerBagType.getSelectedItem());
        returnIntent.putExtra(ApiDataInflation.KEY_BAG_SIZE, (String)spinnerSize.getSelectedItem());
        returnIntent.putExtra(ApiDataInflation.KEY_BRAND_NAME, brandName.getText().toString());
        returnIntent.putExtra(ApiDataInflation.KEY_DESCP, bagDescription.getText().toString());
        returnIntent.putExtra(BagProfileEditionActivity.KEY_FROM_MY_CLOSET, false);

        setResult(BagProfileEditionActivity.INTENT_REQUEST_CODE_UPDATE_BAG,returnIntent);
        finish();

    }
}
