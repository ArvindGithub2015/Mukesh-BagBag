package zuzusoft.com.bagbag.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.closet.model.AddBagResponse;
import zuzusoft.com.bagbag.get_start.model.Member;
import zuzusoft.com.bagbag.get_start.model.RegisterResponse;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.home.models.brand.BrandListResponse;
import zuzusoft.com.bagbag.home.models.gold.GoldListResponse;
import zuzusoft.com.bagbag.home.models.my_closet.MyClosetResponse;
import zuzusoft.com.bagbag.home.models.search.SearchBagResponse;


/**
 * Created by mukeshnarayan on 08/08/18.
 */

public class ApiDataInflation {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_MATERIAL = "material";
    public static final String KEY_COLOUR = "colour";
    public static final String KEY_BAG_TYPE = "bag_type";
    public static final String KEY_BAG_SIZE = "bag_size";
    public static final String KEY_BRAND_NAME = "brand_name";
    public static final String KEY_DESCP = "descp";
    public static final String KEY_BAG_IMAGE = "bag_image";
    public static final String KEY_BAG_ID = "bag_id";
    public static final String KEY_BAG_IMAGE_ID = "bag_image_id";

    public static final String KEY_OWNER_USER_ID = "owner_user_id";
    public static final String KEY_OWNER__BAGE_ID = "bag_id";
    public static final String KEY_OFFER_BAG_IDS = "offer_bag_ids";

    public static final String KEY_CHAT_ID = "chat_id";
    public static final String KEY_CHAT_STATUS = "chat_status";

    public static final String KEY_BRAND_ID = "brand_id";


    private Context context;
    private ApiDataInterface mListener;

    public ApiDataInflation(Context context){

        this.context = context;

        this.mListener = (ApiDataInterface)context;
    }

    private RequestBody createPartFromString(String s) {
        return RequestBody.create(MediaType.parse("text/plain"), s);
    }

    /**
     * Login or Register Api
     * @param userData
     * @param userProfileImage
     */
    public void getUserProfile(HashMap<String, String> userData, Bitmap userProfileImage){

        if(ApiClient.isNetworkAvailable(context)){

            profileAPiCall(userData, userProfileImage);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void profileAPiCall(HashMap<String, String> userData, Bitmap userProfileImage) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody reqSocialId = createPartFromString(userData.get(SessionManager.KEY_SOCIAL_ID));
        RequestBody reqName = createPartFromString(userData.get(SessionManager.KEY_NAME));
        RequestBody reqEmail = createPartFromString(userData.get(SessionManager.KEY_EMAIL));
        RequestBody reqProfileImage = createPartFromString(userData.get(SessionManager.KEY_PROFILE_IMAGE));
        RequestBody reqPhoneNo = createPartFromString(userData.get(SessionManager.KEY_PHONE_NUMBER));
        RequestBody reqSocialProvider = createPartFromString(userData.get(SessionManager.KEY_SOCIAL_PROVIDER));
        RequestBody reqToken = createPartFromString(userData.get(SessionManager.KEY_FCM_TOKEN));
        RequestBody reqGender = createPartFromString(userData.get(SessionManager.KEY_GENDER));
        RequestBody reqLatLan = createPartFromString(userData.get(SessionManager.KEY_LAT_LAN));

        final HashMap<String, RequestBody> map = new HashMap<>();
        // map.put("file", filename);
        map.put("email", reqEmail);
        map.put("name", reqName);
        map.put("contact_no", reqPhoneNo);
        map.put("social_id", reqSocialId);
        //map.put("photo", reqProfileImage);
        map.put("social_provider", reqSocialProvider);
        map.put("token", reqToken);
        map.put("gender", reqGender);
        map.put("latlan", reqLatLan);


        MultipartBody.Part multiUserImage = null;
        try {

            //create a file to write bitmap data
            File f = new File(context.getCacheDir(), "mkn.jpeg");
            //f.createNewFile();

            //add user image bitmap
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            userProfileImage.compress(Bitmap.CompressFormat.PNG, 0 , bos);
            byte[] bitmapdata = bos.toByteArray();


            multiUserImage = MultipartBody.Part.createFormData("photo", "mkn.jpg", RequestBody.create(MediaType.parse("multipart/form-data"), bitmapdata));

            //write the bytes in file
            /*String filename = "pippo.png";
            File sd = Environment.getExternalStorageDirectory();
            File dest = new File(sd, filename);


            try {
                FileOutputStream out = new FileOutputStream(dest);
                userProfileImage.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), dest);
            multiUserImage = MultipartBody.Part.createFormData("photo", dest.getName(), reqFile);*/

            ///multiPartBag = MultipartBody.Part.createFormData("bag_image", "mkn.jpg", RequestBody.create(MediaType.parse("multipart/form-data"), bitmapdata));


        } catch (Exception e) {
            e.printStackTrace();
        }


        Call<RegisterResponse> call = apiService.registerUser(map, multiUserImage);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        //Log.v("Name", response.body().getMessage());

                        Member member = response.body().getUser().getMember();

                        SessionManager sessionManager = new SessionManager(context);
                        HashMap<String, String> userData = new HashMap<>();
                        userData.put(SessionManager.KEY_USER_ID, member.getId());
                        userData.put(SessionManager.KEY_NAME, member.getName());
                        userData.put(SessionManager.KEY_EMAIL, member.getEmail());
                        userData.put(SessionManager.KEY_PROFILE_IMAGE, member.getPhoto());
                        userData.put(SessionManager.KEY_PHONE_NUMBER, member.getPhone());
                        userData.put(SessionManager.KEY_PUBLIC, response.body().getPublicKey());
                        userData.put(SessionManager.KEY_PRIVATE, response.body().getPrivateKey());
                        userData.put(SessionManager.KEY_SOCIAL_ID, member.getSocialId());
                        userData.put(SessionManager.KEY_SOCIAL_PROVIDER, member.getSocialProvider());
                        userData.put(SessionManager.KEY_FCM_TOKEN, member.getUserToken());
                        userData.put(SessionManager.KEY_GENDER, member.getGender());
                        userData.put(SessionManager.KEY_LAT_LAN, member.getLatlan());

                        sessionManager.createLoginSession(userData);

                        if(mListener != null){

                            mListener.onProfileData(response.body());
                        }

                    } else {

                        if(mListener != null){

                            mListener.onProfileData(null);
                        }

                        //DialogHelper.showMessageDialog(context, "", "Email or Password is incorrect!");
                        //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(mListener != null){

                        mListener.onProfileData(null);
                    }

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                if(mListener != null){

                    mListener.onProfileData(null);
                }

            }
        });

    }


    /**
     * Add Bag to my closet api
     * @param addBag
     */
    public void addBag(HashMap<String, String> addBag){

        if(ApiClient.isNetworkAvailable(context)){

            addBagApi(addBag);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void addBagApi(HashMap<String, String> addBag) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody reqUserId = createPartFromString(addBag.get(KEY_USER_ID));
        RequestBody reqMaterial = createPartFromString(addBag.get(KEY_MATERIAL));
        RequestBody reqColour = createPartFromString(addBag.get(KEY_COLOUR));
        RequestBody reqBagType = createPartFromString(addBag.get(KEY_BAG_TYPE));
        RequestBody reqBagSize = createPartFromString(addBag.get(KEY_BAG_SIZE));
        RequestBody reqBrandName = createPartFromString(addBag.get(KEY_BRAND_NAME));
        RequestBody reqDescp = createPartFromString(addBag.get(KEY_DESCP));

        final HashMap<String, RequestBody> map = new HashMap<>();
        // map.put("file", filename);
        map.put("user_id", reqUserId);
        map.put("material", reqMaterial);
        map.put("colour", reqColour);
        map.put("bag_type", reqBagType);
        map.put("bag_size", reqBagSize);
        map.put("brand_name", reqBrandName);
        map.put("bag_description", reqDescp);


        //RequestBody reqFile;
        MultipartBody.Part multiPartBag;
        MultipartBody.Part multiPartPdf;
        File file = null;
        if(addBag.get(KEY_BAG_IMAGE) != null){

            file = new File(addBag.get(KEY_BAG_IMAGE));
            multiPartBag = MultipartBody.Part.createFormData("bag_image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

        }else{

            //get drawable in byte
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.no_image_);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
            byte[] bitmapdata = bos.toByteArray();

            // Get the uri of the image from raw folder
            multiPartBag = MultipartBody.Part.createFormData("bag_image", "mkn.jpg", RequestBody.create(MediaType.parse("multipart/form-data"), bitmapdata));

        }

        Call<AddBagResponse> call = apiService.addBag(map, multiPartBag);

        call.enqueue(new Callback<AddBagResponse>() {
            @Override
            public void onResponse(Call<AddBagResponse> call, Response<AddBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());

                        if(mListener != null){

                            mListener.onAddBagData(response.body().getResponse());
                        }

                    } else {

                        if(mListener != null){

                            mListener.onProfileData(null);
                        }

                        //DialogHelper.showMessageDialog(context, "", "Email or Password is incorrect!");
                        //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(mListener != null){

                        mListener.onProfileData(null);
                    }

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddBagResponse> call, Throwable t) {

                if(mListener != null){

                    mListener.onProfileData(null);
                }

            }
        });


    }


    /**
     * Delete user bags api
     * @param bagId
     */
    public void deleteBag(String bagId){

        if(ApiClient.isNetworkAvailable(context)){

            deletebagApi(bagId);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void deletebagApi(String bagId) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        SessionManager sessionManager = new SessionManager(context);
        String publicKey = sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC);
        String privateKey = sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE);

        Call<MyClosetResponse> call = apiService.deleteBag(bagId, publicKey, privateKey);

        call.enqueue(new Callback<MyClosetResponse>() {
            @Override
            public void onResponse(Call<MyClosetResponse> call, Response<MyClosetResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        if(mListener != null){

                            mListener.onBagDelete(true);
                        }

                    } else {

                        if(mListener != null){

                            mListener.onBagDelete(false);
                        }

                        Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                    }

                }

            }
            @Override
            public void onFailure(Call<MyClosetResponse> call, Throwable t) {

                if(mListener != null){

                    mListener.onBagDelete(false);
                }

                t.printStackTrace();

            }
        });
    }


    /**
     * Add image to existing bag api
     * @param imageData
     */
    public void addBagImage(HashMap<String, String> imageData){

        if(ApiClient.isNetworkAvailable(context)){

            addBagImageApi(imageData);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void addBagImageApi(HashMap<String, String> imageData) {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody reqUserId = createPartFromString(imageData.get(KEY_USER_ID));
        RequestBody reqMaterial = createPartFromString(imageData.get(KEY_BAG_ID));

        final HashMap<String, RequestBody> map = new HashMap<>();
        // map.put("file", filename);
        map.put("user_id", reqUserId);
        map.put("bag_id", reqMaterial);


        //RequestBody reqFile;
        MultipartBody.Part multiPartBag;
        MultipartBody.Part multiPartPdf;
        File file = null;
        if(imageData.get(KEY_BAG_IMAGE) != null){

            file = new File(imageData.get(KEY_BAG_IMAGE));
            multiPartBag = MultipartBody.Part.createFormData("bag_image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

        }else{

            //get drawable in byte
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.no_image_);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
            byte[] bitmapdata = bos.toByteArray();

            // Get the uri of the image from raw folder
            multiPartBag = MultipartBody.Part.createFormData("bag_image", "mkn.jpg", RequestBody.create(MediaType.parse("multipart/form-data"), bitmapdata));

        }

        Call<AddBagResponse> call = apiService.addBagImage(map, multiPartBag);

        call.enqueue(new Callback<AddBagResponse>() {
            @Override
            public void onResponse(Call<AddBagResponse> call, Response<AddBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());

                        if(mListener != null){

                            mListener.onAddBagImageData(response.body().getResponse());
                        }

                    } else {

                        if(mListener != null){

                            mListener.onAddBagImageData(null);
                        }

                        //DialogHelper.showMessageDialog(context, "", "Email or Password is incorrect!");
                        //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(mListener != null){

                        mListener.onAddBagImageData(null);
                    }

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddBagResponse> call, Throwable t) {

                if(mListener != null){

                    mListener.onAddBagImageData(null);
                }

            }
        });

    }



    /**
     * Delete single image from image group
     * @param bagImageId
     */
    public void deleteBagImage(String bagImageId, String bagId){

        if(ApiClient.isNetworkAvailable(context)){

            deleteBagImageApi(bagImageId, bagId);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteBagImageApi(String bagImageId, String bagId) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        SessionManager sessionManager = new SessionManager(context);
        String publicKey = sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC);
        String privateKey = sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE);

        Call<AddBagResponse> call = apiService.deleteBagImage(publicKey, privateKey, bagImageId, bagId);

        call.enqueue(new Callback<AddBagResponse>() {
            @Override
            public void onResponse(Call<AddBagResponse> call, Response<AddBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        if(mListener != null){

                            mListener.onDeleteBagImage(response.body().getResponse());
                        }

                    } else {

                        if(mListener != null){

                            mListener.onDeleteBagImage(null);
                        }

                        Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                    }

                }

            }
            @Override
            public void onFailure(Call<AddBagResponse> call, Throwable t) {

                if(mListener != null){

                    mListener.onDeleteBagImage(null);
                }

                t.printStackTrace();

            }
        });
    }


    /**
     *  Send petition request
     * @param dataSet
     */
    public void sendBagChangeRequest(HashMap<String, String> dataSet){

        if(ApiClient.isNetworkAvailable(context)){

            bagChangeRequestApi(dataSet);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void bagChangeRequestApi(HashMap<String, String> dataSet) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        SessionManager sessionManager = new SessionManager(context);
        RequestBody publicKey = createPartFromString(sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC));
        RequestBody privateKey = createPartFromString(sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE));
        RequestBody userId = createPartFromString(sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID));
        RequestBody offerBagIds = createPartFromString(dataSet.get(KEY_OFFER_BAG_IDS));
        RequestBody ownerUserId = createPartFromString(dataSet.get(KEY_OWNER_USER_ID));
        RequestBody ownerBagId = createPartFromString(dataSet.get(KEY_OWNER__BAGE_ID));

        final HashMap<String, RequestBody> map = new HashMap<>();
        map.put("public_key", publicKey);
        map.put("private_key", privateKey);
        map.put("my_user_id", userId);
        map.put("my_bag_ids", offerBagIds);
        map.put("request_user_id", ownerUserId);
        map.put("request_bag", ownerBagId);

        Call<SearchBagResponse> call = apiService.onBagChangeRequest(map);

        call.enqueue(new Callback<SearchBagResponse>() {
            @Override
            public void onResponse(Call<SearchBagResponse> call, Response<SearchBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("User token mkn", response.message());

                        if(mListener != null){

                            mListener.onBagChangeRequestSend(true);
                        }

                    } else {

                        if(mListener != null){

                            mListener.onBagChangeRequestSend(false);
                        }

                        Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                    }

                }

            }
            @Override
            public void onFailure(Call<SearchBagResponse> call, Throwable t) {

                if(mListener != null){

                    mListener.onBagChangeRequestSend(false);
                }
                t.printStackTrace();

            }
        });
    }


    /**
     *  Update / Edit bag details
     * @param dataBag
     */
    public void updateBagDetails(HashMap<String, String> dataBag){

        if(ApiClient.isNetworkAvailable(context)){

            updateBagDetailsApi(dataBag);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void updateBagDetailsApi(HashMap<String, String> dataBag) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody reqUserId = createPartFromString(dataBag.get(KEY_USER_ID));
        RequestBody reqMaterial = createPartFromString(dataBag.get(KEY_MATERIAL));
        RequestBody reqColour = createPartFromString(dataBag.get(KEY_COLOUR));
        RequestBody reqBagType = createPartFromString(dataBag.get(KEY_BAG_TYPE));
        RequestBody reqBagSize = createPartFromString(dataBag.get(KEY_BAG_SIZE));
        RequestBody reqBrandName = createPartFromString(dataBag.get(KEY_BRAND_NAME));
        RequestBody reqDescp = createPartFromString(dataBag.get(KEY_DESCP));
        RequestBody reqBagId = createPartFromString(dataBag.get(KEY_BAG_ID));

        final HashMap<String, RequestBody> map = new HashMap<>();

        map.put("user_id", reqUserId);
        map.put("material", reqMaterial);
        map.put("colour", reqColour);
        map.put("bag_type", reqBagType);
        map.put("bag_size", reqBagSize);
        map.put("brand_name", reqBrandName);
        map.put("bag_description", reqDescp);
        map.put("bag_id", reqBagId);


        Call<AddBagResponse> call = apiService.onUpdateBagDetails(map);

        call.enqueue(new Callback<AddBagResponse>() {
            @Override
            public void onResponse(Call<AddBagResponse> call, Response<AddBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());

                        if(mListener != null){

                            mListener.onAddBagData(response.body().getResponse());
                        }

                    } else {

                        if(mListener != null){

                            mListener.onProfileData(null);
                        }

                        //DialogHelper.showMessageDialog(context, "", "Email or Password is incorrect!");
                        //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(mListener != null){

                        mListener.onProfileData(null);
                    }

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddBagResponse> call, Throwable t) {

                if(mListener != null){

                    mListener.onProfileData(null);
                }

            }
        });


    }


    /**
     * Accept Petition request
     * @param dataBag
     */
    public void acceptPetitionRequest(HashMap<String, String> dataBag, ApiDataInterface.PetitionListener pListener){

        if(ApiClient.isNetworkAvailable(context)){

            Log.v("Accept_p_request_params", dataBag.toString());
            acceptPetitionRequestApi(dataBag, pListener);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void acceptPetitionRequestApi(final HashMap<String, String> dataBag, final ApiDataInterface.PetitionListener pListener) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody publicKey = createPartFromString(dataBag.get(SessionManager.KEY_PUBLIC));
        RequestBody privateKey = createPartFromString(dataBag.get(SessionManager.KEY_PRIVATE));
        RequestBody userId = createPartFromString(dataBag.get(SessionManager.KEY_USER_ID));
        RequestBody requestBagId = createPartFromString(dataBag.get(KEY_BAG_ID));

        final HashMap<String, RequestBody> map = new HashMap<>();
        map.put("public_key", publicKey);
        map.put("private_key", privateKey);
        map.put("my_user_id", userId);
        map.put("request_bag", requestBagId);

        Log.v("Accept_p_request_params", map.toString());

        Call<SearchBagResponse> call = apiService.onAcceptPetitionRequest(map);

        call.enqueue(new Callback<SearchBagResponse>() {
            @Override
            public void onResponse(Call<SearchBagResponse> call, Response<SearchBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());
                        if(pListener != null){

                            pListener.onPetitionAccept(true, dataBag);

                        }

                    } else {

                        if(pListener != null){

                            pListener.onPetitionAccept(false, dataBag);

                        }

                    }

                } else {

                    if(pListener != null){

                        pListener.onPetitionAccept(false, dataBag);

                    }
                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SearchBagResponse> call, Throwable t) {

                if(mListener != null){

                    pListener.onPetitionAccept(false, dataBag);
                }

            }
        });


    }


    /**
     *  Update / Chat Status
     * @param chatData
     */
    public void updateChatStatus(HashMap<String, String> chatData){

        if(ApiClient.isNetworkAvailable(context)){

            updateChatStatusApi(chatData);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void updateChatStatusApi(HashMap<String, String> chatData) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody publicKey = createPartFromString(chatData.get(SessionManager.KEY_PUBLIC));
        RequestBody privateKey = createPartFromString(chatData.get(SessionManager.KEY_PRIVATE));
        RequestBody chatID = createPartFromString(chatData.get(KEY_CHAT_ID));
        RequestBody chatStatus = createPartFromString(chatData.get(KEY_CHAT_STATUS));

        final HashMap<String, RequestBody> map = new HashMap<>();

        map.put("public_key", publicKey);
        map.put("private_key", privateKey);
        map.put("chat_id", chatID);
        map.put("status", chatStatus);

        Call<AddBagResponse> call = apiService.onUpdateChatStatus(map);

        call.enqueue(new Callback<AddBagResponse>() {
            @Override
            public void onResponse(Call<AddBagResponse> call, Response<AddBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());

                        if(mListener != null){

                            //mListener.onAddBagData(response.body().getResponse());
                        }

                    } else {

                        if(mListener != null){

                            //mListener.onProfileData(null);
                        }

                        //DialogHelper.showMessageDialog(context, "", "Email or Password is incorrect!");
                        //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(mListener != null){

                        //mListener.onProfileData(null);
                    }

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddBagResponse> call, Throwable t) {

                if(mListener != null){

                    //mListener.onProfileData(null);
                }

            }
        });


    }


    /**
     *  Hide Petition
     * @param dataBag
     */
    public void hidePetition(HashMap<String, String> dataBag){

        if(ApiClient.isNetworkAvailable(context)){

            hidePetitionApi(dataBag);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void hidePetitionApi(HashMap<String, String> dataBag) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody publicKey = createPartFromString(dataBag.get(SessionManager.KEY_PUBLIC));
        RequestBody privateKey = createPartFromString(dataBag.get(SessionManager.KEY_PRIVATE));
        RequestBody myUserId = createPartFromString(dataBag.get(SessionManager.KEY_USER_ID));
        RequestBody requestBagId = createPartFromString(dataBag.get(KEY_BAG_ID));

        final HashMap<String, RequestBody> map = new HashMap<>();

        map.put("public_key", publicKey);
        map.put("private_key", privateKey);
        map.put("my_user_id", myUserId);
        map.put("request_bag", requestBagId);

        Call<AddBagResponse> call = apiService.onPetitionHide(map);

        call.enqueue(new Callback<AddBagResponse>() {
            @Override
            public void onResponse(Call<AddBagResponse> call, Response<AddBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());

                        if(mListener != null){

                            //mListener.onAddBagData(response.body().getResponse());
                        }

                    } else {

                        if(mListener != null){

                            //mListener.onProfileData(null);
                        }

                        //DialogHelper.showMessageDialog(context, "", "Email or Password is incorrect!");
                        //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(mListener != null){

                        //mListener.onProfileData(null);
                    }

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddBagResponse> call, Throwable t) {

                if(mListener != null){

                    //mListener.onProfileData(null);
                }

            }
        });


    }


    /**
     *  Switch bag request
     * @param dataBag
     */
    public void switchBagRequest(HashMap<String, String> dataBag){

        if(ApiClient.isNetworkAvailable(context)){

            switchBagRequestApi(dataBag);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void switchBagRequestApi(HashMap<String, String> dataBag) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody publicKey = createPartFromString(dataBag.get(SessionManager.KEY_PUBLIC));
        RequestBody privateKey = createPartFromString(dataBag.get(SessionManager.KEY_PRIVATE));
        RequestBody chatID = createPartFromString(dataBag.get(KEY_CHAT_ID));

        final HashMap<String, RequestBody> map = new HashMap<>();

        map.put("public_key", publicKey);
        map.put("private_key", privateKey);
        map.put("chat_id", chatID);

        Call<AddBagResponse> call = apiService.onSwitchBagRequest(map);

        call.enqueue(new Callback<AddBagResponse>() {
            @Override
            public void onResponse(Call<AddBagResponse> call, Response<AddBagResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());

                        if(mListener != null){

                            //mListener.onAddBagData(response.body().getResponse());
                        }

                    } else {

                        if(mListener != null){

                            //mListener.onProfileData(null);
                        }

                        //DialogHelper.showMessageDialog(context, "", "Email or Password is incorrect!");
                        //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    if(mListener != null){

                        //mListener.onProfileData(null);
                    }

                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddBagResponse> call, Throwable t) {

                if(mListener != null){

                    //mListener.onProfileData(null);
                }

            }
        });


    }


    /**
     * Fetch Gold List
     * @param dataBag
     */
    public void fetchGoldList(HashMap<String, String> dataBag, ApiDataInterface.FetchGoldListener pListener){

        if(ApiClient.isNetworkAvailable(context)){

            fetchGoldListApi(dataBag, pListener);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchGoldListApi(final HashMap<String, String> dataBag, final ApiDataInterface.FetchGoldListener pListener) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody publicKey = createPartFromString(dataBag.get(SessionManager.KEY_PUBLIC));
        RequestBody privateKey = createPartFromString(dataBag.get(SessionManager.KEY_PRIVATE));
        RequestBody goldBrandId = createPartFromString(dataBag.get(KEY_BRAND_ID));

        final HashMap<String, RequestBody> map = new HashMap<>();
        map.put("public_key", publicKey);
        map.put("private_key", privateKey);
        map.put("brand_id", goldBrandId);

        Call<GoldListResponse> call = apiService.onGoldListRequest(map);

        call.enqueue(new Callback<GoldListResponse>() {
            @Override
            public void onResponse(Call<GoldListResponse> call, Response<GoldListResponse> response) {

                int code = response.code();
                if (code == 200) {

                    Log.v("Name", response.body().getMessage());
                    if (response.body().getStatus()) {


                        if(pListener != null){

                            pListener.onFetchGold(response.body().getGoldBags());

                        }

                    } else {

                        if(pListener != null){

                            pListener.onFetchGold(null);

                        }

                    }

                } else {

                    if(pListener != null){

                        pListener.onFetchGold(null);

                    }
                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GoldListResponse> call, Throwable t) {

                if(mListener != null){

                    pListener.onFetchGold(null);
                }

            }
        });


    }


    /**
     * Fetch Gold List
     * @param dataBag
     */
    public void fetchBrandList(HashMap<String, String> dataBag, ApiDataInterface.FetchGoldListener pListener){

        if(ApiClient.isNetworkAvailable(context)){

            fetchBrandListApi(dataBag, pListener);

        }else{

            Toast.makeText(context, MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchBrandListApi(final HashMap<String, String> dataBag, final ApiDataInterface.FetchGoldListener pListener) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        // create a map of data to pass along
        RequestBody publicKey = createPartFromString(dataBag.get(SessionManager.KEY_PUBLIC));
        RequestBody privateKey = createPartFromString(dataBag.get(SessionManager.KEY_PRIVATE));

        final HashMap<String, RequestBody> map = new HashMap<>();
        map.put("public_key", publicKey);
        map.put("private_key", privateKey);

        Call<BrandListResponse> call = apiService.onBrandListRequest(map);

        call.enqueue(new Callback<BrandListResponse>() {
            @Override
            public void onResponse(Call<BrandListResponse> call, Response<BrandListResponse> response) {

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        Log.v("Name", response.body().getMessage());
                        if(pListener != null){

                            pListener.onFetchBrand(response.body().getBrands());

                        }

                    } else {

                        if(pListener != null){

                            pListener.onFetchBrand(null);

                        }

                    }

                } else {

                    if(pListener != null){

                        pListener.onFetchBrand(null);

                    }
                    Toast.makeText(context, "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BrandListResponse> call, Throwable t) {

                if(mListener != null){

                    pListener.onFetchBrand(null);
                }

            }
        });


    }


}
