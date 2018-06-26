package com.example.apple.retrofitdemoapp.Retrofit.Services;

import com.example.apple.retrofitdemoapp.Models.GitHubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public final class GitHubService extends BaseApiService {

    private static IGitHubService sServiceInstance = sRetrofit.create(IGitHubService.class);

    public static void getRepos(String userName, Callback<List<GitHubRepo>> callback) {
        Call<List<GitHubRepo>> reposRequest = sServiceInstance.listRepos(userName);
        reposRequest.enqueue(callback);
    }

    private interface IGitHubService {
        String rootPath = "users";

        @GET(rootPath + "{user}/repos")
        Call<List<GitHubRepo>> listRepos(@Path("user") String user);

        @FormUrlEncoded
        @POST("user/edit")
        Call<Object> updateUser(@Field("first_name") String first, @Field("last_name") String last);
    }
}
