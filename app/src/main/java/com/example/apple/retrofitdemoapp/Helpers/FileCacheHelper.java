package com.example.apple.retrofitdemoapp.Helpers;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class FileCacheHelper {

    private static final String THUMBNAIL_PREFIX = "thumbnail_";

    public static File saveFileToDisk(ResponseBody body, String fileId, String fileExtension, boolean isThumbnail) {
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            File file = prepareFileName(fileId, fileExtension, isThumbnail);

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return file;

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

    public static File getFileFromDisk(String fileId, String fileExtension, boolean isThumbnail) {
        File file = prepareFileName(fileId, fileExtension, isThumbnail);
        return file;
    }

    private static File prepareFileName(String fileId, String fileExtension, boolean isThumbnail) {
        String fileName = "";
        if (isThumbnail) fileName = THUMBNAIL_PREFIX;
        fileName = String.format("%s%s.%s", fileName, fileId, fileExtension);

        File file = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS + File.separator + fileName);

        return file;
    }
}
