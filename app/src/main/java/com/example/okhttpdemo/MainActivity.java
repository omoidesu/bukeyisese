package com.example.okhttpdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.okhttpdemo.History.History;
import com.example.okhttpdemo.Loved.LovedImages;
import com.example.okhttpdemo.Sql.DaoMaster;
import com.example.okhttpdemo.Sql.DaoSession;
import com.example.okhttpdemo.Sql.SetuItem;
import com.example.okhttpdemo.Sql.SetuItemDao;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final OkHttpClient mClient = new OkHttpClient();
    private ImageView mGetImageView;
    private Button mGetSetuBtn;
    private CheckBox mR18;
    private Integer R18 = 0;
    private EditText mEditText;
    private String[] tags = null;
    private ProgressBar mProgressBar;
    private MainActivity mainActivity = this;
    private Image mImage;
    private FloatingActionButton mfab;
    public static DaoSession mSession;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    public static File mImgPath;
    private NavigationView mNavigationView;
    public static ArrayList<String> mImageList = new ArrayList<String>();
    private final String SETUAPI = "https://api.lolicon.app/setu/v2?";
    public static File cachePath = new File("/data/data/com.example.okhttpdemo/cache");
    public static File savedPath = new File("/sdcard/Android/data/com.example.okhttpdemo/files");
    private boolean debugMode = false;
    private EditText mEditTextKeyword;
    private EditText mEditTextUid;
    private LinearLayout mInvisibleLinearLayout;
    private TextView mImageInfo;
    private View mLine;
    private String imageInfo;
    private ChipGroup mChipGroup;
    private ConstraintLayout mBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogToFile.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGetImageView = (ImageView) findViewById(R.id.getImage);
        mGetSetuBtn = (Button) findViewById(R.id.getImagebtn);
        mR18 = (CheckBox) findViewById(R.id.checkBox);
        mEditText = (EditText) findViewById(R.id.tagText);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mImgPath = this.getExternalFilesDir(null);
        mEditTextKeyword = (EditText) findViewById(R.id.keyword);
        mEditTextUid = (EditText) findViewById(R.id.uid);
        mInvisibleLinearLayout = (LinearLayout) findViewById(R.id.invisibleEditText);
        mImageInfo = (TextView) findViewById(R.id.imageInfo);
        mLine = (View) findViewById(R.id.line);
        mChipGroup = (ChipGroup) findViewById(R.id.chipGroup);
        mBackground = (ConstraintLayout) findViewById(R.id.background);

        ViewGroup.LayoutParams layoutParams = mNavigationView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
        mNavigationView.setLayoutParams(layoutParams);

        mImageList = load();
        if (mImageList == null) {
            mImageList = new ArrayList<String>();
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        }

//        SimpleTarget<Drawable> drawableSimpleTarget = new SimpleTarget<Drawable>(){
//
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                mBackground.setBackground(resource);
//            }
//        };
//        Glide.with(MainActivity.this).load(R.drawable.homeback).fitCenter().into(drawableSimpleTarget);



        mGetSetuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });
        mGetSetuBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mInvisibleLinearLayout.getVisibility() == View.GONE) {
                    mInvisibleLinearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, R.string.longClickToast, Toast.LENGTH_SHORT).show();
                } else {
                    mInvisibleLinearLayout.setVisibility(View.GONE);
                }
                return true;
            }
        });

        mR18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    R18 = 1;
                    Log.i(TAG, "onCheckedChanged: R18 mode ON");
                } else {
                    R18 = 0;
                    Log.i(TAG, "onCheckedChanged: R18 mode OFF");
                }
            }
        });

        registerForContextMenu(mGetImageView);

        setupDatabase();

        mfab = (FloatingActionButton) findViewById(R.id.loved);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: floating action button clicked");
                if (mImage != null && mImage.getmBitmap() != null) {
                    String pid = mImage.getPid();
                    String uid = mImage.getUid();
                    String title = mImage.getTitle();
                    String author = mImage.getAuthor();
                    String ext = mImage.getExt();
                    String imgFileName = pid + "." + ext;

                    try {

                        mImage.save(MainActivity.this, mainActivity);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    SetuItem setuItem = new SetuItem(pid, uid, title, author, imgFileName);
                    try {
                        List<SetuItem> list = mSession.getSetuItemDao().queryBuilder().where(SetuItemDao.Properties.MPid.eq(pid)).list();
                        if (list.size() == 0) {
                            mSession.getSetuItemDao().insert(setuItem);
                            Toast.makeText(MainActivity.this, R.string.LovedSuccess, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.LovedDone, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "onClick: 数据插入失败");
                    }
                    mfab.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }
        });

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
                            Toast.makeText(getApplicationContext(), R.string.LovedNone, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.history:
                        Log.i(TAG, "onNavigationItemSelected: history page selected");
                        Intent historyIntent = new Intent(getApplicationContext(), History.class);
                        startActivity(historyIntent);
                        break;
                }
                return true;
            }
        });
    }


    private void setupDatabase() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "setu.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);
        mSession = daoMaster.newSession();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (mImage != null) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int writeExternalStorage = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (writeExternalStorage != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                            try {
                                mImage.save(MainActivity.this, mainActivity);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    Toast saveSuccess = Toast.makeText(this, R.string.SaveSuccess, Toast.LENGTH_SHORT);
                    saveSuccess.show();
                }
                break;
            case R.id.refresh:
                if (mImage != null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mImage.getImage();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    mGetImageView.setImageBitmap(mImage.getmBitmap());
                                    Glide.with(MainActivity.this).load(mImage.getmBitmap()).into(mGetImageView);
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }

                    }).start();
                }
                break;
            case R.id.image:
                if (mImage != null) {
                    Intent imageIntent = new Intent();
                    imageIntent.setData(Uri.parse("https://www.pixiv.net/artworks/" + mImage.getPid()));
                    imageIntent.setAction(Intent.ACTION_VIEW);
                    this.startActivity(imageIntent);
                }
                break;
            case R.id.author:
                if (mImage != null) {
                    Intent authorIntent = new Intent();
                    authorIntent.setData(Uri.parse("https://www.pixiv.net/users/" + mImage.getUid()));
                    authorIntent.setAction(Intent.ACTION_VIEW);
                    this.startActivity(authorIntent);
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearCache:
                AlertDialog.Builder clearCacheDialog = new AlertDialog.Builder(MainActivity.this);
                clearCacheDialog.setTitle("确定清空缓存吗?");
                clearCacheDialog.setCancelable(true);
                clearCacheDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("DELETE", "run: delete cache start");
                                File[] files = cachePath.listFiles();
                                for (File file : files
                                ) {
                                    if (file.isFile()) {
                                        boolean deleted = file.delete();
                                        if (deleted) {
                                            LogToFile.i("DELETE", "run: " + file.getName() + " delete success");
                                        } else {
                                            LogToFile.e("DELETE", "run: " + file.getName() + " delete filed");
                                        }
                                    }
                                }
                            }
                        }).start();
                    }
                });
                clearCacheDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                clearCacheDialog.show();
                break;
            case R.id.checkDB:
                checkDB();
                Intent intent = new Intent(getApplicationContext(), LovedImages.class);
                startActivity(intent);
                break;
            case R.id.DelDB:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.dialogTitle);
                dialog.setCancelable(true);
                dialog.setPositiveButton(R.string.dialogPositive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllDb();
                        Toast.makeText(mainActivity, R.string.dialogClearLoved, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton(R.string.dialogNegative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mainActivity, R.string.dialogCancel, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void get() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
                mInvisibleLinearLayout.setVisibility(View.GONE);
            }
        });
        if (mImageList == null) {
            mImageList = new ArrayList<String>();
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder();

                String setuUrl = SETUAPI + "r18=" + R18;

                Editable tagText = mEditText.getText();
                if (tagText.length() != 0) {
                    tags = tagText.toString().split("，");
                    for (String tag : tags
                    ) {
                        setuUrl = setuUrl + "&tag=" + tag;
                    }
                }

                Editable uidText = mEditTextUid.getText();
                if (uidText.length() != 0) {
                    setuUrl = setuUrl + "&uid=" + uidText.toString();
                }

                Editable keywordText = mEditTextKeyword.getText();
                if (keywordText.length() != 0) {
                    setuUrl = setuUrl + "&keyword=" + keywordText.toString();
                }

                Log.i(TAG, "run: url:" + setuUrl);
                builder.url(setuUrl);

                Request request = builder.build();

                Log.d(TAG, "run: " + request);
                Call call = mClient.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        Log.i(TAG, "run: response success!");
                        final String string = response.body().string();
                        final JSONObject getJson = JSONObject.parseObject(string);
                        List<Object> list = JSON.parseArray(getJson.getJSONArray("data").toJSONString());
                        if (list.isEmpty()) {
                            Log.w(TAG, "run: tag error");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(mainActivity, getString(R.string.TAG_ERROR), Toast.LENGTH_SHORT);
                                    toast.show();
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            });
                            return;
                        }
                        final JSONObject imgInfo = JSONObject.parseObject(list.get(0).toString());

                        Map<String, Object> innerMap = imgInfo.getInnerMap();
                        Map<String, String> urls = (Map<String, String>) innerMap.get("urls");

                        String imgUrl = urls.get("original").toString();

                        String pid = innerMap.get("pid").toString();
                        String uid = innerMap.get("uid").toString();
                        String author = innerMap.get("author").toString();
                        String title = innerMap.get("title").toString();
                        String ext = innerMap.get("ext").toString();
                        JSONArray imageTags = (JSONArray) innerMap.get("tags");
                        String imgInfoString = title + "\tid: " + pid + "\t画师: " + author;

                        mImage = new Image(pid, uid, author, title, imgUrl, ext, R18);

                        String imgInfoText = "img pid: " + pid + "\ttitle: " + title + "\nuid: " + uid + "\tauthor: " + author + "\nurl: " + imgUrl;

                        Log.i(TAG, "run: " + imgInfoText);

                        mImage.getImage();
                        Bitmap bitmap = mImage.getmBitmap();
                        imageInfo = "标题：\t\t" + title + "    (" + pid + ")\n作者：\t\t" + author + "\n图片尺寸：\t" + bitmap.getWidth() + "×" + bitmap.getHeight();

                        LogToFile.i(TAG, "run: get image success");
                        mImageList.add(pid);
                        RequestOptions backgroundOptions = MyApplication.getBackgroundOptions();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.GONE);
                                Glide.with(MainActivity.this).load(bitmap).fitCenter().into(mGetImageView);
                                LogToFile.i(TAG, "run: Image set");
                                mfab.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                mLine.setVisibility(View.VISIBLE);
                                mImageInfo.setText(imageInfo);
                                mImageInfo.setVisibility(View.VISIBLE);
                                mChipGroup.removeAllViews();
                                for (Object imageTag : imageTags
                                ) {
                                    String tagInfo = imageTag.toString();
                                    Chip chip = new Chip(MainActivity.this);
                                    chip.setText("#" + tagInfo);
                                    chip.setChipStartPadding(5);
                                    chip.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Editable gotText = mEditText.getText();
                                            if (gotText == tagText || gotText.length() == 0){
                                                mEditText.setText(tagInfo);
                                            } else {
                                                mEditText.setText(gotText.toString() + "，" + tagInfo);
                                            }
                                        }
                                    });
                                    mChipGroup.addView(chip);
                                }
                                SimpleTarget<Drawable> drawableSimpleTarget = new SimpleTarget<Drawable>(){

                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        mBackground.setBackground(resource);
                                    }
                                };
                                Glide.with(MainActivity.this).load(bitmap).apply(backgroundOptions.bitmapTransform(new BlurTransformation(10,8))).into(drawableSimpleTarget);
                            }
                        });


                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
                                if (ext == "jpg") {
                                    format = Bitmap.CompressFormat.JPEG;
                                }


                                File file = new File(cachePath, pid + "." + ext);
                                try {
                                    if (!file.exists()) {
                                        file.createNewFile();
                                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                                        bitmap.compress(format, 100, fileOutputStream);
                                        fileOutputStream.flush();
                                        fileOutputStream.close();
                                        LogToFile.i(TAG, "run: cache write success");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    LogToFile.e(TAG, "run: cache write filed");
                                }

                                if (mImageList.size() > 100) {
                                    String imageTitle = mImageList.remove(0);
                                    File delFile = new File(cachePath, imageTitle + ".png");
                                    if (delFile.exists()) {
                                        LogToFile.i(TAG, "run: 缓存删除成功");
                                        delFile.delete();
                                    } else {
                                        LogToFile.e(TAG, "run: 文件不存在 " + delFile.getName());
                                        File delFile1 = new File(cachePath, imageTitle + ".jpg");
                                        if (delFile1.exists()) {
                                            LogToFile.i(TAG, "run: 缓存删除成功");
                                            delFile.delete();
                                        } else {
                                            LogToFile.e(TAG, "run: 删除失败, 文件不存在 " + delFile1.getName());
                                        }
                                    }
                                }
                            }
                        });
                        thread.start();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "run: response failed!" + e.toString());
                    e.printStackTrace();
                }
            }
        });
        executor.shutdown();
    }

    private void checkDB() {
        try {
            List<SetuItem> setuList = mSession.getSetuItemDao().loadAll();
            for (SetuItem setu : setuList) {
                Log.i(TAG, "checkDB: pid: " + setu.getMPid() + " Title: " + setu.getMTitle());
            }
        } catch (Exception e) {
            Log.e(TAG, "checkDB: error");
        }
    }

    public static void deleteAllDb() {
        try {
            mSession.getSetuItemDao().deleteAll();
            Log.i(TAG, "deleteAllDb: 删除成功");
        } catch (Exception e) {
            Log.e(TAG, "deleteAllDb: 删除失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save(mImageList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        save(mImageList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImageList = load();
    }

    @Override
    protected void onStop() {
        super.onStop();
        save(mImageList);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mImageList = load();
    }

    public void save(ArrayList imageList) {
        JSONArray history = JSONArray.parseArray(JSON.toJSONString(imageList));
        String saveHistory = history.toString();

        BufferedWriter bufferedWriter = null;

        try {
            FileOutputStream fileOutputStream = openFileOutput("history.json", MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(saveHistory);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList load() {
        FileInputStream in = null;
        BufferedReader reader = null;

        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("history.json");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogToFile.e(TAG, "load: history.json not found");
        } catch (IOException e) {
            e.printStackTrace();
            LogToFile.e(TAG, "load: " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String historyString = content.toString();
        LogToFile.i(TAG, "load: \n" + historyString);
        ArrayList<String> history = (ArrayList<String>) JSONObject.parseArray(historyString, String.class);
        return history;
    }
}