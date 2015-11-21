package com.pepperonas.sample_asynctask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements SampleAsyncTaskListener {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SampleAsyncTask(this).execute("http://ws.spotify.com/lookup/1/?uri=spotify:track:6NmXV4o6bmp704aPGyTVVG");


        new SampleAsyncTask(new SampleAsyncTaskListener() {
            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "onSuccess " + s);
            }


            @Override
            public void onFailed(String s) {
                Log.e(TAG, "onFailed " + s);
            }
        }).execute("http://ws.spotify.com/lookup/1/?uri=spotify:track:6NmXV4o6bmp704aPGyTVVG");

    }


    @Override
    public void onSuccess(String s) {
        Log.i(TAG, "onSuccess " + s);
    }


    @Override
    public void onFailed(String s) {
        Log.e(TAG, "onFailed " + s);
    }
}
