package cn.sevensencond.petmonitor;

import java.util.ArrayList;
import java.util.List;

import cn.sevensencond.petmonitor.petserver.device;

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
    
    public void update(Device device) {
        ContentValues cv = new ContentValues();
        cv.put("phoneNumber", "");
        db.update("DeviceList", cv, "serialNumber = ?", new String[]{ device.serialNumber });
    }
    
    public void del(Device device) {
        db.delete("DeviceList", "serialNumber = ?", new String[]{ device.serialNumber });
    }
    
    public Device getDevice(String serialNumber) {
        Cursor cursor = db.rawQuery("SELECT * FROM DeviceList WHERE serialNumber = ?", new String[]{ serialNumber });
        if (cursor.getCount() == 1 && cursor.moveToNext()) {
            return cursorToDevice(cursor);
        }
        else {
            return null;
        }
    }
    
    public Device cursorToDevice(Cursor cursor) {
        Log.d("cursor", "cursor pos : " + cursor.getPosition());
        Device device = new Device();
        device.name = cursor.getString(cursor.getColumnIndex("name"));
        device.phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));
        device.serialNumber = cursor.getString(cursor.getColumnIndex("serialNumber"));
        device.ownerName = cursor.getString(cursor.getColumnIndex("ownerName"));
        return device;
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