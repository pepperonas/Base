package com.pepperonas.sample_asynctask;

import android.os.AsyncTask;
import android.util.Log;

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
public class SampleAsyncTask extends AsyncTask<String, String, String> {

    private static final String TAG = "SampleAsyncTask";

    private MainActivity mMain;

    private SampleAsyncTaskListener listener;


    public SampleAsyncTask(MainActivity main, SampleAsyncTaskListener l) {
        mMain = main;
        listener = l;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        Log.d(TAG, "doInBackground Url: " + params[0]);

        StringBuilder stringBuilder = new StringBuilder();

        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpGet httpPost = new HttpGet(params[0]);
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity ht = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            InputStream is = buf.getContent();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = r.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

        } catch (IOException e) {
            listener.onFailed("An error occurred.");
            e.printStackTrace();
        }

        listener.onSuccess(stringBuilder.toString());
        return stringBuilder.toString();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

