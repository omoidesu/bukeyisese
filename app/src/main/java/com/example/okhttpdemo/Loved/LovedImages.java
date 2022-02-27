package com.example.okhttpdemo.Loved;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.okhttpdemo.History.History;
import com.example.okhttpdemo.MainActivity;
import com.example.okhttpdemo.R;
import com.example.okhttpdemo.Sql.SetuItem;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class LovedImages extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    public static Resources mResources;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private static final String TAG = "LovedImages";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loved);

        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navView);

        ViewGroup.LayoutParams layoutParams = mNavigationView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
        mNavigationView.setLayoutParams(layoutParams);

        List<SetuItem> setuList = MainActivity.mSession.getSetuItemDao().loadAll();

        mRecycleView = (RecyclerView) findViewById(R.id.lovedRecycleView);
//        网格布局
//        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mAdapter = new SetuAdapter(setuList);

//        瀑布流布局
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mAdapter);

        mResources = this.getResources();

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
                        Intent lovedIntent = new Intent(getApplicationContext(), LovedImages.class);
                        startActivity(lovedIntent);
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

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
