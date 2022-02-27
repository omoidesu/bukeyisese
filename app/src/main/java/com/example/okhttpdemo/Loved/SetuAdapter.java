package com.example.okhttpdemo.Loved;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.okhttpdemo.MainActivity;
import com.example.okhttpdemo.R;
import com.example.okhttpdemo.SetuView;
import com.example.okhttpdemo.Sql.SetuItem;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class SetuAdapter extends RecyclerView.Adapter<SetuAdapter.ViewHolder> {
    private List<SetuItem> mSetuList;

    public SetuAdapter(List<SetuItem> setuList) {
        mSetuList = setuList;
    }

    @NonNull
    @Override
    public SetuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lovedimage, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                SetuItem setuItem = mSetuList.get(position);
                Intent intent = new Intent(view.getContext(), SetuView.class);
                intent.putExtra("FileName", setuItem.getMImgFileName());
                intent.putExtra("pid", setuItem.getMPid());
                view.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SetuAdapter.ViewHolder holder, int position) {
        SetuItem setuItem = mSetuList.get(position);
        holder.setuTitle.setText(setuItem.getMTitle());
//        holder.setuImage.setImageResource(R.drawable.cheems);
        File mImgPath = new File(MainActivity.mImgPath, "Pictures");
        File file = new File(mImgPath, setuItem.getMImgFileName());

        RequestOptions requestOptions = new RequestOptions();
//        requestOptions

        if (file.exists()) {
            Glide.with(holder.setuView).load(file.getAbsoluteFile()).error(R.drawable.ic_baseline_image_128).fitCenter().into(holder.setuImage);
        } else {
            File nsfw = new File(MainActivity.mImgPath, "NSFW");
            File newFile = new File(nsfw, setuItem.getMImgFileName());
            Glide.with(holder.setuView).load(newFile.getAbsoluteFile()).error(R.drawable.ic_baseline_image_128).fitCenter().into(holder.setuImage);
        }
    }

    @Override
    public int getItemCount() {
        return mSetuList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View setuView;
        ImageView setuImage;
        TextView setuTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setuView = itemView;
            setuImage = (ImageView) itemView.findViewById(R.id.lovedImg);
            setuTitle = (TextView) itemView.findViewById(R.id.lovedTitle);
        }
    }

}