package MyAPI;

import java.util.List;
import java.util.Map;

import model.ImageResponse;
import model.LoginSignupResponse;
import model.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyAPI {


    @GET("heroes")
    Call<List<User>> getAllHeroes();

    // 1. Using @object
    @POST("heroes")
    Call<Void> registerUser(@Header("Cookie") String cookie, @Body User user);

    // 2. Using @field
    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addHero(@Header("Cookie") String cookie, @Field("name") String name, @Field("desc") String desc);

    //3. Using @FieldMap
    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addHero2(@FieldMap Map<String, String> map);

    //For uploading image
    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

    //for login
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginSignupResponse> checkUser(@Field("username") String username, @Field("password") String password);

    //For Register
    @FormUrlEncoded
    @POST("users/signup")
    Call<LoginSignupResponse> registerUser(@Field("username") String username, @Field("password") String password);


}