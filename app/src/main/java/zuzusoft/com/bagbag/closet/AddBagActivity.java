package zuzusoft.com.bagbag.closet;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;
import okhttp3.RequestBody;
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
 * Created by mukeshnarayan on 18/11/18.
 */

public class AddBagActivity extends BaseActivity {


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
    @BindView(R.id.bagDescription)
    EditText bagDescription;
    @NotEmpty
    @BindView(R.id.brandName)
    EditText brandName;
    @BindView(R.id.blurOverlay)
    FrameLayout blurOverlay;
    private Button btnAddBag;
    private ImagePicker imagePicker;
    private List<Uri> mSelected = new ArrayList<>();
    private Uri resultUri;
    private boolean isCamera = false;
    private int REQUEST_CODE_CHOOSE = 999;
    private boolean permission = false;
    private SessionManager sessionManager;
    private Validator validator;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addbag);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        permission = true;/* ... */
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        permission = false;/* ... */
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
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
        progressView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressView.setRefreshing(false);
            }
        });

        sessionManager = new SessionManager(context);

        validator = new Validator(this);
        validator.setValidationListener(this);

        imagePicker = new ImagePicker();
        imagePicker.setTitle("Camera");
        imagePicker.setCropImage(false);

        btnAddBag = (Button) findViewById(R.id.btnAddBag);
        btnAddBagListener();

        btnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.btnBack:

                finish();

                hideKeyboard(AddBagActivity.this);

                break;
        }
    }

    private void btnAddBagListener() {

        btnAddBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validator.validate();
            }
        });
    }


    private void pickAction() {

        final CharSequence colors[] = new CharSequence[]{"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Pick a color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                if (which == 0) {

                    //camera

                    isCamera = true;

                    imagePicker.startCamera(AddBagActivity.this, new ImagePicker.Callback() {

                        @Override
                        public void onPickImage(Uri imageUri) {

                            Log.v("Camera image uri: ", imageUri.getPath());

                            CropImage.activity(imageUri)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(AddBagActivity.this);

                        }

                    });

                } else {

                    //gallery
                    Matisse.from(AddBagActivity.this)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                            .countable(true)
                            .maxSelectable(1)
                            //.gridExpectedSize(getResources().getDimensionPixelSize(16))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new MyGlideEngine())
                            .forResult(REQUEST_CODE_CHOOSE);

                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (isCamera) {

            isCamera = false;

            imagePicker.onActivityResult(this, requestCode, resultCode, data);
        } else {


            if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

                mSelected = Matisse.obtainResult(data);

                Log.v("Matisse", "mSelected: " + mSelected);


                //go to crop activity

                CropImage.activity(mSelected.get(0))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }


            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    Uri resultUri = result.getUri();

                    this.resultUri = resultUri;

                    HashMap<String, String> dataSet = new HashMap<>();
                    dataSet.put(ApiDataInflation.KEY_USER_ID, sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID));
                    dataSet.put(ApiDataInflation.KEY_MATERIAL, (String) spinnerMaterial.getSelectedItem());
                    dataSet.put(ApiDataInflation.KEY_COLOUR, (String) spinnerColor.getSelectedItem());
                    dataSet.put(ApiDataInflation.KEY_BAG_TYPE, (String) spinnerBagType.getSelectedItem());
                    dataSet.put(ApiDataInflation.KEY_BAG_SIZE, (String) spinnerSize.getSelectedItem());
                    dataSet.put(ApiDataInflation.KEY_BRAND_NAME, brandName.getText().toString());
                    dataSet.put(ApiDataInflation.KEY_DESCP, bagDescription.getText().toString());
                    dataSet.put(ApiDataInflation.KEY_BAG_IMAGE, resultUri.getPath());

                    //progressView.setRefreshing(true);
                    //Blurry.with(context).radius(25).sampling(2).onto(blurOverlay);

                    dialogHelper.showProgressDialog();
                    ApiDataInflation dataInflation = new ApiDataInflation(context);
                    dataInflation.addBag(dataSet);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();
                    error.printStackTrace();

                }
            }

        }


    }


    @Override
    public void onValidationSucceeded() {
        pickAction();
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
    public void onAddBagData(Response response) {
        super.onAddBagData(response);

        //progressView.setRefreshing(false);
        dialogHelper.closeDialog();

        Intent intent = new Intent(context, BagProfileEditionActivity.class);
        intent.putExtra(BagProfileEditionActivity.KEY_SEIRAL_DATA, response);
        intent.putExtra(BagProfileEditionActivity.KEY_FROM_MY_CLOSET, false);
        startActivity(intent);

        finish();

    }

    @Override
    protected void onPause() {

        hideKeyboard(this);

        super.onPause();
    }

}
