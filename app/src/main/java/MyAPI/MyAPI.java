package MyAPI;

import java.util.List;

import model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyAPI {

    @GET("heroes")
    Call<List<User>> getUser();

    @POST("heroes")
    Call<Void> registerUser(@Body User user);
}
