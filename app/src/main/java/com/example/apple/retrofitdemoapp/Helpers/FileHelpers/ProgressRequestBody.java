package com.example.apple.retrofitdemoapp.Helpers.FileHelpers;

import android.os.Handler;
import android.os.Looper;

import com.example.apple.retrofitdemoapp.Retrofit.CompleteCallbacks.OnFileRequestComplete;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private File mFile;
    private String mMimeType;
    private OnFileRequestComplete<ResponseBody> mListener;

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public ProgressRequestBody(final File file, String mimeType, final  OnFileRequestComplete<ResponseBody> listener) {
        mFile = file;
        mMimeType = mimeType;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(mMimeType);
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgress((int)(100 * mUploaded / mTotal));
        }
    }
}
