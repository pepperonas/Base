package com.pepperonas.sample_asynctask;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SampleAsyncTask extends AsyncTask<String, String, String> {

    private static final String TAG = "SampleAsyncTask";

    private SampleAsyncTaskListener listener;


    public SampleAsyncTask(SampleAsyncTaskListener l) {
        listener = l;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        URL url;

        String text = "";

        try {

            url = new URL(params[0]);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            InputStream is = new BufferedInputStream(url.openStream(), 8192);
            text = convertStreamToString(is);

            is.close();

        } catch (IOException e) {
            listener.onFailed("An error occurred.");
            e.printStackTrace();
        }

        listener.onSuccess(text);
        return text;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


    public static String convertStreamToString(InputStream inputStream) {
        java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

