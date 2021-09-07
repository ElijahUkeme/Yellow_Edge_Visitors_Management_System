package services;

import model.LoginResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("useremail") String email,
            @Field("userphone") String phone,
            @Field("houseid") String houseid
    );
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> userLogin(
            @Field("username") String username,
            @Field("password") String password
    );
}
