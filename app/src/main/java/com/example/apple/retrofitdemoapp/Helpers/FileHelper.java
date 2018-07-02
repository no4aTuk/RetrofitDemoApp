package com.example.apple.retrofitdemoapp.Helpers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import com.example.apple.retrofitdemoapp.Enums.FileCategory;

import java.io.File;
import java.util.regex.Pattern;

public class FileHelper {

    public static File generateFile(Context context, Uri uri) {
        if (context != null && uri != null) {
            String path = getFilePath(context, uri);
            if (path != null) {
                return new File(path);
            }
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getFilePath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // non-primary volumes
                return "/storage/" + type + "/" + split[1];
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndex(column);
                if (column_index >= 0) {
                    return cursor.getString(column_index);
                }

            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static String getFileExtension(File file) {
        if (file != null) {
            return android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString().toUpperCase());
        } else {
            return "";
        }
    }

    public static FileCategory getFileCategory(String mime) {
        if (mime == null) {
            return FileCategory.Document;
        } else {
            Pattern imagePattern = Pattern.compile("image/.+");
            Pattern videoPattern = Pattern.compile("video/.+");
            Pattern audioPattern = Pattern.compile("audio/.+");
            Pattern textPattern = Pattern.compile("text/.+");

            if ((imagePattern.matcher(mime)).matches()) {
                if (mime.equals("image/vnd.djvu")) {
                    return FileCategory.Document;
                }
                return FileCategory.Image;
            }
            if ((videoPattern.matcher(mime)).matches()) {
                return FileCategory.Video;
            }
            if ((audioPattern.matcher(mime)).matches()) {
                return FileCategory.Audio;
            }
            if ((textPattern.matcher(mime)).matches()) {
                return FileCategory.Document;
            }
        }
        return FileCategory.Document;
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
}
