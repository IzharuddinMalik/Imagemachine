package com.imagemachine.view.detailmachine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.badge.BadgeUtils;
import com.imagemachine.R;
import com.imagemachine.view.dashboard.DashboardActivity;
import com.imagemachine.view.detailmachine.adapter.AdapterDetailImage;
import com.imagemachine.view.inputmachine.InputMachineActivity;
import com.imagemachine.viewmodel.MachineViewModel;

public class DetailMachineActivity extends AppCompatActivity {

    private MachineViewModel machineVM;
    private String machineID, machineName, machineType, machineLastMaintenance, machineQRNumber, strFrom;
    private ImageView ivBack;
    private TextView tvMachineName, tvMachineType, tvMachineLastMaintenance, tvMachineQR;
    private RecyclerView rvListImage;
    private Button btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_machine);

        machineVM = new MachineViewModel(this);

        ivBack = findViewById(R.id.ivBackDetailMachine);
        tvMachineName = findViewById(R.id.tvDetailMachineName);
        tvMachineType = findViewById(R.id.tvDetailMachineType);
        tvMachineLastMaintenance = findViewById(R.id.tvDetailMachineLastMaintenanceDate);
        tvMachineQR = findViewById(R.id.tvDetailMachineNumber);
        rvListImage = findViewById(R.id.rvListDetailImageMachine);
        btnEdit = findViewById(R.id.btnEditMachine);
        btnDelete = findViewById(R.id.btnDeleteMachine);

        strFrom = getIntent().getStringExtra("from");

        if (strFrom.equals("qr")) {
            machineQRNumber = getIntent().getStringExtra("machineqr");
            machineVM.getDetailMachine(machineQRNumber);
        } else {
            machineID = getIntent().getStringExtra("machineid");
            machineName = getIntent().getStringExtra("machinename");
            machineType = getIntent().getStringExtra("machinetype");
            machineLastMaintenance = getIntent().getStringExtra("machinedate");
            machineQRNumber = getIntent().getStringExtra("machineqr");

            tvMachineName.setText(machineName);
            tvMachineType.setText(machineType);
            tvMachineLastMaintenance.setText(machineLastMaintenance);
            tvMachineQR.setText(machineQRNumber);

            machineVM.getDetailImageMachine(machineID);
        }

        initLiveData();

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, InputMachineActivity.class);
            intent.putExtra("from", "edit");
            intent.putExtra("machineid", machineID);
            intent.putExtra("machinename", machineName);
            intent.putExtra("machinetype", machineType);
            intent.putExtra("machinedate", machineLastMaintenance);
            intent.putExtra("machineqr", machineQRNumber);
            startActivity(intent);
        });

        ivBack.setOnClickListener(v -> {
            this.onBackPressed();
        });

        btnDelete.setOnClickListener(v -> {
            machineVM.deleteMachine(machineID);

            initDelete();
        });
    }

    public void initLiveData() {
        machineVM.getArrBitmap().observe(this, bitmaps -> {
            AdapterDetailImage adapterDetailImage = new AdapterDetailImage(this, bitmaps);
            rvListImage.setLayoutManager(new GridLayoutManager(this, 2));
            rvListImage.setAdapter(adapterDetailImage);
            adapterDetailImage.notifyDataSetChanged();
        });

        if (strFrom.equals("qr")) {
            machineVM.getGetDetailMachine().observe(this, machineModel -> {
                Log.i("MACHINEMODEL", " === " + machineModel.getIdMachine());
                machineVM.getDetailImageMachine(machineModel.getIdMachine());
                tvMachineName.setText(machineModel.getNameMachine());
                tvMachineType.setText(machineModel.getTypeMachine());
                tvMachineLastMaintenance.setText(machineModel.getLastMaintMachine());
                tvMachineQR.setText(machineModel.getQrCodeMachine());
            });
        }
    }

    public void initDelete(){
        machineVM.getIsDelete().observe(this, aBoolean ->  {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}