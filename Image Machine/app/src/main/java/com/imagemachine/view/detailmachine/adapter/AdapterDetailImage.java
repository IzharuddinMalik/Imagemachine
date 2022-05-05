package com.imagemachine.view.detailmachine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imagemachine.R;
import com.imagemachine.view.inputmachine.adapter.AdapterImageInputMachine;

import java.util.ArrayList;

public class AdapterDetailImage extends RecyclerView.Adapter<AdapterDetailImage.DetailImageViewHolder> {

    private Context context;
    private ArrayList<byte[]> arrBitmap;

    public AdapterDetailImage(Context mCtx, ArrayList<byte[]> arrBitmap) {
        this.context = mCtx;
        this.arrBitmap = arrBitmap;
    }

    @Override
    public DetailImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_item_image, parent, false);

        return new DetailImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailImageViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(arrBitmap.get(position), 0, arrBitmap.size());
        holder.imageView.setImageBitmap(bitmap);
    }

    public class DetailImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public DetailImageViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.ivItemImageMachine);
        }
    }

    @Override
    public int getItemCount() {
        return arrBitmap.size();
    }
}
