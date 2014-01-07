package cn.sevensencond.petmonitor;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add device
     * 
     * @param device
     */
    public void add(ContentValues values) {
        db.beginTransaction(); // 开始事务
        try {
            db.insert("DeviceList", null, values); 
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * update person's age
     * 
     * @param person
     */
//    public void updateAge(Device person) {
//        ContentValues cv = new ContentValues();
//        cv.put("age", person.age);
//        db.update("person", cv, "name = ?", new String[] { person.name });
//    }

    /**
     * query all devices, return list
     * 
     * @return List<Device>
     */
    public List<Device> query() {
        ArrayList<Device> devices = new ArrayList<Device>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Device device = new Device();
            device.name = c.getString(c.getColumnIndex("name"));
            device.phoneNumber = c.getString(c.getColumnIndex("phoneNumber"));
            device.serialNumber = c.getString(c.getColumnIndex("serialNumber"));
            device.ownerName = c.getString(c.getColumnIndex("ownerName"));
            devices.add(device);
        }
        c.close();
        return devices;
    }

    /**
     * query all persons, return cursor
     * 
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM DeviceList", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}