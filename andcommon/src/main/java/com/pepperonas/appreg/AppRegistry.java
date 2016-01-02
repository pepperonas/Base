/*
 * Copyright (c) 2016 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pepperonas.appreg;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.pepperonas.jbasx.concurrency.ThreadUtils;
import com.pepperonas.jbasx.format.TimeFormatUtils;
import com.pepperonas.jbasx.log.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class AppRegistry {

    public enum StatusCode {
        UNKNOWN_FAILURE(-399),
        EXCEPTION(-301),
        IO_EXCEPTION(-300),
        DEFAULT_ERROR(-1);

        private int i;


        StatusCode(int i) {
            this.i = i;
        }

    }


    private static final String TAG = "AppRegistry";

    public static final String DEFAULT_RESPONSE_STRING = "MISSING_RESPONSE";

    private OnRegisterResultListener mListener;


    public AppRegistry(final Builder builder) {
        ThreadUtils.runInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ccmp.kochab.uberspace.de/app_registry/user_validation.php");

                if (builder.activity instanceof OnRegisterResultListener) {
                    mListener = (OnRegisterResultListener) builder.activity;
                }

                String responseString = DEFAULT_RESPONSE_STRING;
                int httpStatus = 0;

                try {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("dev_id", builder.devId));
                    nameValuePairs.add(new BasicNameValuePair("app_id", builder.appId));
                    nameValuePairs.add(new BasicNameValuePair("user_id", builder.userId));
                    nameValuePairs.add(new BasicNameValuePair("stamp", builder.timestamp != null ? String.valueOf(builder.timestamp)
                                                                                                 : null));
                    nameValuePairs.add(new BasicNameValuePair("expires", String.valueOf(builder.expires)));
                    nameValuePairs.add(new BasicNameValuePair("any_text", builder.anyText));
                    nameValuePairs.add(new BasicNameValuePair("any_int", String.valueOf(builder.anyInt)));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);

                    httpStatus = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();
                    responseString = EntityUtils.toString(entity, "UTF-8");

                } catch (IOException e) {
                    if (mListener != null) {
                        mListener.onFailed(StatusCode.IO_EXCEPTION, httpStatus, null);
                    }
                    if (builder.listener != null) {
                        builder.listener.onFailed(StatusCode.IO_EXCEPTION, httpStatus, null);
                    }
                    e.printStackTrace();
                } catch (Exception e) {
                    if (mListener != null) {
                        mListener.onFailed(StatusCode.EXCEPTION, httpStatus, null);
                    }
                    if (builder.listener != null) {
                        builder.listener.onFailed(StatusCode.EXCEPTION, httpStatus, null);
                    }
                }

                if (responseString.contains("user_exists")) {

                    Log.d(TAG, "call " + responseString);

                    Long stamp = null;
                    String extraString = null;
                    Integer extraInt = null;

                    try {
                        stamp = TimeFormatUtils.timestampToMillis(responseString.split("<stamp>")[1]);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception while getting timestamp");
                    }
                    try {
                        extraString = responseString.split("<extra_string>")[1].split("</extra_string>")[0];
                    } catch (Exception e) {
                        Log.e(TAG, "Exception while getting extraString");
                    }
                    try {
                        extraInt = Integer.parseInt(responseString.split("<extra_int>")[1].split("</extra_int>")[0]);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception while getting extraInt");
                    }

                    if (mListener != null) {
                        mListener.onUserExists(responseString, stamp, extraString, extraInt);
                    }
                    if (builder.listener != null) {
                        builder.listener.onUserExists(responseString, stamp, extraString, extraInt);
                    }
                    return null;
                } else if (responseString.contains("user_registered")) {
                    if (mListener != null) {
                        mListener.onUserRegistered(responseString);
                    }
                    if (builder.listener != null) {
                        builder.listener.onUserRegistered(responseString);
                    }
                    return null;
                } else if (mListener != null) {
                    mListener.onFailed(StatusCode.UNKNOWN_FAILURE, httpStatus, responseString);
                }
                if (builder.listener != null) {
                    builder.listener.onFailed(StatusCode.UNKNOWN_FAILURE, httpStatus, responseString);
                }
                return null;
            }
        });
    }


    public static class Builder {

        private final Activity activity;
        private final String devId;
        private final String appId;
        private final String userId;
        private Long timestamp = 0L;
        private OnRegisterResultListener listener;
        private String anyText = "";
        private int anyInt = 0;
        private long expires = 0;


        public Builder(Activity activity, String devId, String appId, String userId) {
            this.activity = activity;
            this.devId = devId;
            this.appId = appId;
            this.userId = userId;
        }


        public Builder setOnRegisterUserListener(OnRegisterResultListener listener) {
            this.listener = listener;
            return this;
        }


        public Builder setTimestamp(@Nullable Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }


        public Builder setAnyText(String anyText) {
            this.anyText = anyText;
            return this;
        }


        public Builder setAnyInt(int anyInt) {
            this.anyInt = anyInt;
            return this;
        }


        public Builder expires(int expires) {
            this.expires = expires;
            return this;
        }


        public void send() {
            new AppRegistry(this);
        }

    }

}
