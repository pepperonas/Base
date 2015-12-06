package com.pepperonas.sample_preferencefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.pepperonas.sample_preferencefragment.multipref.MultiPrefActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, Menu.NONE, getString(R.string.settings));
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.settings))) {
            startActivity(new Intent(this, MultiPrefActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
