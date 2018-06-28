package com.example.apple.retrofitdemoapp.Retrofit.Services.FileService;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.example.apple.retrofitdemoapp.Helpers.FileCacheHelper;
import com.example.apple.retrofitdemoapp.MainActivity;
import com.example.apple.retrofitdemoapp.R;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

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

public final class FileService extends BaseApiService {

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

                //Streaming downloads in current Thread leads to NetworkOnMainThread exception so we need to save file in background;
                FileCacherAsyncTask fileTask = new FileCacherAsyncTask(
                        context, result, fileId, fileExtension, isThumbnail, completeCallback);
                fileTask.execute();
            }

            @Override
            public void onFail(String error) {
                completeCallback.onFail(error);
            }
        });
    }

    //TODO DELETE THIS SHIT LATER
    public static String getFileExtension(File file) {
        if (file != null) {
            return android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString().toUpperCase());
        } else {
            return "";
        }
    }

    //TODO DELETE THIS SHIT LATER
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

    private static class FileCacherAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> context;
        private String fileId;
        private String fileExtension;
        private boolean isThumbnail;
        private ResponseBody file;
        private OnRequestComplete<File> cb;

        // only retain a weak reference to the activity
        FileCacherAsyncTask(final Context context, ResponseBody file, final String fileId, final String fileExtension,
                            boolean isThumbnail, final OnRequestComplete<File> completeCallback) {
            this.context = new WeakReference<>(context);
            this.fileId = fileId;
            this.fileExtension = fileExtension;
            this.isThumbnail = isThumbnail;
            this.file = file;
            this.cb = completeCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {

            //Cache new file
            if (context == null) return null;
            File file = FileCacheHelper.saveFileToDisk(context.get(), this.file, fileId, fileExtension, isThumbnail);
            cb.onSuccess(file);

            return null;
        }
    }
}
