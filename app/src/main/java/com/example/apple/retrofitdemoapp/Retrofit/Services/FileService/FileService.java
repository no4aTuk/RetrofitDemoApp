package com.example.apple.retrofitdemoapp.Retrofit.Services.FileService;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.apple.retrofitdemoapp.Helpers.FileCacheHelper;
import com.example.apple.retrofitdemoapp.Helpers.FileHelper;
import com.example.apple.retrofitdemoapp.Models.ErrorResult;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnFileRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnRequestComplete;
import com.example.apple.retrofitdemoapp.Retrofit.Configuration.ApiConfiguration;
import com.example.apple.retrofitdemoapp.Retrofit.Services.BaseApiService;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public final class FileService extends BaseApiService {

    private static final IFileService sServiceInstance = sRetrofit.create(IFileService.class);
    private static final String rootPath = ApiConfiguration.getInstance().getFileServerURL();

    public static Map<String, OnFileRequestComplete<File>> downloadFileListener =
            new HashMap<String, OnFileRequestComplete<File>>(); //Used by FileDownloadProgressInterceptor

    public static void uploadFileWithProgress(Context context, File file, String category, OnFileRequestComplete<ResponseBody> completeCallback) {

        String mimeType = FileHelper.getMimeTypeByExtension(context, file);
        //Uncomment when do not need to detect upload progress anymore
        //MediaType mediaType = MediaType.parse(mimeType);
        //RequestBody requestFile = RequestBody.create(
        //        mediaType, file);
        ProgressRequestBody requestFile = new ProgressRequestBody(file, mimeType, completeCallback);

        MultipartBody.Part body = MultipartBody.Part.createFormData(category,
                file.getName(), requestFile);

        String fullUrl = rootPath + "v1/file/save/" + category;
        Call<ResponseBody> uploadRequest = sServiceInstance.upload(fullUrl, body, mimeType);
        proceedAsync(uploadRequest, completeCallback);
    }

    public static void downloadFileWithProgress(final Context context, final String fileId,
                                                final String fileExtension, final String format,
                                                final OnFileRequestComplete<File> completeCallback) {
        //Check file on disk
        final boolean isThumbnail = format != null;
//        File cachedFile = FileCacheHelper.getFileFromDisk(context, fileId, fileExtension, isThumbnail);
//        if (cachedFile != null) {
//            completeCallback.onSuccess(cachedFile);
//            return;
//        }

        String fullUrl = rootPath + "v1/file/" + fileId;
        FileService.downloadFileListener.put(fileId, completeCallback);
        Call<ResponseBody> fileRequest = sServiceInstance.download(fullUrl, format);
        proceedAsync(fileRequest, new OnRequestComplete<ResponseBody>() {
            @Override
            public void onSuccess(final ResponseBody result) {

                //@Streaming downloads in current Thread leads to NetworkOnMainThread exception so we need to save file in background;
                FileCacherAsyncTask fileTask = new FileCacherAsyncTask(
                        context, result, fileId, fileExtension, isThumbnail, completeCallback);
                fileTask.execute();
                FileService.downloadFileListener.remove(fileId);
            }

            @Override
            public void onFail(ErrorResult error) {
                completeCallback.onFail(error);
                FileService.downloadFileListener.remove(fileId);
            }
        });
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
