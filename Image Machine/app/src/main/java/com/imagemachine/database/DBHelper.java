package com.imagemachine.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.imagemachine.view.dashboard.ui.home.model.MachineModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "db_machine";
    private static final String TB_MACHINE = "tb_machine";
    private static final String ID_MACHINE = "idmachine";
    private static final String MACHINE_NAME = "machinename";
    private static final String MACHINE_TYPE = "machinetype";
    private static final String MACHINE_QRCODE = "machineqrcode";
    private static final String MACHINE_LASTMAINT = "machinelastmaint";

    private static final String TB_IMAGEMACHINE = "tb_imagemachine";
    private static final String ID_IMAGEMACHINE = "idimagemachine";
    private static final String ID_MACHINEIMAGE = "idmachine";
    private static final String IMAGEMACHINE_NAME = "imagemachinename";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MACHINE = "CREATE TABLE " + TB_MACHINE + "(" +
                ID_MACHINE + " TEXT PRIMARY KEY, " +
                MACHINE_NAME + " TEXT, " +
                MACHINE_TYPE + " TEXT, " +
                MACHINE_QRCODE + " TEXT, " +
                MACHINE_LASTMAINT + " DATETIME " + ")";

        String CREATE_TABLE_IMAGEMACHINE = "CREATE TABLE " + TB_IMAGEMACHINE + "(" +
                ID_IMAGEMACHINE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID_MACHINEIMAGE + " INTEGER, " +
                IMAGEMACHINE_NAME + " BLOB " + ")";

        db.execSQL(CREATE_TABLE_MACHINE);
        db.execSQL(CREATE_TABLE_IMAGEMACHINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_IMAGEMACHINE);
        db.execSQL("DROP TABLE IF EXISTS " + TB_MACHINE);
        onCreate(db);
    }

    public void insertMachine(String machineID, String machineName, String machineType, String machineQRCode, String machineLastMaint, ArrayList<byte[]> imageName) {
        SQLiteDatabase database = this.getWritableDatabase();

        String insertMachine = "INSERT INTO " + TB_MACHINE + "(" + ID_MACHINE + "," + MACHINE_NAME + "," + MACHINE_TYPE + "," + MACHINE_QRCODE + "," + MACHINE_LASTMAINT + ")" +
                " VALUES ('" + machineID + "','" + machineName + "','" + machineType + "','" + machineQRCode + "','" + machineLastMaint + "')";

        database.execSQL(insertMachine);

        for (int i=0; i < imageName.size();i++) {
            insertImage(machineID, imageName.get(i));
        }
    }

    public void insertImage(String machineID, byte[] imageName) {
        SQLiteDatabase database = this.getWritableDatabase();

        String insertImageMachine = "INSERT INTO " + TB_IMAGEMACHINE + "(" + ID_MACHINEIMAGE + "," + IMAGEMACHINE_NAME + ")" +
                " VALUES ('" + machineID + "','" + imageName + "')";

        database.execSQL(insertImageMachine);
    }

    public void editImage(String machineID, String machineName, String machineType, String machineQRCode, String machineLastMaint) {
        SQLiteDatabase database = this.getWritableDatabase();

        String editMachine = "UPDATE " + TB_MACHINE + " SET " + MACHINE_NAME + " = '" + machineName + "'," + MACHINE_TYPE + " = '" + machineType + "'," + MACHINE_QRCODE + " = '" + machineQRCode + "'," + MACHINE_LASTMAINT + " = '" + machineLastMaint + "' WHERE " +
                ID_MACHINE + " = '" + machineID + "'";

        database.execSQL(editMachine);
    }

    public ArrayList<MachineModel> getAllMachine() {
        ArrayList<MachineModel> machineList = new ArrayList<>();
        String querySelect = "SELECT * FROM " + TB_MACHINE;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(querySelect, null);

        if (cursor.moveToFirst()) {
            do {
                MachineModel machineModel = new MachineModel();
                machineModel.setIdMachine(cursor.getString(0));
                machineModel.setNameMachine(cursor.getString(1));
                machineModel.setTypeMachine(cursor.getString(2));
                machineModel.setQrCodeMachine(cursor.getString(3));
                machineModel.setLastMaintMachine(cursor.getString(4));

                machineList.add(machineModel);
            } while (cursor.moveToNext());
        }

        return machineList;
    }

    public MachineModel getDetailMachineQR(String machineqr) {
        MachineModel machineModel = new MachineModel();
        String querySelect = "SELECT * FROM " + TB_MACHINE + " WHERE " + MACHINE_QRCODE + " = '" + machineqr + "'";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(querySelect, null);

        if (cursor.moveToFirst()) {
            do {
                machineModel.setIdMachine(cursor.getString(0));
                machineModel.setNameMachine(cursor.getString(1));
                machineModel.setTypeMachine(cursor.getString(2));
                machineModel.setQrCodeMachine(cursor.getString(3));
                machineModel.setLastMaintMachine(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        return machineModel;
    }

    public ArrayList<byte[]> getDetailImageMachine(String machineID) {
        ArrayList<byte[]> arrBitmap = new ArrayList<>();
        String querySelect = "SELECT * FROM " + TB_IMAGEMACHINE + " WHERE idmachine = '" + machineID + "'";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(querySelect, null);

        if (cursor.moveToFirst()){
            do {
                byte[] imgByte = cursor.getBlob(2);
                arrBitmap.add(imgByte);
            } while (cursor.moveToNext());
        }

        return arrBitmap;
    }

    public void deleteMachine(String machineID) {
        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + TB_MACHINE + " WHERE " + ID_MACHINE + " = '" + machineID + "'";
        database.execSQL(deleteQuery);
    }

    public boolean databaseExists(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (dbFile.exists()) {
            return true;
        }
        dbFile.mkdirs(); //<<<<<<<<<< creates the databases folder
        return false;
    }
}
