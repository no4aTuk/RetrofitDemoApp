package com.example.apple.retrofitdemoapp.Helpers;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class FileCacheHelper {

    private static final String THUMBNAIL_PREFIX = "thumbnail_";
    private static final int MAX_FILE_SIZE_MB = 100;

    public static File saveFileToDisk(Context context, ResponseBody body, String fileId, String fileExtension, boolean isThumbnail) {
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            File file = prepareFileName(context, fileId, fileExtension, isThumbnail, true);

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

    public static File getFileFromDisk(Context context, String fileId, String fileExtension, boolean isThumbnail) {
        File file = prepareFileName(context, fileId, fileExtension, isThumbnail, false);
        return file;
    }

    private static File prepareFileName(Context context, String fileId, String fileExtension, boolean isThumbnail, boolean isSaving) {
        //Check is external storage available
        if (isSaving && !isExternalStorageWritable() || isSaving && !checkAvailableSpace(MAX_FILE_SIZE_MB)) {
            return null;
        }
        if (!isSaving && !isExternalStorageReadable()) {
            return null;
        }

        File fileFolder;
        File resultFile;

        String fileName = "";
        if (isThumbnail) fileName = THUMBNAIL_PREFIX;
        fileName = String.format("%s%s.%s", fileName, fileId, fileExtension);

        if (isThumbnail) {
            fileFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        } else {
            fileFolder = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
        }
        resultFile = new File(fileFolder.getAbsoluteFile() + File.separator + fileName);

        if (!fileFolder.exists()) {
            fileFolder.mkdir();
        }
        if (!resultFile.exists()) {
            try {
                resultFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultFile;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private static boolean checkAvailableSpace(int threshold) {
        if (isExternalStorageWritable()) {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bytesAvailable = 0l;
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
            bytesAvailable = bytesAvailable / 1048576;
            return bytesAvailable > threshold;
        }
        return false;
    }
}
