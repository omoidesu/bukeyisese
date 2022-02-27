package com.example.okhttpdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Image {
    private String pid;
    private String uid;
    private String author;
    private String title;
    private String imgUrl;
    private String ext;
    private String imgUrlRe;
    private Bitmap mBitmap;
    private boolean NSFW;
    private final OkHttpClient mClient = new OkHttpClient();
    private static final String TAG = "imageClass";

    public Image(String pid, String uid, String author, String title, String imgUrl, String ext, Integer R18) {
        this.pid = pid;
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.imgUrl = imgUrl;
        this.ext = ext;
        if (R18 == 1) {
            this.NSFW = true;
        } else {
            this.NSFW = false;
        }
        String[] split = imgUrl.split("/");
        int p = split[split.length - 1].split("_p")[1].charAt(0);
        Log.i(TAG, "Image: " + p);

        this.imgUrlRe = "https://pixiv.re/" + this.pid + "-" + (p - 47) + "." + ext;
//      https://pixiv.re/95604472-1.jpg
        LogToFile.init(MyApplication.getContext());

    }

    public String getPid() {
        return pid;
    }

    public String getUid() {
        return uid;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getExt() {
        return ext;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void getImage() {

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        LogToFile.i(TAG, "getImage: start at " + ft.format(new Date()));
        Request.Builder builder = new Request.Builder();
        String newUrl = "https://pixiv.re/" + this.pid + "." + ext;
        Request request = builder.url(newUrl).build();
        LogToFile.i(TAG, "getImage: img url: " + imgUrlRe);

        Response response = null;

        try {
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogToFile.i(TAG, "getImage: status code: " + response.code());
        if (response.code() == 404) {
            request = builder.url(imgUrlRe).build();
            try {
                response = mClient.newCall(request).execute();
                LogToFile.i(TAG, "getImage: new url: " + newUrl + "\nstatus code: " + response.code());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Log.i(TAG, "getImage: " + response.headers().toString());

        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        this.mBitmap = bitmap;

        LogToFile.i(TAG, "getImage: end at " + ft.format(new Date()));

    }

    public void save(Context context, MainActivity mainActivity) throws FileNotFoundException {
        Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.PNG;
        if (this.ext == "jpg") {
            mFormat = Bitmap.CompressFormat.JPEG;
        }


        File appDir = new File(mainActivity.getExternalFilesDir(null), "Pictures");

        if (this.NSFW) {
            appDir = new File(mainActivity.getExternalFilesDir(null), "NSFW");
        }

//        File appDir = new File("data/data/com.example.okhttpdemo");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        String fileName = this.pid + "." + this.ext;
        File file = new File(appDir + "/" + fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                this.mBitmap.compress(mFormat, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
    }
}

