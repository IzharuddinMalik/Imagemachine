package com.imagemachine.view.dashboard.ui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imagemachine.R;
import com.imagemachine.view.dashboard.ui.home.model.MachineModel;
import com.imagemachine.view.detailmachine.DetailMachineActivity;

import java.util.ArrayList;
import java.util.List;

public class AdapterListMachine extends RecyclerView.Adapter<AdapterListMachine.ListMachineViewHolder> {

    Context context;
    ArrayList<MachineModel> listMachine;

    public AdapterListMachine(Context mctx, ArrayList<MachineModel> listMachine) {
        this.context = mctx;
        this.listMachine = listMachine;
    }

    @Override
    public ListMachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_item_machine, parent, false);

        return new ListMachineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListMachineViewHolder holder, int position) {
        holder.tvMachineName.setText(listMachine.get(position).getNameMachine());
        holder.tvMachineType.setText(listMachine.get(position).getTypeMachine());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailMachineActivity.class);
            intent.putExtra("from", "detail");
            intent.putExtra("machineid", listMachine.get(position).getIdMachine());
            intent.putExtra("machinename", listMachine.get(position).getNameMachine());
            intent.putExtra("machinetype", listMachine.get(position).getTypeMachine());
            intent.putExtra("machinedate", listMachine.get(position).getLastMaintMachine());
            intent.putExtra("machineqr", listMachine.get(position).getQrCodeMachine());
            context.startActivity(intent);
        });
    }

    public class ListMachineViewHolder extends RecyclerView.ViewHolder{
        TextView tvMachineName, tvMachineType;

        public ListMachineViewHolder(View view) {
            super(view);

            tvMachineName = view.findViewById(R.id.tvListNameOfMachine);
            tvMachineType = view.findViewById(R.id.tvListTypeOfMachine);
        }
    }

    @Override
    public int getItemCount() {
        return listMachine.size();
    }
}
