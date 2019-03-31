package zuzusoft.com.bagbag.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zuzusoft.com.bagbag.BagBagApplication;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.AddBagActivity;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.helper.custom_views.CustomFontTextView;
import zuzusoft.com.bagbag.home.adapters.MyClosetAdapter;
import zuzusoft.com.bagbag.home.models.my_closet.Bag;
import zuzusoft.com.bagbag.home.models.my_closet.MyClosetResponse;
import zuzusoft.com.bagbag.localDB.ChatModel;
import zuzusoft.com.bagbag.rest.ApiClient;
import zuzusoft.com.bagbag.rest.ApiInterface;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class MyClosetFragment extends Fragment {

    public static boolean KEY_REFRESH_MY_CLOSET = false;
    private static String TAG = MyClosetFragment.class.getSimpleName();

    ListView bagList;
    @BindView(R.id.profileImage)
    CircleImageView profileImage;
    @BindView(R.id.name)
    CustomFontTextView name;
    @BindView(R.id.bagCount)
    CustomFontTextView bagCount;
    @BindView(R.id.hashTagName)
    CustomFontTextView hashTagName;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    private TextView btnAddBag;
    private DialogHelper dialogHelper;

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_closet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //spread butter
        ButterKnife.bind(this, view);

        dialogHelper = new DialogHelper(getActivity());
        dialogHelper.setDialogInfo("", "Please Wait...");

        updateUserProfile();

        bagList = (ListView) view.findViewById(R.id.bagList);
        checkViewEmpty(1);

        btnAddBag = (TextView) view.findViewById(R.id.btnAddBag);
        btnAddBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddBagActivity.class);
                startActivity(intent);

            }
        });

        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getMyClosetList();
                    }
                }
        );

        getMyClosetList();

        printHashKey(getContext());

        // Local Storage test
        insertNewMovieData();


    }

    private void insertNewMovieData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int totalItem = BagBagApplication.getInstance(getContext()).getDaoAccess().getNoOfChat();
                totalItem++;
                ChatModel movie = new ChatModel();
                movie.setChatId("" + totalItem);
                movie.setMessage("Message" + totalItem);
                BagBagApplication.getInstance(getContext()).getDaoAccess().
                        insertOnlySingleChat(movie);

                int totalItemNew = BagBagApplication.getInstance(getContext()).getDaoAccess().getNoOfChat();

                final List<ChatModel> movies = new ArrayList<>();

                if (totalItemNew > 0) {
                    movies.addAll(BagBagApplication.getInstance(getContext()).getDaoAccess().
                            getAllChatList());


                    Log.d(TAG, movies.toString());
                } else {
                    Log.d(TAG, "No CHat found");

                }


//                getChatList();

            }
        }).start();

    }

    private void getChatList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int totalItem = BagBagApplication.getInstance(getContext()).getDaoAccess().getNoOfChat();

                final List<ChatModel> movies = new ArrayList<>();

                if (totalItem > 0) {
                    movies.addAll(BagBagApplication.getInstance(getContext()).getDaoAccess().
                            getAllChatList());


                    Log.d(TAG, movies.toString());
                } else {
                    Log.d(TAG, "No CHat found");

                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (KEY_REFRESH_MY_CLOSET) {

            getMyClosetList();
        }

    }

    private void updateUserProfile() {

        SessionManager sessionManager = new SessionManager(getActivity());
        final HashMap<String, String> userData = sessionManager.getUserDetails();

        Log.v("Profile Image", userData.get(SessionManager.KEY_PROFILE_IMAGE));


        if (userData.get(SessionManager.KEY_SOCIAL_PROVIDER).equalsIgnoreCase("Facebook")) {

            Glide.with(getContext())
                    .load(userData.get(SessionManager.KEY_PROFILE_IMAGE))
                    // .placeholder(R.raw.default_profile)
                    .into(profileImage);

            /*new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        URL fb_url = new URL(userData.get(SessionManager.KEY_PROFILE_IMAGE));
                        HttpsURLConnection conn1 = (HttpsURLConnection) fb_url.openConnection();
                        HttpsURLConnection.setFollowRedirects(true);
                        conn1.setInstanceFollowRedirects(true);
                        final Bitmap fb_img = BitmapFactory.decodeStream(conn1.getInputStream());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                profileImage.setImageBitmap(fb_img);
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();*/

        } else {

          /*  Glide.with(getContext())
                    .load(userData.get(SessionManager.KEY_PROFILE_IMAGE))
                    // .placeholder(R.raw.default_profile)
                    .into(profileImage);*/
            Picasso.with(getContext())
                    .load(userData.get(SessionManager.KEY_PROFILE_IMAGE))
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(profileImage);

        }

        name.setText(userData.get(SessionManager.KEY_NAME).split(" ")[0]);
        hashTagName.setText("@" + userData.get(SessionManager.KEY_NAME).toLowerCase());


    }

    public void getMyClosetList() {

        if (ApiClient.isNetworkAvailable(getActivity())) {

            myClosetApi();

        } else {

            Toast.makeText(getActivity(), MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void myClosetApi() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //dialogHelper.showProgressDialog();
        swipeRefresh.setRefreshing(true);


        SessionManager sessionManager = new SessionManager(getContext());
        String publicKey = sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC);
        String privateKey = sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE);

        Call<MyClosetResponse> call = apiService.getMyCloset(publicKey, privateKey);

        call.enqueue(new Callback<MyClosetResponse>() {
            @Override
            public void onResponse(Call<MyClosetResponse> call, Response<MyClosetResponse> response) {


                if (getContext() != null) {

                    //dialogHelper.closeDialog();
                    swipeRefresh.setRefreshing(false);
                    KEY_REFRESH_MY_CLOSET = false;

                    int code = response.code();
                    if (code == 200) {

                        if (response.body().getStatus()) {

                            List<Bag> dataSet = response.body().getResponse().getBag();
                            Collections.reverse(dataSet);

                            if (!dataSet.isEmpty()) {

                                bagList.setAdapter(new MyClosetAdapter(getActivity(), dataSet));
                                bagCount.setText(" " + dataSet.size() + " " + getString(R.string.text_handbag_count));
                                checkViewEmpty(3);

                            } else {

                                checkViewEmpty(2);
                                bagCount.setText(" 0 " + getString(R.string.text_handbag_count));
                                Toast.makeText(getActivity(), "My Closet Empty!", Toast.LENGTH_SHORT).show();

                            }


                        } else {

                            checkViewEmpty(2);
                            bagCount.setText(" 0 " + getString(R.string.text_handbag_count));
                            //Toast.makeText(getActivity(), "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<MyClosetResponse> call, Throwable t) {

                //dialogHelper.closeDialog();
                swipeRefresh.setRefreshing(false);
                t.printStackTrace();

            }
        });
    }

    private void checkViewEmpty(int mode) {

        switch (mode) {

            case 1:
                //default, hide both listview and empty view
                bagList.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);

                break;


            case 2:
                //when my closet empty
                bagList.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

                break;


            case 3:
                //when my closet not empty
                bagList.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                break;
        }

    }

}
