package com.pepperonas.andbasx.concurrency;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.pepperonas.andbasx.AndBasx;
import com.pepperonas.andbasx.interfaces.LoaderTaskListener;
import com.pepperonas.jbasx.io.IoUtils;
import com.pepperonas.jbasx.log.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class LoaderTaskUtils extends AsyncTask<String, String, String> {

    private static final String TAG = "LoaderTaskUtils";


    public enum Action {
        READ(0),
        RESOLVE(5),
        STORE_FILE(10);

        int i;


        Action(int i) {
            this.i = i;
        }
    }


    private Builder builder;


    public LoaderTaskUtils(Builder builder) {
        this.builder = builder;
        if (builder.params == null) {
            this.execute(this.builder.url);
        } else {
            String[] args = new String[this.builder.params.size() + 1];
            args[0] = builder.url;
            int i = 1;
            for (String s : this.builder.params) {
                args[i++] = s;
            }
            this.execute(args);
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.builder.progressDialog != null) {
            if (builder.showProgress) {
                this.builder.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                this.builder.progressDialog.setIndeterminate(true);
                this.builder.progressDialog.show();
            }
        }
    }


    @Override
    protected String doInBackground(String... args) {
        URL url;
        OutputStream output = null;
        InputStream is = null;

        try {
            int count;

            url = new URL(args[0]);

            if (builder.action == Action.READ || builder.action == Action.STORE_FILE) {

                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                urlConnection.setReadTimeout(builder.readTimeout);
                urlConnection.setConnectTimeout(builder.connectionTimeout);

                is = new BufferedInputStream(url.openStream(), 8192);

                if (builder.action == Action.STORE_FILE) {

                    int length = urlConnection.getContentLength();
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

                } else if (builder.action == Action.READ) {

                    String text = IoUtils.convertStreamToString(is);
                    builder.loaderTaskListener.onLoaderTaskSuccess(builder.action, text);
                    return "";

                }

            } else if (builder.action == Action.RESOLVE) {
                Log.i(TAG, "doInBackground " + url);
                Log.d(TAG, "doInBackground  ", args);

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(builder.readTimeout);
                conn.setConnectTimeout(builder.connectionTimeout);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                List<AbstractMap.SimpleEntry<String, String>> urlParams = new ArrayList<>();

                OutputStream os = conn.getOutputStream();
                int i = 0;
                String key = "";

                for (String param : args) {
                    if (param.equals(args[0])) continue;
                    if (i % 2 == 0) {
                        key = param;
                    } else if (i % 2 == 1) {
                        urlParams.add(new AbstractMap.SimpleEntry<>(key, param));
                    }
                    i++;
                }

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(processQuery(urlParams));
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
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


    private String processQuery(List<AbstractMap.SimpleEntry<String, String>> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        int ctr = 0;

        for (AbstractMap.SimpleEntry pair : params) {
            if (first) first = false;
            else result.append("&");

            publishProgress("" + ((ctr * 100) / params.size()));

            result.append(URLEncoder.encode((String) pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) pair.getValue(), "UTF-8"));

            ctr++;
        }

        builder.loaderTaskListener.onLoaderTaskSuccess(builder.action, result.toString());
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
        private int connectionTimeout = 15000;
        private int readTimeout = 10000;
        private Action action;
        private String dirPath;
        private String fileName;
        private String extension;
        private List<String> params;
        private ProgressDialog progressDialog;
        private boolean showProgress;


        public Builder(Context context, LoaderTaskListener loaderTaskListener, String url) {
            action = Action.READ;

            this.ctx = context;
            this.loaderTaskListener = loaderTaskListener;
            this.url = url;
        }


        public Builder(Context context, LoaderTaskListener loaderTaskListener, String url, String... params) {
            action = Action.RESOLVE;
            this.params = new ArrayList<>();

            this.ctx = context;
            this.loaderTaskListener = loaderTaskListener;
            this.url = url;
            Collections.addAll(this.params, params);
        }


        public Builder addParam(String key, String value) {
            action = Action.RESOLVE;
            if (this.params == null) this.params = new ArrayList<>();

            params.add(key);
            params.add(value);
            return this;
        }


        public Builder storeContent(String dirPath, String fileName, String extension) {
            this.action = Action.STORE_FILE;

            this.dirPath = dirPath;
            this.fileName = fileName;
            if (!extension.contains(".")) {
                extension = "." + extension;
            }
            this.extension = extension;
            return this;
        }


        public Builder showDialog(int stringIdTitle, int stringIdMessage) {
            return showDialog(AndBasx.getContext().getString(stringIdTitle), AndBasx.getContext().getString(stringIdMessage));
        }


        public Builder showDialog(String title, String message) {
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            showProgress = false;
            return this;
        }


        public Builder showProgressDialog(int stringIdTitle, int stringIdMessage) {
            return showProgressDialog(AndBasx.getContext().getString(stringIdTitle), AndBasx.getContext().getString(stringIdMessage));
        }


        public Builder showProgressDialog(String title, String message) {
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            showProgress = true;
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
