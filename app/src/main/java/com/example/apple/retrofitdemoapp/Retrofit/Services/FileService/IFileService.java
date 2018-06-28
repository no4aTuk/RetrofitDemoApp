package com.example.apple.retrofitdemoapp.Retrofit.Services.FileService;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

interface IFileService {

    @Multipart
    @POST
    Call<ResponseBody> upload(@Url String url, @Part MultipartBody.Part file, @Query("mimeType") String mimeType);

    @Multipart
    @POST
    Call<ResponseBody> uploadProgress(@Url String url, @Part MultipartBody.Part file, @Query("mimeType") String mimeType);

    @Streaming
    @Headers({
            "Content-Type: multipart/form-data"
    })
    @GET
    Call<ResponseBody> download(@Url String url, @Query("format") String format);
}
