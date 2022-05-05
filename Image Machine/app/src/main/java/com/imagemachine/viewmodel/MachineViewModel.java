package com.imagemachine.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.imagemachine.database.DBHelper;
import com.imagemachine.view.dashboard.ui.home.model.MachineModel;

import java.util.ArrayList;
import java.util.List;

public class MachineViewModel {

    private DBHelper dbHelper;
    private Context context;
    private static MutableLiveData<Boolean> isInsert = new MutableLiveData<>();
    private static MutableLiveData<String> message = new MutableLiveData<>();
    private static MutableLiveData<ArrayList<MachineModel>> listMachine = new MutableLiveData();
    private static MutableLiveData<ArrayList<byte[]>> arrBitmap = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isEdit = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isDelete = new MutableLiveData<>();
    private static MutableLiveData<MachineModel> getDetailMachine = new MutableLiveData<>();

    public MachineViewModel(Context context) {
        this.context = context;
    }

    public void insertMachine(String machineID, String machineName, String machineType, String machineQRCode, String machineLastMaint, ArrayList<byte[]> arrImage) {
        dbHelper = new DBHelper(context);
        dbHelper.insertMachine(machineID, machineName, machineType, machineQRCode, machineLastMaint, arrImage);
        isInsert.setValue(true);
        message.setValue("Inserted Data Successfully!");
    }

    public void getAllMachine() {
        dbHelper = new DBHelper(context);
        listMachine.setValue(dbHelper.getAllMachine());
    }

    public void getDetailImageMachine(String machineID) {
        dbHelper = new DBHelper(context);
        arrBitmap.setValue(dbHelper.getDetailImageMachine(machineID));
    }

    public void editMachine(String machineID, String machineName, String machineType, String machineQRCode, String machineLastMaint) {
        dbHelper = new DBHelper(context);
        dbHelper.editImage(machineID, machineName, machineType, machineQRCode, machineLastMaint);
        isEdit.setValue(true);
        message.setValue("Edit Data Successfully!");
    }

    public void deleteMachine(String machineID) {
        dbHelper = new DBHelper(context);
        dbHelper.deleteMachine(machineID);
        isDelete.setValue(true);
        message.setValue("Delete Data Successfully!");
    }

    public void getDetailMachine(String machineQR) {
        dbHelper = new DBHelper(context);
        getDetailMachine.setValue(dbHelper.getDetailMachineQR(machineQR));
    }

    public MutableLiveData<Boolean> getIsInsert() {
        return isInsert;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<ArrayList<MachineModel>> getListMachine() {
        return listMachine;
    }

    public MutableLiveData<ArrayList<byte[]>> getArrBitmap() {
        return arrBitmap;
    }

    public MutableLiveData<Boolean> getIsEdit() {
        return isEdit;
    }

    public MutableLiveData<Boolean> getIsDelete() {
        return isDelete;
    }

    public MutableLiveData<MachineModel> getGetDetailMachine() {
        return getDetailMachine;
    }
}
