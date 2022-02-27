package com.example.okhttpdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SetuView extends AppCompatActivity {
    private ImageView mImageView;
    private String Pid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        mImageView = (ImageView) findViewById(R.id.setuImage);

        String fileName = getIntent().getStringExtra("FileName");
        Pid = getIntent().getStringExtra("pid");

        File mImgPath = new File(MainActivity.mImgPath, "Pictures");
        File file = new File(mImgPath, fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            File nsfw = new File(MainActivity.mImgPath, "NSFW");
            File newFile = new File(nsfw, fileName);
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(newFile);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                mImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }

        registerForContextMenu(mImageView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.setuviewmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pixiv:
                Intent imageIntent = new Intent();
                imageIntent.setData(Uri.parse("https://www.pixiv.net/artworks/" + Pid));
                imageIntent.setAction(Intent.ACTION_VIEW);
                this.startActivity(imageIntent);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
