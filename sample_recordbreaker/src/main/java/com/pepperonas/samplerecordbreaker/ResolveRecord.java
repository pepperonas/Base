package com.pepperonas.samplerecordbreaker;

import android.app.Activity;

import com.pepperonas.jbasx.concurrency.ThreadUtils;
import com.pepperonas.jbasx.log.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class ResolveRecord {

    private static final String TAG = "ResolveRecord";

    private OnRecordResolvedListener mListener;


    public ResolveRecord(final Activity activity, final Builder builder) {
        ThreadUtils.runInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                mListener = (OnRecordResolvedListener) activity;

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ccmp.kochab.uberspace.de/recordbreaker/resolve_data.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("_app_id", BuildConfig.APPLICATION_ID));
                nameValuePairs.add(new BasicNameValuePair("_key", builder.key));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                Log.d(TAG, "Resolving record " + response.getStatusLine());

                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {

                    Log.i(TAG, "call readLine: " + line);

                    if (line.contains("FAILED==")) {
                        if (line.contains("expired")) {
                            mListener.onRecordBreakerFailed(RecordBreaker.Status.EXPIRED);
                        } else if (line.contains("invalid_data")) {
                            mListener.onRecordBreakerFailed(RecordBreaker.Status.INVALID_DATA);
                        } else if (line.contains("no_data_found")) {
                            mListener.onRecordBreakerFailed(RecordBreaker.Status.NO_DATA_FOUND);
                        }
                        return null;
                    } else {
                        builder.append(line);
                    }
                }

                try {
                    JSONObject posts = new JSONObject(builder.toString()).getJSONObject("posts");
                    String key = posts.getString("key");
                    String ex_after_in_sec = posts.getString("exp_after_in_sec");
                    String remaining = posts.getString("remaining");
                    String value0 = posts.getString("value0");
                    String value1 = posts.getString("value1");
                    String value2 = posts.getString("value2");
                    String value3 = posts.getString("value3");

                    mListener.onRecordBreakerSuccess(RecordBreaker.Status.RESOLVE_SUCCESS, key, ex_after_in_sec, remaining, value0, value1, value2, value3);

                } catch (JSONException e) {
                    mListener.onRecordBreakerFailed(RecordBreaker.Status.PARSING_FAILED);
                    e.printStackTrace();
                }

                return null;
            }
        });
    }


    public static class Builder {

        private final Activity activity;
        private final String key;


        /**
         * @param activity The {@link Activity}, which launches the task.
         * @param key      The key which identifies data.
         */
        public Builder(Activity activity, String key) {
            this.activity = activity;
            this.key = key;
        }


        /**
         * Perform the resolve-method.
         */
        public void resolve() {
            new ResolveRecord(this.activity, this);
        }

    }
}
