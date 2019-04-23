package com.fixpapa.ffixpapa.Services.Rest;

import android.content.Context;
import android.content.SharedPreferences;

import com.fixpapa.ffixpapa.EngineerPart.EngineerInfoModule.ScheduleMod.SuccC;
import com.fixpapa.ffixpapa.EngineerPart.EngineerInfoModule.SuccessEn;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.ChangePass.Succ;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.JobIndispute.SuccessI;
import com.fixpapa.ffixpapa.UserPart.HomePart.Model.Success;
import com.fixpapa.ffixpapa.UserPart.Model.FbloginModel.SuccessFb;
import com.fixpapa.ffixpapa.UserPart.Model.NotificationModel.SuccessNoti;
import com.fixpapa.ffixpapa.UserPart.Model.SignUpModel.SucessModel.SuccessModel;
import com.fixpapa.ffixpapa.UserPart.Model.UserLoginData.SucessLogin;
import com.fixpapa.ffixpapa.VendorPart.Model.EngineerModel.SucessEngineerModel;
import com.fixpapa.ffixpapa.VendorPart.Model.NewJobs.SuccessModelVendor;
import com.fixpapa.ffixpapa.VendorPart.Model.OverViewModule;
import com.fixpapa.ffixpapa.VendorPart.Model.RatingModel.SuccessMM;
import com.fixpapa.ffixpapa.VendorPart.Model.SmallComponent.SmallSucess;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * Created by Advosoft2 on_toggle 4/18/2018.
 */

public interface ApiInterface {

   /* SharedPreferences sharedPref = getSharedPreferences("LOGINVALUES",
            Context.MODE_PRIVATE);
    String  ACCESS_TOKEN = sharedPref.getString("ACCESS_TOKEN", "");

    String accesstoke = ACCESS_TOKEN;*/

    @Multipart
    @POST("api/People/signup")
    Call<SuccessModel> userRegister(@Part MultipartBody.Part image, @Part("data") RequestBody name);


    @FormUrlEncoded
    @POST("api/People/login")
    Call<SucessLogin> userLogin(@FieldMap Map<String, String> filters);
    /*@POST("login")
    Call<GetUserLoginData> userLogin(@QueryMap Map<String, String> filters);*/

    @GET("api/Categories/allData")
    Call<Success> userService();

    @FormUrlEncoded
    @POST("api/People/verifyMobile")
    Call<SuccessModel> verifyOtp(@FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/People/resendOtp")
    Call<SuccessModel> resendOtp(@FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/People/reset")
    Call<SuccessModel> resetPassword(@FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/People/editCustomer")
    Call<SuccessModel> updateUserDetail(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);


    @Multipart
    @POST("api/People/uploadProfilePic")
    Call<SuccessModel> updateUserProfile(@Header("Authorization") String authorization, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("api/People/editEmail")
    Call<SuccessModel> updateUserEmail(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/People/editMobile")
    Call<SuccessModel> updateUserMobile(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @GET("api/Services/getAllServices")
    Call<OverViewModule> getExpertQuality();


    //POST /People/addEngineer
    @Multipart
    @POST("api/People/addEngineer")
    Call<SuccessModel> vendorRegister(@Header("Authorization") String authorization, @Part MultipartBody.Part image, @Part("data") RequestBody name);


  /*  @GET("People/getAddresses/?access_token="+ accesstoke)
    Call<SuccessModelVendor> getAddresses();*/

    @GET("api/People/getAddresses")
    Call<SuccessModel> getAddresses(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/People/manageAddresses")
    Call<SuccessModel> manageAddresses(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/People/change-password")
    Call<Succ> changePassword(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @POST("api/Requestamcs/createAmc")
    Call<SuccessModel> sendAmcRequest(@Header("Authorization") String authorization, @Body JsonObject similarJobParameter);


    @POST("api/Requestrents/createRentReq")
    Call<SuccessModel> sendRentRequest(@Header("Authorization") String authorization, @Body JsonObject similarJobParameter);

    @POST("api/Requestbids/createBid")
    Call<SuccessModel> sendOfficeSetupRequest(@Header("Authorization") String authorization, @Body JsonObject similarJobParameter);

    @Multipart
    @POST("api/Requestjobs/createJob")
    Call<SucessLogin> postFixProblem(@Header("Authorization") String authorization, @Part List<MultipartBody.Part> photos, @Part("data") RequestBody name);

    @POST("api/Requestpurchases/createPurchase")
    Call<SuccessModel> sendPurchaseRequest(@Header("Authorization") String authorization, @Body JsonObject similarJobParameter);


    @GET("api/Requestjobs/vendorJobs")
    Call<SuccessModelVendor> getNewVendorJobs(@Header("Authorization") String authorization, @Query("vendorId") String callback);


    @GET("api/Requestjobs/custAllJobs")
    Call<SuccessModelVendor> getBookingDetail(@Header("Authorization") String authorization);

    /*@GET("api/Requestjobs/custAllJobs")
    Call<SuccessModelVendor> getBookingDetailOpen(@Header("Authorization") String authorization);
*/

    @GET("api/People/getEngineers")
    Call<SucessEngineerModel> getAllEngineers(@Header("Authorization") String authorization, @Query("vendorId") String callback);

    /* @GET("People/inactiveEngineer")
     Call<SucessEngineerModel> activeInactiveEngineers(@Header("Authorization") String authorization,@Body JsonObject jsonObject);
 */
    @FormUrlEncoded
    @POST("api/People/actInacEngineers")
    Call<SuccessModel> activeInactiveEngineers(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/People/editEngineer")
    Call<SuccessModel> editEngineer(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/Requestjobs/vendorAcceptorCancel")
    Call<SmallSucess> acceptVendor(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);


    @GET("api/Requestjobs/vendorAllJobs")
    Call<SuccessModelVendor> getVendorJobs(@Header("Authorization") String authorization, @Query("vendorId") String callback);

    @FormUrlEncoded
    @POST("api/People/changePwd")
    Call<SuccessModel> changeEngineerPassword(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @GET("api/People/getFreeEngineers")
    Call<SucessEngineerModel> getAllFreeEngineers(@Header("Authorization") String authorization, @Query("vendorId") String callback);

    @FormUrlEncoded
    @POST("api/Requestjobs/assignEngineer")
    Call<SmallSucess> assignEngineer(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/Requestjobs/cancelJob")
    Call<SmallSucess> cancelJobByVendor(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);


    @GET("api/Requestjobs/engineerNewJobs")
    Call<SuccessModelVendor> getEngineerJobs(@Header("Authorization") String authorization);

    @GET("api/Requestjobs/engineerAllJobs")
    Call<SuccessModelVendor> getEngineerAllJobs(@Header("Authorization") String authorization);


    @FormUrlEncoded
    @POST("api/Requestjobs/engineerAccept")
    Call<SmallSucess> engineerAcceptJobRequest(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/Requestjobs/engineerSchedule")
    Call<SuccC> scheduleJobRequest(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);

    @Multipart
    @POST("api/Requestjobs/generateBill")
    Call<SmallSucess> generateBill(@Header("Authorization") String authorization, @Query("requestjobId") String callback,
                                   @Part("data") RequestBody name, @Part MultipartBody.Part image);

    @Multipart
    @POST("api/Requestjobs/pickProduct")
    Call<SmallSucess> pickPro(@Header("Authorization") String authorization, @Query("requestjobId") String callback,
                              @Part("data") RequestBody name, @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("api/Requestjobs/startJob")
    Call<SmallSucess> startJobEngineer(@Header("Authorization") String authorization,
                                       @Query("requestjobId") String callback, @FieldMap Map<String, String> filters);


    @POST("api/Requestjobs/completeJob")
    Call<SmallSucess> completeJob(@Header("Authorization") String authorization,
                                  @Body JsonObject valuesGroup);

    @POST("api/Requestjobs/completeJob")
    Call<SuccessI> completeJobPen(@Header("Authorization") String authorization,
                                  @Body JsonObject valuesGroup);


    @POST("api/Ratings/giveRating")
    Call<SuccessMM> giveRating(@Header("Authorization") String authorization,
                               @Body JsonObject valuesGroup);

    @FormUrlEncoded
    @POST("api/Requestjobs/updateStatus")
    Call<SmallSucess> updateJobStatus(@Header("Authorization") String authorization,
                                      @Query("requestjobId") String callback, @FieldMap Map<String, String> filters);

    @POST("api/Requestjobs/partRequest")
    Call<SmallSucess> addpartRequest(@Header("Authorization") String authorization,
                                     @Query("requestjobId") String callback, @Body JsonObject valuesGroup);

    @FormUrlEncoded
    @POST("api/Requestjobs/updateJobStatus")
    Call<SmallSucess> updateDeliverStatus(@Header("Authorization") String authorization,
                                          @Query("requestjobId") String callback, @FieldMap Map<String, String> filters);

    @Multipart
    @POST("api/People/uploadEngineerPic")
    Call<SuccessModel> updateEngineerProfile(@Header("Authorization") String authorization, @Part("engineerId") RequestBody callback, @Part MultipartBody.Part image);


    @GET("api/People/googleLogin")
    Call<SuccessFb> googleLogin(@Query("firebaseToken") String fire, @Query("realm") String rea,
                                @Query("access_token") String accesstoke);


    @GET("auth/facebook/token")
    Call<SuccessFb> facebookLogin(@Query("firebaseToken") String fire, @Query("realm") String rea,
                                  @Query("access_token") String accesstoke);


    @GET("api/People/logout")
    Call<SuccessModelVendor> logoutAllServices(@Header("Authorization") String authorization);

    @GET("api/Notifications/getNotifications")
    Call<SuccessNoti> getAllNotification(@Header("Authorization") String authorization);

    @POST("api/Transactions/payment")
    Call<SmallSucess> callPaytm(@Body JsonObject similarJobParameter);


   /* @GET("api/Transactions/payment")
    Call<SmallSucess> callPaytm(@Query("MID") String marchantId,@Query("ORDER_ID") String orderId,@Query("CUST_ID") String cusId
            ,@Query("INDUSTRY_TYPE_ID") String indusType,@Query("CHANNEL_ID") String channelID,@Query("TXN_AMOUNT") String txtAmount
    ,@Query("WEBSITE") String website,@Query("EMAIL") String email,@Query("MOBILE_NO") String mobileno);
*/


    @POST("api/Requestjobs/partResponse")
    Call<SmallSucess> partRequest(@Header("Authorization") String authorization, @Body JsonObject valuesGroup);


    // @FormUrlEncoded
    @GET("api/Requestjobs/getJobById")
    Call<SmallSucess> getJobDetail(@Header("Authorization") String authorization,
                                   @Query("requestjobId") String callback);

    @FormUrlEncoded
    @POST("api/Transactions/cashPayment")
    Call<SmallSucess> cashPayment(@Header("Authorization") String authorization,
                                  @FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/People/vendorAvailability")
    Call<SmallSucess> vendorVisibility(@Header("Authorization") String authorization,
                                       @FieldMap Map<String, String> filters);


    @FormUrlEncoded
    @POST("api/Transactions/chequePayment")
    Call<SmallSucess> submitBankDetail(@Header("Authorization") String authorization,
                                       @FieldMap Map<String, String> filters);

    @FormUrlEncoded
    @POST("api/People/editEmail_match_otp")
    Call<SuccessModel> updateEmail(@Header("Authorization") String authorization, @FieldMap Map<String, String> filters);


    @GET("api/People/viewProfile")
    Call<SuccessEn> getEgineerInfo(@Header("Authorization") String authorization, @Query("peopleId") String callback1, @Query("realm") String callback);

}
