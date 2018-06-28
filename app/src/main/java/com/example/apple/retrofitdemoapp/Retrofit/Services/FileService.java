package com.example.apple.retrofitdemoapp.Retrofit.Services;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.apple.retrofitdemoapp.Helpers.FileCacheHelper;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class FileService extends BaseApiService {

    //[text="a80cd6da-e21e-494a-85a2-d2459a7d076c"] uploaded file 1
    //[text="acd4f7e8-bf95-471d-875c-2efa21261159"] uploaded file 2
    //https://futurestud.io/tutorials/retrofit-2-how-to-download-files-from-server

    private static final IFileService sServiceInstance = sRetrofit.create(IFileService.class);
    private static final String rootPath = ApiConfiguration.getInstance().getFileServerURL();

    public static void uploadFile(Context context, File file, String category, OnRequestComplete<ResponseBody> completeCallback) {

        String mimeType = getMimeTypeByExtension(context, file);
        MediaType mediaType = MediaType.parse(mimeType);
        RequestBody requestFile = RequestBody.create(
                mediaType, file);

        MultipartBody.Part body = MultipartBody.Part.createFormData(category,
                file.getName(), requestFile);

        String fullUrl = rootPath + "v1/file/save/" + category;
        Call<ResponseBody> uploadRequest = sServiceInstance.upload(fullUrl, body, mimeType);
        proceedAsync(uploadRequest, completeCallback);
    }

    public static void downloadFile(final Context context, final String fileId, final String fileExtension, final String format, final OnRequestComplete<File> completeCallback) {
        //Check file on disk
        final boolean isThumbnail = format != null;
        File cachedFile = FileCacheHelper.getFileFromDisk(context, fileId, fileExtension, isThumbnail);
        if (cachedFile != null) {
            completeCallback.onSuccess(cachedFile);
            return;
        }

        String fullUrl = rootPath + "v1/file/" + fileId;
        Call<ResponseBody> fileRequest = sServiceInstance.download(fullUrl, format);
        proceedAsync(fileRequest, new OnRequestComplete<ResponseBody>() {
            @Override
            public void onSuccess(final ResponseBody result) {

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        //Cache new file
                        File file = FileCacheHelper.saveFileToDisk(context, result, fileId, fileExtension, isThumbnail);
                        completeCallback.onSuccess(file);

                        return null;
                    }
                }.execute();
            }

            @Override
            public void onFail(String error) {
                completeCallback.onFail(error);
            }
        });
    }

    //TODO DELETE THIS SHIT
    public static String getFileExtension(File file) {
        if (file != null) {
            return android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString().toUpperCase());
        } else {
            return "";
        }
    }

    //TODO DELETE THIS SHIT
    public static String getMimeTypeByExtension(Context context, File file) {
        String extension = getFileExtension(file);
        String mime = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        if (mime == null) {
            Uri uri = Uri.fromFile(file);
            ContentResolver contentResolver = context.getContentResolver();
            mime = contentResolver.getType(uri);
        }
        return mime != null ? mime : "application/octet-stream";
    }

    private interface IFileService {

        @Multipart
        @POST
        Call<ResponseBody> upload(@Url String url, @Part MultipartBody.Part file, @Query("mimeType") String mimeType);

        @Streaming
        @GET
        Call<ResponseBody> download(@Url String url, @Query("format") String format);
    }
}
