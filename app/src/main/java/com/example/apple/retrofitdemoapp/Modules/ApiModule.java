package com.example.apple.retrofitdemoapp.Modules;

import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.CredentialsStorage;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthService;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.AuthServiceHolder;
import com.example.apple.retrofitdemoapp.Retrofit.Services.AuthService.IAuthService;
import com.example.apple.retrofitdemoapp.Retrofit.Services.ConsultationService;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.FileService;
import com.example.apple.retrofitdemoapp.Retrofit.Services.FileService.IFileService;
import com.example.apple.retrofitdemoapp.Scopes.ApiApplicationScope;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {OkHttpModule.class, ApiConfigurationModule.class})
public class ApiModule {

    @Provides
    public AuthService authService2(Retrofit retrofit, AuthServiceHolder serviceHolder, ApiConfiguration apiConfiguration, CredentialsStorage credentialsStorage) {
        IAuthService serviceInstance = retrofit.create(IAuthService.class);
        AuthService authService = new AuthService(serviceInstance, apiConfiguration, credentialsStorage);
        serviceHolder.set(authService);
        return authService;
    }

    @Provides
    public FileService fileService2(Retrofit retrofit, ApiConfiguration apiConfiguration, CredentialsStorage credentialsStorage) {
        IFileService fileService = retrofit.create(IFileService.class);
        return new FileService(fileService, apiConfiguration, credentialsStorage);
    }

    @Provides
    public ConsultationService consultationService(Retrofit retrofit, ApiConfiguration apiConfiguration, CredentialsStorage credentialsStorage) {
        ConsultationService.IConsultationService consultationService = retrofit.create(ConsultationService.IConsultationService.class);
        return new ConsultationService(consultationService, apiConfiguration, credentialsStorage);
    }

    @ApiApplicationScope
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory,
                             ApiConfiguration configuration, AuthServiceHolder authServiceHolder,
                             CredentialsStorage credentialsStorage) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(configuration.getApiURL())
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build();

        //Configure authHolder to handle refresh token logic.. so awful
        authService2(retrofit, authServiceHolder, configuration, credentialsStorage);

        return retrofit;
    }

    @ApiApplicationScope
    @Provides
    public Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @ApiApplicationScope
    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }
}
