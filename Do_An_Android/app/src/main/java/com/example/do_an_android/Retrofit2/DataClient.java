package com.example.do_an_android.Retrofit2;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {
    @Multipart
    @POST("upload.php")
    Call<String> UploadImage(@Part MultipartBody.Part image);

}
