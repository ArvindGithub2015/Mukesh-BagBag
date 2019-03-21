package zuzusoft.com.bagbag.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.rest.ApiClient;
import zuzusoft.com.bagbag.rest.ApiInterface;

public class HelpandAssistanceActivity extends BaseActivity {

    @NotEmpty
    @BindView(R.id.input_name)
    EditText input_name;

    @NotEmpty
    @Email
    @BindView(R.id.input_email)
    EditText input_email;

    @NotEmpty
    @BindView(R.id.input_mobile)
    EditText input_mobile;

    @NotEmpty
    @BindView(R.id.messageBody)
    EditText messageBody;

    @BindView(R.id.send)
    ImageView send;

    private DialogHelper dialogHelper;
    private Validator validator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.active_help_assistance);

        initViews();
    }

    private void initViews() {

        //spread butter
        ButterKnife.bind(this);
        setupTolbar();

        dialogHelper = new DialogHelper(context);
        dialogHelper.setDialogInfo("", "Please Wait...");

        validator = new Validator(this);
        validator.setValidationListener(this);
        send.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.send:

                validator.validate();

                break;

        }
    }


    @Override
    public void onValidationSucceeded() {

        if(ApiClient.isNetworkAvailable(context)){

            makeAPiCall();

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void makeAPiCall() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //show progress dialog
        dialogHelper.showProgressDialog();

        String mobile = input_mobile.getText().toString().trim();
        String message = messageBody.getText().toString();
        String email = input_email.getText().toString().trim();
        String name = input_name.getText().toString();


        //Call<RegisterResponse> call = apiService.onContactUs(name, email, mobile, message);

        /*call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                dialogHelper.closeDialog();

                int code = response.code();

                if (code == 200) {

                    if (response.body().getStatus()) {

                        //reset views
                        DialogHelper.showMessageDialog(context, "", response.body().getMessage());

                        input_email.setText("");
                        input_name.setText("");
                        input_mobile.setText("");
                        messageBody.setText("");


                    } else {

                        DialogHelper.showMessageDialog(context, "", response.body().getMessage());
                    }

                } else {

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                dialogHelper.closeDialog();
                t.printStackTrace();

            }
        });*/

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

    private void setupTolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Help and Assistance");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();

    }

}
