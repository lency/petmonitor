package cn.sevensencond.petmonitor;

//import android.content.ContentValues;
import android.content.Context;
//import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "petmonitor.db";
    private static final int DATABASE_VERSION = 2;
    
    
//    private static final String TBL_NAME = "DeviceList";
    private static final String CREATE_DEVICE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + " DeviceList(_id integer primary key autoincrement,"
            + " trackerID integer,"
            + " trackerType integer,"
            + " trackerTypeDesc varchar(50),"
            + " user varchar(20),"
            + " name varchar(30),"
            + " phoneNumber varchar(20),"
            + " serialNumber varchar(11),"
            + " controlNumber varchar(20),"
            + " guardian1 varchar(20),"
            + " guardian2 varchar(20),"
            + " guardian3 varchar(20),"
            + " guardian4 varchar(20),"
            + " filePath varchar(260),"
            + " interval integer,"
            + " chargeMode integer,"
            + " chargePeriodType integer,"
            + " serviceStatus integer,"
            + " serviceEndDate varchar(16),"
            + " balance double,"
            + " countPerPackage integer,"
            + " isElectronicFenceOn integer,"
            + " trackerSticker blob,"
            + " trackerStickerVer integer,"
            + " warning integer,"
            + " status long,"
            + " moveDistance integer,"
            + " fenceLatitude double,"
            + " fenceLongitude double,"
            + " bdFenceLatitude double,"
            + " bdFenceLongitude double,"
            + " fenceOn integer,"
            + " fenceRadius double,"
            + " enterFenceAlarm integer,"
            + " mode integer,"
            + " disableStatus integer,"
            + " gpsEnabled integer,"
            + " ownerType integer,"
            + " ownerName varchar(30),"
            + " stickerUrl varchar(250))";

//    private SQLiteDatabase db;

    public DBHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("database", "create database");
        db.execSQL(CREATE_DEVICE_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("database", "onUpgrade: old version:" + oldVersion + " newVersion: " + newVersion);
        db.execSQL("drop table if exists DeviceList");
        onCreate(db);
    }
    
//
//    public void insert(ContentValues values) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.insert(TBL_NAME, null, values);
//        db.close();
//    }
//
//    public Cursor query() {
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
//        return c;
//    }
//
//    public void del(int id) {
//        if (db == null)
//            db = getWritableDatabase();
//        db.delete(TBL_NAME, "_id=?", new String[] { String.valueOf(id) });
//    }
//    
//    public void truncate() {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM "+TBL_NAME);
//    }
//
//    public void close() {
//        if (db != null)
//            db.close();
//    }
}