package com.xmwsh.videolib.media.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FileUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private static String getFileName() {
        return "shot_"+dateFormat.format(new Date())+".jpg";
    }

    private static String getSDcardDir() {
        String dir = Environment.getExternalStorageDirectory() + File.separator + "xmwshShot" + File.separator;
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return dir;
    }

    public static String saveBitmap(Bitmap mBitmap) {

        try {
            String filePath = getSDcardDir() + getFileName();
            File filePic = new File(filePath);
            if (!filePic.exists()) {
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return filePic.getAbsolutePath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
