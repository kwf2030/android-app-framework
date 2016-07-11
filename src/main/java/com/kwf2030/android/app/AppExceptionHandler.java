package com.kwf2030.android.app;

import android.os.Environment;
import com.tiexing.airticket.business.TxApp;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

final class AppExceptionHandler implements Thread.UncaughtExceptionHandler {
  static AppExceptionHandler create() {
    return new AppExceptionHandler();
  }

  //用来存储设备信息和异常信息
  private Map<String, String> mInfo = new HashMap<>();

  //用于格式化日期,作为日志文件名的一部分
  private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

  private AppExceptionHandler() {
  }

  @Override
  public void uncaughtException(Thread thread, Throwable throwable) {
    throwable.printStackTrace();
    handleException(throwable);
  }

  /**
   * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成
   *
   * @param throwable throwable
   * @return true:如果处理了该异常信息;否则返回false
   */
  private boolean handleException(Throwable throwable) {
    if (throwable == null) {
      return false;
    }
    //收集设备参数信息
    collectDeviceInfo();
    //保存日志文件
    saveCrashInfo2File(throwable);
    return true;
  }

  /**
   * 收集设备参数信息
   */
  public void collectDeviceInfo() {
    mInfo.put("versionName", App.getVersionName());
    mInfo.put("versionCode", App.getVersionCode() + "");
  }

  /**
   * 保存错误信息到文件中
   *
   * @param throwable throwable
   */
  private void saveCrashInfo2File(final Throwable throwable) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mInfo.entrySet()) {
          String key = entry.getKey();
          String value = entry.getValue();
          sb.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
          cause.printStackTrace(printWriter);
          cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
          String time = mFormatter.format(new Date());
          String fileName = "crash-" + time + ".log";
          if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = TxApp.TMP_DIR + "crash/";
            File dir = new File(path);
            if (!dir.exists()) {
              if (!dir.mkdirs()) {
                return;
              }
            }
            FileOutputStream fos = new FileOutputStream(path + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
          }
        } catch (Exception ignore) {
        }
      }
    }).start();
  }
}
