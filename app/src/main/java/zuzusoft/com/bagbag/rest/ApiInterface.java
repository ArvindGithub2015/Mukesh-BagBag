package zuzusoft.com.bagbag.rest;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import zuzusoft.com.bagbag.closet.model.AddBagResponse;
import zuzusoft.com.bagbag.get_start.model.RegisterResponse;
import zuzusoft.com.bagbag.home.models.brand.BrandListResponse;
import zuzusoft.com.bagbag.home.models.chat.ChatListResponse;
import zuzusoft.com.bagbag.home.models.gold.GoldListResponse;
import zuzusoft.com.bagbag.home.models.my_closet.MyClosetResponse;
import zuzusoft.com.bagbag.home.models.search.SearchBagResponse;

/**
 * Created by mukeshnarayan on 08/04/18.
 */

public interface ApiInterface {

    @Multipart
    @POST("rest/Apis/sociallogin.json")
    Call<RegisterResponse> registerUser(@PartMap() Map<String, RequestBody> partMap,
                                        @Part MultipartBody.Part file);

    @Multipart
    @POST("rest/Apis/addBag.json")
    Call<AddBagResponse> addBag(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("rest/Apis/updateBag.json")
    Call<AddBagResponse> onUpdateBagDetails(
            @PartMap() Map<String, RequestBody> partMap);

    @GET("rest/Apis/myCloset.json")
    Call<MyClosetResponse> getMyCloset(@Query("public_key") String publicKey,
                                       @Query("private_key") String privateKey);

    @GET("rest/Apis/deleteBag.json")
    Call<MyClosetResponse> deleteBag(@Query("bag_id") String bagId,
                                     @Query("public_key") String publicKey,
                                     @Query("private_key") String privateKey);

    @Multipart
    @POST("rest/Apis/addImg.json")
    Call<AddBagResponse> addBagImage(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);

    @GET("rest/Apis/deleteImage.json")
    Call<AddBagResponse> deleteBagImage(@Query("public_key") String publicKey,
                                        @Query("private_key") String privateKey,
                                        @Query("bag_img_id") String bagImageId,
                                        @Query("bag_id") String bagId);
    @Multipart
    @POST("rest/Apis/searchBagList.json")
    Call<SearchBagResponse> getSeachBags(@PartMap() Map<String, RequestBody> partMap);


    @GET("rest/Apis/myPetitions.json")
    Call<SearchBagResponse> getAllPetitions(@Query("public_key") String publicKey,
                                            @Query("private_key") String privateKey);

    @Multipart
    @POST("rest/Apis/changeBag.json")
    Call<SearchBagResponse> onBagChangeRequest(@PartMap() Map<String, RequestBody> partMap);


    @GET("rest/Apis/myChatList.json")
    Call<ChatListResponse> getMyChats(@Query("public_key") String publicKey,
                                      @Query("private_key") String privateKey);

    @Multipart
    @POST("rest/Apis/acceptPetition.json")
    Call<SearchBagResponse> onAcceptPetitionRequest(@PartMap() Map<String, RequestBody> partMap);

    @Multipart
    @POST("rest/Apis/updateChatStatus.json")
    Call<AddBagResponse> onUpdateChatStatus(
            @PartMap() Map<String, RequestBody> partMap);

    @Multipart
    @POST("rest/Apis/hidePetition.json")
    Call<AddBagResponse> onPetitionHide(
            @PartMap() Map<String, RequestBody> partMap);

    @Multipart
    @POST("rest/Apis/switchRequest.json")
    Call<AddBagResponse> onSwitchBagRequest(
            @PartMap() Map<String, RequestBody> partMap);


    @Multipart
    @POST("rest/Apis/goldListing.json")
    Call<GoldListResponse> onGoldListRequest(@PartMap() Map<String, RequestBody> partMap);

    @Multipart
    @POST("rest/Apis/getBrands.json")
    Call<BrandListResponse> onBrandListRequest(@PartMap() Map<String, RequestBody> partMap);




   /* @Multipart
    @POST("uploadHeadPic")
    Call<LoginResponse> uploadHeadPic(@Part MultipartBody.Part file, @Part("json") RequestBody json);

    @POST("rest/Users/save.json")
    Call<RegisterResponse> registerUser(@Body RegisterPostModel user);

    @Multipart
    @POST("Users/saveform")
    Call<RegisterResponse> uploadDataWithAttachmentStr(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);


    @Multipart
    @POST("Users/saveform")
    Call<String> uploadDataWithAttachmentStr1(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);


    @Multipart
    @POST("Users/edit")
    Call<RegisterResponse> updateProfile(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2);


    @POST("rest/Users/checkotp.json")
    @FormUrlEncoded
    Call<RegisterResponse> checkOtp(@Field("reg_code") String regCode,
                                    @Field("user_id") String userId);


    @POST("rest/Users/password.json")
    @FormUrlEncoded
    Call<RegisterResponse> forgotPassword(@Field("mobile") String regCode);

    @POST("rest/Users/login.json")
    @FormUrlEncoded
    Call<LoginResponse> getLoginResponse(@Field("email") String email, @Field("password") String password);


    @POST("Users/contactus")
    @FormUrlEncoded
    Call<RegisterResponse> onContactUs(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("mobile") String mobile,
                                       @Field("message") String message);*/








   /* @POST("rest/Users/login.json")
    @FormUrlEncoded
    Call<LoginResponse> getLoginResponse(@Field("email") String email,
                                         @Field("password") String password);


    @POST("rest/Users/save.json")
   *//* @FormUrlEncoded*//* //@Body RegisterPostModel user
    Call<RegisterResponse> registerUser(@Body RegisterPostModel user);

    @POST("rest/Users/userprofile.json")
    @FormUrlEncoded
    Call<LoginResponse> getProfileResponse(@Field("user_id") String userId);


    @POST("rest/Users/password.json")
    @FormUrlEncoded
    Call<LoginResponse> passwordResend(@Field("email") String email);


    //http://zuzusoft.co.in/zuzusoft.co.in/hope/rest/Users/quotelisting.json
    @GET("rest/Users/quotelisting.json")
    Call<QuotesResponse> getQuotesResponse();

    @GET("rest/Users/bloglisting.json")
    Call<BlogResponse> getBlogResponse();

    @GET("rest/Users/history.json")
    Call<MyBookingResponse> getMyBookingResponse(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @GET("rest/Questions/listquestion.json")
    Call<FaqResponse> getFaqResponse();

    @GET
    Call<ProductResponse> getProducts(@Url String url);


    @POST("rest/Users/sendcontactus.json")
    @FormUrlEncoded
    Call<LoginResponse> contactUsResponse(@Field("name") String name,
                                          @Field("email") String email,
                                          @Field("mobile") String mobile,
                                          @Field("complain_type") String complain_type,
                                          @Field("message") String message);

    @GET("rest/Users/quoteofday.json")
    Call<QuotesResponse> getQuotesofthedayResponse();


    @Multipart
    @POST("rest/Users/changePhoto.json")
    Call<LoginResponse> postImage(@Part MultipartBody.Part image,
                                  @Part("private_key") RequestBody public_key,
                                  @Part("private_key") RequestBody private_key);


    //rest/Users/askquestion.json
    @POST("rest/Users/askquestion.json")
    @FormUrlEncoded
    Call<LoginResponse> askQuestion(@Field("question") String question, @Field("public_key") String publicKey, @Field("private_key") String privateKey);


    //rest/Questions/removequestion.json
    @POST("rest/Questions/removequestion.json")
    @FormUrlEncoded
    Call<LoginResponse> removeQuestion(@Field("question_id") String questionId, @Field("public_key") String publicKey, @Field("private_key") String privateKey);


    @POST("rest/Users/like.json")
    @FormUrlEncoded
    Call<LoginResponse> likedResponse(@Field("answer_id") String answerId,
                                      @Field("public_key") String email,
                                      @Field("private_key") String mobile);


    @POST("rest/Users/dislike.json")
    @FormUrlEncoded
    Call<LoginResponse> dislikedResponse(@Field("answer_id") String answerId,
                                         @Field("public_key") String email,
                                         @Field("private_key") String mobile);


    @POST("rest/Users/replyquestion.json")
    @FormUrlEncoded
    Call<LoginResponse> replyResponse(@Field("question_id") String questionId,
                                      @Field("reply") String answer,
                                      @Field("public_key") String email,
                                      @Field("private_key") String mobile);*/


}
