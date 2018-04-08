package com.kira.common.app;

import android.app.Application;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

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
    /**
     * 获取声音文件的本地地址
     *
     * @param isTmp 是否是缓存文件， True，每次返回的文件地址是一样的
     * @return 录音文件的地址
     */
    public static File getAudioTmpFile(boolean isTmp) {
        File dir = new File(getCacheDirFile(), "audio");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        // aar
        File path = new File(getCacheDirFile(), isTmp ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }
    /**
     * 显示一个Toast
     *
     * @param msg 字符串
     */
    public static void showToast(final String msg) {
        // Toast 只能在主线程中显示，所有需要进行线程转换，
        // 保证一定是在主线程进行的show操作
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                Toast.makeText(mMyApplication, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 显示一个Toast
     *
     * @param msgId 传递的是字符串的资源
     */
    public static void showToast(@StringRes int msgId) {
        showToast(mMyApplication.getString(msgId));
    }
}
