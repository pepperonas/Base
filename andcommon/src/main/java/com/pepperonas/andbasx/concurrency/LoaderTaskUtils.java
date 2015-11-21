package com.pepperonas.andbasx.concurrency;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.pepperonas.andbasx.AndBasx;
import com.pepperonas.andbasx.interfaces.LoaderTaskListener;
import com.pepperonas.jbasx.io.IoUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class LoaderTaskUtils extends AsyncTask<String, String, String> {

    private static final String TAG = "LoaderTaskUtils";


    public enum Action {
        GET_TEXT(0),
        STORE_FILE(10);

        int i;


        Action(int i) {
            this.i = i;
        }
    }


    private Builder builder;


    public LoaderTaskUtils(Builder builder) {
        this.builder = builder;
        this.execute(this.builder.url);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.builder.progressDialog != null) {
            if (this.builder.action == Action.GET_TEXT) {
                this.builder.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                this.builder.progressDialog.setIndeterminate(true);
            }
            this.builder.progressDialog.show();
        }
    }


    @Override
    protected String doInBackground(String... params) {
        URL url;
        OutputStream output = null;
        InputStream is = null;

        try {
            int count;

            url = new URL(params[0]);

            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            urlConnection.setReadTimeout(builder.readTimeout);
            urlConnection.setConnectTimeout(builder.connectionTimeout);

            int length = urlConnection.getContentLength();
            is = new BufferedInputStream(url.openStream(), 8192);

            if (builder.action == Action.STORE_FILE) {

                output = new FileOutputStream(new File(builder.dirPath, builder.fileName + builder.extension));
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = is.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / length));
                    output.write(data, 0, count);
                }

                builder.loaderTaskListener.onLoaderTaskSuccess(builder.action, "File successfully stored.");
                return "";
            } else if (builder.action == Action.GET_TEXT) {
                String text = IoUtils.convertStreamToString(is);
                builder.loaderTaskListener.onLoaderTaskSuccess(builder.action, text);
                return "";
            }
        } catch (IOException e) {
            builder.loaderTaskListener.onLoaderTaskFailed(builder.action, "An error occurred.");
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    @Override
    protected void onProgressUpdate(String... values) {

        if (this.builder.progressDialog != null && values[0] != null) {
            this.builder.progressDialog.setProgress(Integer.parseInt(values[0]));
        }

        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (builder.progressDialog != null) {
            builder.progressDialog.dismiss();
        }
    }


    public static class Builder {

        private final Context ctx;
        private final LoaderTaskListener loaderTaskListener;
        private final String url;
        private int connectionTimeout = 5000;
        private int readTimeout = 5000;
        private Action action = Action.GET_TEXT;
        private String dirPath;
        private String fileName;
        private String extension;
        private ProgressDialog progressDialog;


        public Builder(Context context, LoaderTaskListener loaderTaskListener, String url) {
            this.ctx = context;
            this.loaderTaskListener = loaderTaskListener;
            this.url = url;
        }


        public Builder storeContent(Action action, String dirPath, String fileName, String extension) {
            this.action = action;
            this.dirPath = dirPath;
            this.fileName = fileName;
            if (!extension.contains(".")) {
                extension = "." + extension;
            }
            this.extension = extension;
            return this;
        }


        public Builder showProgressDialog(int stringIdTitle, int stringIdMessage) {
            showProgressDialog(AndBasx.getContext().getString(stringIdTitle),
                               AndBasx.getContext().getString(stringIdMessage));
            return this;
        }


        public Builder showProgressDialog(String title, String message) {
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            return this;
        }


        public Builder setConnectionTimeout(int timeout) {
            connectionTimeout = timeout;
            return this;
        }


        public Builder setReadTimeout(int timeout) {
            readTimeout = timeout;
            return this;
        }


        public void launch() {
            new LoaderTaskUtils(this);
        }

    }

}
