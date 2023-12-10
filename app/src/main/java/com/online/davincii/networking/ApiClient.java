package com.online.davincii.networking;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.online.davincii.models.AddCartResponse;
import com.online.davincii.models.AddCreationRequest;
import com.online.davincii.models.AddCreationResponse;
import com.online.davincii.models.BuyCreationRequest;
import com.online.davincii.models.BuyCreationResponse;
import com.online.davincii.models.CanvasListRequest;
import com.online.davincii.models.CanvasListResponse;
import com.online.davincii.models.CanvasSizeData;
import com.online.davincii.models.CanvasSizeRequest;
import com.online.davincii.models.CanvasSizeResponse;
import com.online.davincii.models.CategoriesListResponse;
import com.online.davincii.models.DeleteCreationRequest;
import com.online.davincii.models.DeleteCreationResponse;
import com.online.davincii.models.EditProfileRequest;
import com.online.davincii.models.EditProfileResponse;
import com.online.davincii.models.FeedRequest;
import com.online.davincii.models.FeedResponse;
import com.online.davincii.models.ForgotRequest;
import com.online.davincii.models.ForgotResponse;
import com.online.davincii.models.HomeLikeRequest;
import com.online.davincii.models.HomeLikeResponse;
import com.online.davincii.models.HomeSupportRequest;
import com.online.davincii.models.HomeSupportResponse;
import com.online.davincii.models.LikeUserResponse;
import com.online.davincii.models.LogOutResponse;
import com.online.davincii.models.LoginRequest;
import com.online.davincii.models.LoginResponse;
import com.online.davincii.models.MyCartDeleteRequest;
import com.online.davincii.models.MyCartDeleteResponse;
import com.online.davincii.models.MyCartQuantityRequest;
import com.online.davincii.models.MyCartQuantityResponse;
import com.online.davincii.models.MyCartRequest;
import com.online.davincii.models.MyCartResponse;
import com.online.davincii.models.OrderDetailData;
import com.online.davincii.models.OrderDetailRequest;
import com.online.davincii.models.OrderDetailResponse;
import com.online.davincii.models.OrderListResponse;
import com.online.davincii.models.ProfileResponse;
import com.online.davincii.models.RegisterRequest;
import com.online.davincii.models.SearchRequest;
import com.online.davincii.models.SearchResponse;
import com.online.davincii.models.SoldProductResponse;
import com.online.davincii.models.UpdateTokenRequest;
import com.online.davincii.models.UpdateTokenResponse;
import com.online.davincii.models.UserProfileRespose;
import com.online.davincii.models.addresses.AddressRequest;
import com.online.davincii.models.addresses.AddressResponse;
import com.online.davincii.models.chat.RootModel;
import com.online.davincii.models.comments.AllCommentResponse;
import com.online.davincii.models.comments.AllCommentsRequest;
import com.online.davincii.models.comments.CommentLikeRequest;
import com.online.davincii.models.comments.CommentLikeResponse;
import com.online.davincii.models.comments.CommentMakeRequest;
import com.online.davincii.models.comments.CommentMakeResponse;
import com.online.davincii.models.comments.ReplyCommentRequest;
import com.online.davincii.models.comments.ReplyCommentResponse;
import com.online.davincii.models.comments.ReplyLikeRequest;
import com.online.davincii.models.comments.ReplyLikeResponse;
import com.online.davincii.models.discoverydetails.DiscoveryRequest;
import com.online.davincii.models.discoverydetails.DiscoveryResponses;
import com.online.davincii.models.discoverysearch.DC_AF_SeeAll_Response;
import com.online.davincii.models.discoverysearch.DC_ArtFu_SeeAllRequest;
import com.online.davincii.models.discoverysearch.DC_TS_SeeAllResponse;
import com.online.davincii.models.discoverysearch.DC_TopStu_SeeAllResquest;
import com.online.davincii.models.discoverysearch.DiscoveryResponse;
import com.online.davincii.models.StudioprofileRequest;
import com.online.davincii.models.StudioprofileResponse;
import com.online.davincii.models.order.NFTRequest;
import com.online.davincii.models.order.OrderRequest;
import com.online.davincii.models.order.OrderResponse;
import com.online.davincii.models.registger.ImageData;
import com.online.davincii.models.registger.RegisterResponse;
import com.online.davincii.models.ResetPasswordRequest;
import com.online.davincii.models.ResetPasswordResponse;
import com.online.davincii.models.SupportResponse;

import com.online.davincii.models.VerificationcodeRequest;
import com.online.davincii.models.VerificationcodeResponse;
import com.online.davincii.models.sales_report.ReportListResponse;
import com.online.davincii.models.sales_report.ReportRequest;
import com.online.davincii.models.sales_report.ReportResponse;
import com.online.davincii.models.sales_report.SalesReportRequest;
import com.online.davincii.models.sales_report.SalesReportResponse;
import com.online.davincii.models.sales_report.SupportersResponse;
import com.online.davincii.utils.Constant;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class ApiClient {

    public static APIInterface getClient() {

        // Create a trust manager that does not validate certificate chains
        @SuppressLint("TrustAllX509TrustManager") final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .sslSocketFactory(Objects.requireNonNull(getSSLSocketFactory()), (X509TrustManager) trustAllCerts[0])

                .addInterceptor(interceptor).build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(APIInterface.class);
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("TrustAllX509TrustManager") final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }
    }

    public interface APIInterface {
        //TODO Firebase notification
        @Headers({"Authorization: key=" + "AAAAsPPSle0:APA91bEibzE9OCRIHYcH4yc_6E47eUjP69WzL02Zm8NUaFDdYXgjH47vQODvwQA0Z_i5dMPkKJJbbXr_CAbpY5OK_Av9urEZcNLNj30AAdP9RrcLdlBuPnwP_GIuv3g4xM8vyGccYfpQ", "Content-Type:application/json"})
        @POST("https://fcm.googleapis.com/fcm/send")
        Call<ResponseBody> sendNotification(@Body RootModel root);

        @POST("User2/Login")
        @Headers("Content-type: application/json")
        Call<LoginResponse> userLogin(@Body LoginRequest request);

        @POST("User2/userregister")
        @Headers("Content-type: application/json")
        Call<RegisterResponse> userRegister(@Body RegisterRequest request);

        @POST("User2/forgetpassword")
        @Headers("Content-type: application/json")
        Call<ForgotResponse> forgetPassword(@Body ForgotRequest request);

        @POST("User2/VerifyCode")
        @Headers("Content-type: application/json")
        Call<VerificationcodeResponse> verificationCode(@Body VerificationcodeRequest request);

        @POST("User2/updatepassword")
        @Headers("Content-type: application/json")
        Call<ResetPasswordResponse> updatePassword(@Body ResetPasswordRequest request);

        @GET("User2/categorystylelist")
        Call<CategoriesListResponse> getCategories();

        @GET("User2/support")
        Call<SupportResponse> getSupport();

        @POST("User2/editprofile")
        @Headers("Content-type: application/json")
        Call<EditProfileResponse> editProfile(@Header("Authorization") String token, @Body EditProfileRequest request);

        @GET("User2/Userinfo")
        Call<UserProfileRespose> getUserProfile(@Header("Authorization") String token);

        @GET("User2/Userinfo")
        Call<ProfileResponse> getProfile(@Header("Authorization") String token);

        @POST("User2/studio_profile")
        Call<UserProfileRespose> getStudioProfile(@Header("Authorization") String token, @Body FeedRequest request);

        @POST("User2/feed")
        Call<FeedResponse> getFeed(@Header("Authorization") String token, @Body FeedRequest request);

        @POST("User2/addcreation")
        Call<AddCreationResponse> addCreation(@Header("Authorization") String token, @Body AddCreationRequest request);

        @GET("User2/my_cart")
        Call<MyCartResponse> myCartItems(@Header("Authorization") String token);

        @POST("User2/add_to_cart")
        Call<AddCartResponse> addItems(@Header("Authorization") String token, @Body MyCartRequest request);

        @POST("User2/cart_quantity_change")
        Call<MyCartQuantityResponse> myCartQuantity(@Header("Authorization") String token, @Body MyCartQuantityRequest request);

        @POST("User2/delete_cart_items")
        Call<MyCartDeleteResponse> myCartDelete(@Header("Authorization") String token, @Body MyCartDeleteRequest request);

        @POST("User2/buycreation")
        Call<BuyCreationResponse> buyCreation(@Header("Authorization") String token, @Body BuyCreationRequest request);

        @GET("User2/discovery")
        Call<DiscoveryResponse> getdiscovery(@Header("Authorization") String token);

        @POST("User2/artforyou")
        Call<DC_AF_SeeAll_Response> getArtFu(@Header("Authorization") String token, @Body DC_ArtFu_SeeAllRequest request);

        @POST("User2/studios")
        Call<DC_TS_SeeAllResponse> getTopStudio(@Header("Authorization") String token, @Body DC_TopStu_SeeAllResquest resquest);

        @POST("User2/Logout")
        Call<LogOutResponse> logOut(@Header("Authorization") String token);

        @DELETE("User2/delete-account")
        Call<LogOutResponse> deleteAccount(@Header("Authorization") String token);


        @POST("User2/studio_profile")
        Call<StudioprofileResponse> studioProfile(@Header("Authorization") String token, @Body StudioprofileRequest request);

        @POST("User2/supporting")
        Call<HomeSupportResponse> homeSupport(@Header("Authorization") String token, @Body HomeSupportRequest request);

        @POST("User2/like")
        Call<HomeLikeResponse> homeLike(@Header("Authorization") String token, @Body HomeLikeRequest request);

        @POST("User2/search")
        Call<SearchResponse> callSearch(@Header("Authorization") String token, @Body SearchRequest request);

        @POST("User2/discovery_detail")
        Call<DiscoveryResponses> discoveryDetails(@Header("Authorization") String token, @Body DiscoveryRequest request);

        @POST("User2/delete_creation")
        Call<DeleteCreationResponse> deleteCreation(@Header("Authorization") String token, @Body DeleteCreationRequest request);

        @POST("User2/reply")
        Call<ReplyCommentResponse> replyComment(@Header("Authorization") String token, @Body ReplyCommentRequest request);

        @POST("User2/comment")
        Call<CommentMakeResponse> commentMake(@Header("Authorization") String token, @Body CommentMakeRequest request);

        @POST("User2/all_comments")
        Call<AllCommentResponse> allComment(@Header("Authorization") String token, @Body AllCommentsRequest request);

        @POST("User2/reply_like")
        Call<ReplyLikeResponse> replyLike(@Header("Authorization") String token, @Body ReplyLikeRequest request);

        @POST("User2/comment_like")
        Call<CommentLikeResponse> commentLike(@Header("Authorization") String token, @Body CommentLikeRequest request);

        @POST("User2/update_device_token")
        Call<UpdateTokenResponse> updateToken(@Header("Authorization") String token, @Body UpdateTokenRequest request);

        @POST("User2/notification")
        Call<SalesReportResponse> getSalesReport(@Header("Authorization") String token, @Body SalesReportRequest reportRequest);

        @POST("User2/User2_creations")
        Call<CanvasListResponse> getCanvasList(@Header("Authorization") String token, @Body CanvasListRequest request);

        @POST("User2/liked_by")
        Call<LikeUserResponse> getLikedUser(@Header("Authorization") String token, @Body CanvasListRequest request);

        @GET("User2/delievery_address_list")
        Call<AddressResponse> getAddress(@Header("Authorization") String token);

        @POST("User2/add_delievery_address")
        Call<RegisterResponse> addAddress(@Header("Authorization") String token, @Body AddressRequest request);

   //      live payment api
        @POST("User2/order")
        Call<OrderResponse> placeOrder(@Header("Authorization") String token, @Body OrderRequest request);

//        @POST("User2/ordertest_1dollar")
//        Call<OrderResponse> placeOrder(@Header("Authorization") String token, @Body OrderRequest request);

        // test payment api
//        @POST("User2/ordertest")
//        Call<OrderResponse> placeOrder(@Header("Authorization") String token, @Body OrderRequest request);

        @POST("User2/nft")
        Call<OrderResponse> nftOrder(@Header("Authorization") String token, @Body NFTRequest request);

        @GET("User2/order_list")
        Call<OrderListResponse> getOrderList(@Header("Authorization") String token);

        @POST("User2/Order_Detail")
        Call<OrderDetailResponse> orderDetail(@Header("Authorization") String token, @Body OrderDetailRequest request);

        @POST("User2/delete_saved_address")
        Call<AddressResponse> deleteAddress(@Header("Authorization") String token, @Body AddressRequest request);

        @POST("User2/edit_delievery_address")
        Call<RegisterResponse> editAddress(@Header("Authorization") String token, @Body AddressRequest request);

        @GET("User2/get_supporters")
        Call<SupportersResponse> getSupportersList(@Header("Authorization") String token);

        @GET("User2/get_report_options")
        Call<ReportListResponse> getReportOptions();

        @POST("User2/report")
        Call<ReportResponse> addReport(@Header("Authorization") String token, @Body ReportRequest request);

        @GET("User2/s3_credentials")
        Call<ImageData> getImageData();

        @GET("User2/sold_canvas")
        Call<SoldProductResponse> getSoldItem(@Header("Authorization") String token);

        @POST("User2/canvas_sizes")
        Call<CanvasSizeResponse> getCanvasSizes(@Header("Authorization") String token, @Body CanvasSizeRequest request);


    }
}
