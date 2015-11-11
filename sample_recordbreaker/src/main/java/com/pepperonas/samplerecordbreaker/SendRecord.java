package com.pepperonas.samplerecordbreaker;

import android.app.Activity;

import com.pepperonas.jbasx.concurrency.ThreadUtils;
import com.pepperonas.jbasx.log.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class SendRecord {

    private static final String TAG = "SendRecord";

    private OnRecordResolvedListener mListener;


    public SendRecord(final Activity activity, final Builder builder) {
        ThreadUtils.runInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ccmp.kochab.uberspace.de/recordbreaker/add_data_form.php");

                mListener = (OnRecordResolvedListener) activity;

                try {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("_app_id", BuildConfig.APPLICATION_ID));
                    nameValuePairs.add(new BasicNameValuePair("_key", builder.key));
                    nameValuePairs.add(new BasicNameValuePair("_value0", builder.value0));
                    nameValuePairs.add(new BasicNameValuePair("_value1", builder.value1));
                    nameValuePairs.add(new BasicNameValuePair("_value2", builder.value2));
                    nameValuePairs.add(new BasicNameValuePair("_value3", builder.value3));
                    if (builder.expiresAfterMin == 0) {
                        nameValuePairs.add(new BasicNameValuePair("_exp_after", String.valueOf(builder.expiresAfter)));
                    } else nameValuePairs.add(new BasicNameValuePair("_exp_after_min", String.valueOf(builder.expiresAfterMin)));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    Log.d(TAG, "Sending record:  " + response.getStatusLine());

                } catch (IOException e) {
                    mListener.onRecordBreakerSuccess(RecordBreaker.Status.SENDER_FAILED, "");
                    e.printStackTrace();
                }

                mListener.onRecordBreakerSuccess(RecordBreaker.Status.SENDER_SUCCESS, "");
                return null;
            }
        });
    }


    public static class Builder {

        private final Activity activity;
        private final String key;
        private final String value0;
        private String value1;
        private String value2;
        private String value3;
        private int expiresAfter = 24;
        private int expiresAfterMin = 0;


        /**
         * @param activity The {@link Activity}, which launches the task.
         * @param key      The key which identifies data.
         * @param value0   The value which indicates the action after the record is received on the device again.
         */
        public Builder(Activity activity, String key, String value0) {
            this.activity = activity;
            this.key = key;
            this.value0 = value0;
        }


        /**
         * @param value1 A additional value.
         */
        public Builder setValue1(String value1) {
            this.value1 = value1;
            return this;
        }


        /**
         * @param value2 A additional value.
         */
        public Builder setValue2(String value2) {
            this.value2 = value2;
            return this;
        }


        /**
         * @param value3 A additional value.
         */
        public Builder setValue3(String value3) {
            this.value3 = value3;
            return this;
        }


        /**
         * @param durationInHours The duration until the record will be unavailable for the user (default 24h).
         */
        public Builder expiresInHours(int durationInHours) {
            this.expiresAfter = durationInHours;
            return this;
        }


        /**
         * @param durationInMinutes The duration until the record will be unavailable for the user (default 24h).
         */
        public Builder expiresInMinutes(int durationInMinutes) {
            this.expiresAfterMin = durationInMinutes;
            return this;
        }


        /**
         * Perform the send-method.
         */
        public void send() {
            new SendRecord(this.activity, this);
        }

    }
}
