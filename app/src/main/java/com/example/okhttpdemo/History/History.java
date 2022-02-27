package com.example.okhttpdemo.History;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.okhttpdemo.Loved.LovedImages;
import com.example.okhttpdemo.MainActivity;
import com.example.okhttpdemo.R;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class History extends AppCompatActivity {
    private ListView mListView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private static final String TAG = "History";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mListView = (ListView) findViewById(R.id.historyList);

        ViewGroup.LayoutParams layoutParams = mNavigationView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
        mNavigationView.setLayoutParams(layoutParams);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        }

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepage:
                        Log.i(TAG, "onNavigationItemSelected: homepage selected");
                        Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.myLove:
                        Log.i(TAG, "onNavigationItemSelected: myLove page selected");
                        File file = new File("/data/data/com.example.okhttpdemo/databases/setu.db");
                        if (file.exists()) {
                            Intent lovedIntent = new Intent(getApplicationContext(), LovedImages.class);
                            startActivity(lovedIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "你还没有Loved!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.history:
                        Log.i(TAG, "onNavigationItemSelected: history page selected");
                        Intent historyIntent = new Intent(getApplicationContext(), History.class);
                        startActivity(historyIntent);
                        break;
                }
                return false;
            }
        });

        HistoryAdapter historyAdapter = new HistoryAdapter(getApplicationContext(), R.layout.historyitem, MainActivity.mImageList);
        mListView.setAdapter(historyAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = MainActivity.mImageList.get(position);
                Intent imageIntent = new Intent();
                imageIntent.setData(Uri.parse("https://www.pixiv.net/artworks/" + pid));
                imageIntent.setAction(Intent.ACTION_VIEW);
                startActivity(imageIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG, "onOptionsItemSelected: home");
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
