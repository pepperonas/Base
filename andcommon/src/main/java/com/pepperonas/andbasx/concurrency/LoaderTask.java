package com.pepperonas.andbasx.concurrency;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class LoaderTask extends AsyncTask<String, String, String> {

    Builder builder;


    public LoaderTask(Builder builder) {
        this.builder = builder;
        if (this.builder.progressDialog != null) {
            this.builder.progressDialog.show();
        }
        this.execute(this.builder.url);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        for (int i = 0; i < 5000; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(String.valueOf(i));
        }

        return "ok";
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
        private final String url;
        private ProgressDialog progressDialog;


        public Builder(Context ctx, String url) {
            this.ctx = ctx;
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
            new LoaderTask(this);
        }

    }

}
