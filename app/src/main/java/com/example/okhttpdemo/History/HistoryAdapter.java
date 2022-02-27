package com.example.okhttpdemo.History;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.okhttpdemo.Loved.LovedImages;
import com.example.okhttpdemo.MainActivity;
import com.example.okhttpdemo.MyApplication;
import com.example.okhttpdemo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter<String> {
    private int resourceID;

    public HistoryAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String pid = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.historyImage = (ImageView) view.findViewById(R.id.historyImage);
            viewHolder.historyPid = (TextView) view.findViewById(R.id.historyTitle);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.historyPid.setText(pid);

        File file = new File(MainActivity.cachePath, pid + ".png");
        if (file.exists()) {
            Glide.with(view).load(file.getAbsoluteFile()).apply(MyApplication.getGlideOptions()).into(viewHolder.historyImage);
        } else {
            File file1 = new File(MainActivity.cachePath, pid + ".jpg");
            if (file1.exists()) {
                Glide.with(view).load(file1.getAbsoluteFile()).apply(MyApplication.getGlideOptions()).into(viewHolder.historyImage);
            } else {
                Glide.with(view).load(R.drawable.ic_baseline_image_128).apply(MyApplication.getGlideOptions()).into(viewHolder.historyImage);
            }
        }
        return view;
    }


    class ViewHolder {
        ImageView historyImage;
        TextView historyPid;
    }
}
