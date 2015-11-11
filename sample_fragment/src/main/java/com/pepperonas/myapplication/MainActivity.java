package com.pepperonas.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Fragment mFragment;

    private int mSelected = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate  " + "");

        // null check not realy needed - but just in case...
        if (savedInstanceState == null) {

            initUi();

            // get an instance of FragmentTransaction from your Activity
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            /*IMPORTANT: Do the INITIAL(!) transaction only once!
            * If we call this everytime the layout changes orientation,
            * we will end with a messy, half-working UI.
            * */
            mFragment = FragmentOne.newInstance(mSelected = 0);
            fragmentTransaction.add(R.id.frame, mFragment);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged  " +
                   (newConfig.orientation
                    == Configuration.ORIENTATION_LANDSCAPE
                    ? "landscape" : "portrait"));

        initUi();

        Log.i(TAG, "onConfigurationChanged - last selected: " + mSelected);
        makeFragmentTransaction(mSelected);
    }


    /**
     * Called from {@link #onCreate} and {@link #onConfigurationChanged}
     */
    private void initUi() {
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate  instanceState == null / reinitializing..." + "");
        Button btnFragmentOne = (Button) findViewById(R.id.btn_fragment_one);
        Button btnFragmentTwo = (Button) findViewById(R.id.btn_fragment_two);
        btnFragmentOne.setOnClickListener(this);
        btnFragmentTwo.setOnClickListener(this);
    }


    /**
     * Not invoked (just for testing)...
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState  " + "YOU WON'T SEE ME!!!");
    }


    /**
     * Not invoked (just for testing)...
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState  " + "YOU WON'T SEE ME, AS WELL!!!");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume  " + "");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause  " + "");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy  " + "");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_fragment_one:
                Log.d(TAG, "onClick btn_fragment_one " + "");
                makeFragmentTransaction(0);
                break;

            case R.id.btn_fragment_two:
                Log.d(TAG, "onClick btn_fragment_two " + "");
                makeFragmentTransaction(1);
                break;

            default:
                Log.d(TAG, "onClick  null - wtf?!" + "");
        }
    }


    /**
     * We replace the current Fragment with the selected one.
     * Note: It's called from {@link #onConfigurationChanged} as well.
     */
    private void makeFragmentTransaction(int selection) {

        switch (selection) {
            case 0:
                mFragment = FragmentOne.newInstance(mSelected = 0);
                break;
            case 1:
                mFragment = FragmentTwo.newInstance(mSelected = 1);
                break;
        }

        // Create new transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.frame, mFragment);

        /*This would add the Fragment to the backstack...
        * But right now we comment it out.*/
        //        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
