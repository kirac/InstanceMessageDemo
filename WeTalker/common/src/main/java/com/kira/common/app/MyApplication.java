package com.kira.common.app;

import android.app.Application;
import android.os.SystemClock;

import java.io.File;

/**
 * Created by kira on 2018/4/7/007.
 */

public class MyApplication extends Application {
    private static MyApplication mMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication=this;
    }
    public static File getPortraitTmpFile()
    {
        //得到头像目录的缓存地址
        File portraitDir = new File(getCacheDirFile(), "portrait");
       if (!portraitDir.exists() ||portraitDir==null)
       {
           portraitDir.mkdirs();
       }
       //删除旧的一些缓存文件
        File[] files = portraitDir.listFiles();
       if (files!=null && files.length>0)
       {
           for (File file : files) {
               file.delete();
           }
       }

        File path = new File(portraitDir, SystemClock.uptimeMillis() + ".jpg");
       return path.getAbsoluteFile();
    }

    private static File getCacheDirFile() {
        return mMyApplication.getCacheDir();
    }
}
