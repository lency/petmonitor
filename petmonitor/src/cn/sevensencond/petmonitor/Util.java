package cn.sevensencond.petmonitor;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class Util {
    public static final String USER_STICKER_PATH;

    static {
        USER_STICKER_PATH = Environment.getExternalStorageDirectory()
                + "/usersticker/";
    }

    public static byte[] readStream(InputStream paramInputStream) throws Exception {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayOfByte = new byte[1024];
        while (true) {
            int i = paramInputStream.read(arrayOfByte);
            if (i == -1) {
                localByteArrayOutputStream.close();
                paramInputStream.close();
                return localByteArrayOutputStream.toByteArray();
            }
            localByteArrayOutputStream.write(arrayOfByte, 0, i);
        }
    }

    public static byte[] getImage(String paramString) throws Exception {
        HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                paramString).openConnection();
        localHttpURLConnection.setConnectTimeout(60000);
        localHttpURLConnection.setRequestMethod("GET");
        InputStream localInputStream = localHttpURLConnection.getInputStream();
        if (localHttpURLConnection.getResponseCode() == 200)
            return readStream(localInputStream);
        return null;
    }

    public static void saveUserSticker(Bitmap paramBitmap) throws IOException {
        File localFile = new File(USER_STICKER_PATH);
        if (!localFile.exists()) {
            localFile.mkdir();
        }
        BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(
                new FileOutputStream(
                        new File(USER_STICKER_PATH + "userpic.png")));
        paramBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                localBufferedOutputStream);
        localBufferedOutputStream.flush();
        localBufferedOutputStream.close();
    }
    
    public static byte[] getUserStickerFromFile()
    {
      if (!new File(USER_STICKER_PATH + "userpic.png").exists())
          return null;
      Bitmap localBitmap;
      localBitmap = BitmapFactory.decodeFile(USER_STICKER_PATH + "userpic.png");
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      localBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
      return localByteArrayOutputStream.toByteArray();
    }
    
    public static Bitmap toRoundCorner(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    
    public static boolean checkNetwork(Context paramContext) {
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext
                .getSystemService("connectivity")).getActiveNetworkInfo();
        if (localNetworkInfo == null
                || localNetworkInfo.getState() != NetworkInfo.State.CONNECTED)
            return false;
        return true;
    }
    
    public static void sendMessage(Handler paramHandler, int paramInt,
            Bundle paramBundle) {
        Message localMessage = new Message();
        localMessage.what = paramInt;
        if (paramBundle != null)
            localMessage.setData(paramBundle);
        paramHandler.sendMessage(localMessage);
    }
}
