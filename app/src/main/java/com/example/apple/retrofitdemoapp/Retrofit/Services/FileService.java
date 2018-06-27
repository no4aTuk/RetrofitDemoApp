package com.example.apple.retrofitdemoapp.Retrofit.Services;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

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
import retrofit2.http.Url;

public class FileService extends BaseApiService {

    //[text="a80cd6da-e21e-494a-85a2-d2459a7d076c"] uploaded file 1
    //[text="acd4f7e8-bf95-471d-875c-2efa21261159"] uploaded file 2
    //https://futurestud.io/tutorials/retrofit-2-how-to-download-files-from-server

    private static final IFileService sServiceInstance = sRetrofit.create(IFileService.class);
    private static final String rootPath = ApiConfiguration.getInstance().getFileServerURL();

    public static void uploadFile(File file, String category, Context context, OnRequestComplete<ResponseBody> completeCallback) {

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

    public static void downloadFile(final Context context, String fileId, String format, OnRequestComplete<ResponseBody> completeCallback) {
        String fullUrl = rootPath + "v1/file/" + fileId;
        Call<ResponseBody> fileRequest = sServiceInstance.download(fullUrl, format);
        proceedAsync(fileRequest, new OnRequestComplete<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody result) {
                //TODO write into disk
                saveFileToDisk(result, context);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private static File saveFileToDisk(ResponseBody body, Context context) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(context.getExternalFilesDir(null) + File.separator + "doctor.jpeg");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return futureStudioIconFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static String getFileExtension(File file) {
        if (file != null) {
            return android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString().toUpperCase());
        } else {
            return "";
        }
    }

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

        @GET
        Call<ResponseBody> download(@Url String url, @Query("format") String format);
    }
}
