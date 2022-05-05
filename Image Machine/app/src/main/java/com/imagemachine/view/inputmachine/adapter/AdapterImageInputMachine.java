package com.imagemachine.view.inputmachine.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imagemachine.R;

import java.util.List;

public class AdapterImageInputMachine extends RecyclerView.Adapter<AdapterImageInputMachine.InputMachineViewHolder> {

    private Context context;
    private List<Uri> uriList;

    public AdapterImageInputMachine(Context ctx, List<Uri> uriList) {
        this.context = ctx;
        this.uriList = uriList;
    }

    @NonNull
    @Override
    public InputMachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_item_image, parent, false);

        return new InputMachineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InputMachineViewHolder holder, int position) {
        holder.imageView.setImageURI(uriList.get(position));
    }

    public class InputMachineViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public InputMachineViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.ivItemImageMachine);
        }
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }
}
