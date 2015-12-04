package com.pepperonas.testlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pepperonas.andbasx.base.ToastUtils;
import com.pepperonas.jbasx.interfaces.ThreadListener;
import com.pepperonas.jbasx.log.Log;
import com.pepperonas.jbasx.network.NetworkAddressUtils;
import com.pepperonas.jbasx.network.NetworkBaseUtils;
import com.pepperonas.jbasx.network.Networker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity implements ThreadListener {

    private static final String TAG = "MainActivity";
    public static final String SOURCE_NAME = "gapps.zip";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        new LoaderTaskUtils.Builder(this, new LoaderTaskListener() {
        //            @Override
        //            public void onLoaderTaskSuccess(LoaderTaskUtils.Action action, String s) {
        //                Log.d(TAG, "onLoaderTaskSuccess  " + s);
        //            }
        //
        //
        //            @Override
        //            public void onLoaderTaskFailed(LoaderTaskUtils.Action action, String s) {
        //                Log.e(TAG, "onLoaderTaskFailed " + s);
        //            }
        //        }, "https://www.google.com").launch();
        //
        //
        //        new LoaderTaskUtils.Builder(this, new LoaderTaskListener() {
        //            @Override
        //            public void onLoaderTaskSuccess(LoaderTaskUtils.Action action, String s) {
        //
        //            }
        //
        //
        //            @Override
        //            public void onLoaderTaskFailed(LoaderTaskUtils.Action action, String s) {
        //
        //            }
        //        }, "http://orig02.deviantart.net/5229/f/2010/011/8/a/sombrero_galaxy_by_bubimandril.jpg")
        //                .storeContent(LoaderTaskUtils.Action.STORE_FILE,
        //                              AndroidStorageUtils.getExternalRootDir(),
        //                              "SombreroGalaxy", ".png")
        //                .showProgressDialog("Downloading", "The Sombrero Galaxy will reach your phone soon.")
        //                .launch();


        //        testConcurrency();
        //        testAndroidStorageUtils();
        //        AndroidStorageUtils.getAppsExternalFileDir()
        //        testBitmapUtils();
        //        testNetworkBaseUtils();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //        SystemUtils.runRootCmd("reboot -p");
    }


    private void testConcurrency() {

        //        ThreadUtils.doItDelayed(3000, new Callable<Void>() {
        //            @Override
        //            public Void call() throws Exception {
        //                showString();
        //                return null;
        //            }
        //        });

        //        ThreadUtils.doInBackground(new Callable<Void>() {
        //            @Override
        //            public Void call() throws Exception {
        //                copyCat();
        //                return null;
        //            }
        //        });
    }


    private void showString() {
        ToastUtils.toastShort("delayed...");
    }


    //    private void testBitmapUtils() {
    //        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_test);
    //        BitmapUtils.storeInAppsDataDir(icon, "bitmap_test", "ic_test");
    //    }


    private void testAndroidStorageUtils() {
        //        Log.d(TAG, "onCreate  " + AndroidStorageUtils.getAppsInternalDataDir());
        //
        //        Log.d(TAG, "onCreate  " + AndroidStorageUtils.getAppsExternalCacheDir());
        //        Log.d(TAG, "onCreate  " + AndroidStorageUtils.getAppsExternalFileDir("dirName"));
        //        Log.d(TAG, "onCreate  " + AndroidStorageUtils.getExternalRootDir());
    }


    private void copyCat() {
        //        File sd = new File(AndroidStorageUtils.getExternalRootDir());
        //        File ccSrc = new File(sd.getPath() + "/Download/" + SOURCE_NAME);
        //        Log.i(TAG, "ccSrcName: " + ccSrc.getName());
        //        FileUtils.copy(ccSrc, sd.getPath() + "/DownloadCopy/", true);
    }


    private void testNetworkBaseUtils() {
        List<String> ips = NetworkBaseUtils.getNetworkAddresses(false);
        for (String ip : ips) {
            Log.d(TAG, "testNetworkBaseUtils (all): " + ip);
        }

        ips = NetworkBaseUtils.getNetworkAddresses(true);
        for (String ip : ips) {
            Log.d(TAG, "testNetworkBaseUtils (IP only): " + ip);
        }

        testHostIps(ips);
    }


    private void testHostIps(List<String> ips) {
        for (String ip : ips) {

            ip = NetworkAddressUtils.replaceBytes(ip, 1);

            Log.d(TAG, "testHostIps: " + ip);

            final ExecutorService exService = Executors.newFixedThreadPool(1);
            final Future<List<String>> task = exService.submit(new Networker(Networker.Mode.IP_LOOKUP, ip, 100));
            try {
                List<String> hostName = task.get();
                for (String s : hostName) {
                    Log.d(TAG, "testHostIps IP: " + s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            exService.shutdownNow();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBaseThreadSuccess(String s) {
        Log.d(TAG, "onBaseThreadSuccess  " + s);
    }


    @Override
    public void onBaseThreadFailed(String s) {
        Log.d(TAG, "onBaseThreadFailed  " + s);
    }

}
