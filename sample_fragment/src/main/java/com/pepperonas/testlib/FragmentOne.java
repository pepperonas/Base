package com.pepperonas.testlib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class FragmentOne extends Fragment {

    private static final String TAG = "FragmentOne";


    public static Fragment newInstance(int i) {
        Fragment fragment = new FragmentOne();
        Bundle args = new Bundle();
        args.putInt("the_id", i);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView  " + "");
        return inflater.inflate(R.layout.fragment_one, container, false);
    }

}