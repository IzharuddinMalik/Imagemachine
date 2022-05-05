package com.imagemachine.view.inputmachine;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.imagemachine.R;
import com.imagemachine.database.DBHelper;
import com.imagemachine.utils.CustomToast;
import com.imagemachine.view.dashboard.DashboardActivity;
import com.imagemachine.view.inputmachine.adapter.AdapterImageInputMachine;
import com.imagemachine.viewmodel.MachineViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class InputMachineActivity extends AppCompatActivity {

    private ArrayList<Uri> uri = new ArrayList();
    private ArrayList<byte[]> arrImage = new ArrayList<>();
    //image pick code
    private int IMAGE_PICK_CODE = 1;

    private int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private RecyclerView rvListImage;

    private Button btnImageMachine, btnInputMachine;
    private TextInputEditText tietMachineName, tietMachineType, tietMachineQrNumber;
    private TextView tvSelectDate;
    private ImageView ivBack;

    private String strMachineID, strMachineName, strMachineType, strMachineQR, strSelectDate, strFrom;

    private CustomToast customToast;
    private Calendar calendar;

    private MachineViewModel machineVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_machine);

        customToast = new CustomToast();
        machineVM = new MachineViewModel(this);

        rvListImage = findViewById(R.id.rvListImageMachine);
        btnImageMachine = findViewById(R.id.btnImageMachine);
        btnInputMachine = findViewById(R.id.btnInputMachine);
        tietMachineName = findViewById(R.id.tietInputNameMachine);
        tietMachineType = findViewById(R.id.tietInputTypeMachine);
        tietMachineQrNumber = findViewById(R.id.tietInputqrnumberMachine);
        tvSelectDate = findViewById(R.id.tvLastDateMaintenaceMachine);
        ivBack = findViewById(R.id.ivBackInputMachine);

        strFrom = getIntent().getStringExtra("from");

        if (strFrom.equals("edit")) {
            strMachineID = getIntent().getStringExtra("machineid");
            strMachineName = getIntent().getStringExtra("machinename");
            strMachineType = getIntent().getStringExtra("machinetype");
            strMachineQR = getIntent().getStringExtra("machineqr");
            strSelectDate = getIntent().getStringExtra("machinedate");

            tietMachineName.setText(strMachineName);
            tietMachineType.setText(strMachineType);
            tietMachineQrNumber.setText(strMachineQR);
            tvSelectDate.setText(strSelectDate);
        }

        btnImageMachine.setOnClickListener(v -> {
            openGalleryImage();
        });

        tvSelectDate.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String formatTanggal = "yyyy-MM-dd";
                String formatTanggal2 = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                SimpleDateFormat sdf2 = new  SimpleDateFormat(formatTanggal2);
                tvSelectDate.setText(sdf2.format(calendar.getTime()));
                strSelectDate = sdf.format(calendar.getTime());
            },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis() + 10000);
            datePickerDialog.show();
        });

        btnInputMachine.setOnClickListener(v -> {
            if (!checkMachineName() || !checkMachineType() || !checkMachineQR()) {

            } else {

                if (strFrom.equals("edit")) {
                    machineVM.editMachine(strMachineID, strMachineName, strMachineType, strMachineQR, strSelectDate);
                } else {
                    String machineID = UUID.randomUUID().toString().substring(0, 7);
                    machineVM.insertMachine(machineID, strMachineName, strMachineType, strMachineQR, strSelectDate, arrImage);
                }

                initLiveData();
            }
        });

        ivBack.setOnClickListener(v -> {
            this.onBackPressed();
        });
    }

    void initLiveData() {
        machineVM.getIsInsert().observe(this, aBoolean -> {
            if (aBoolean) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            }
        });

        machineVM.getMessage().observe(this, s -> {
            customToast.customToast(this, s);
        });

        machineVM.getIsEdit().observe(this, aBoolean ->  {
            if (aBoolean) {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            }
        });
    }

    void openGalleryImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Pictures: "), IMAGE_PICK_CODE);
    }

    public void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count;i++) {
                        uri.add(data.getClipData().getItemAt(i).getUri());
                    }
                    Log.i("URI", " === " + uri);
                    Log.i("SIZECOUNT", " === " + count);
                    AdapterImageInputMachine adapterImageInputMachine = new AdapterImageInputMachine(this, uri);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InputMachineActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rvListImage.setLayoutManager(linearLayoutManager);
                    rvListImage.setAdapter(adapterImageInputMachine);

                    for (int i=0;i < uri.size();i++) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri.get(i));
                            byte[] arrData = getBytes(inputStream);

                            arrImage.add(arrData);
                            Log.i("ARRIMAGE", " === " + arrImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            } else if (data.getData() != null) {
                String imagePath = data.getData().getPath();
                Log.i("IMAGEPATH", " === " + imagePath);
            }
        }
    }

    public boolean checkMachineName() {
        strMachineName = tietMachineName.getText().toString();

        if (strMachineName.isEmpty()) {
            customToast.customToast(this, "Machine name cannot be empty!");
            return false;
        }

        return true;
    }

    public boolean checkMachineType() {
        strMachineType = tietMachineType.getText().toString();

        if (strMachineType.isEmpty()) {
            customToast.customToast(this, "Machine type cannot be empty!");
            return false;
        }

        return true;
    }

    public boolean checkMachineQR() {
        strMachineQR = tietMachineQrNumber.getText().toString();

        if (strMachineQR.isEmpty()) {
            customToast.customToast(this, "Machine QR Number cannot be empty!");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}