package com.pepperonas.andbasx.concurrency;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.pepperonas.andbasx.AndBasx;
import com.pepperonas.andbasx.interfaces.LoaderTaskListener;
import com.pepperonas.jbasx.log.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class LoaderTaskUtils extends AsyncTask<String, String, String> {

    private static final String TAG = "LoaderTaskUtils";

    Builder builder;


    public LoaderTaskUtils(Builder builder) {
        this.builder = builder;
        this.execute(this.builder.url);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.builder.progressDialog != null) {
            this.builder.progressDialog.show();
        }
    }


    @Override
    protected String doInBackground(String... params) {

        StringBuilder result = new StringBuilder();

        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpGet httpPost = new HttpGet(params[0]);
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity he = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(he);
            InputStream is = buf.getContent();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = r.readLine()) != null) {
                result.append(line + "\n");
            }

            if (AndBasx.mLog == AndBasx.LogMode.ALL) {
                Log.i(TAG, "Content load from web:\n" + result);
            }

        } catch (IOException e) {
            builder.loaderTaskListener.onLoaderTaskFailed(e.getMessage());
        }
        builder.loaderTaskListener.onLoaderTaskSuccess(result.toString());
        return result.toString();
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
        private ProgressDialog progressDialog;


        public Builder(Context ctx, LoaderTaskListener l, String url) {
            this.ctx = ctx;
            this.loaderTaskListener = l;
            this.url = url;
        }


        public Builder showProgressDialog(String title, String message, boolean setIndeterminate, int max) {
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setProgress(0);
            progressDialog.setMax(max);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(setIndeterminate);
            return this;
        }


        public void launch() {
            new LoaderTaskUtils(this);
        }

    }

}
