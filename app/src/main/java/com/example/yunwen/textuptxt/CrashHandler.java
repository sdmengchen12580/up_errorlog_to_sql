package com.example.yunwen.textuptxt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  <h3>全局捕获异常</h3>
 *  <br>
 *  当程序发生Uncaught异常的时候,有该类来接管程序,并记录错误日志
 */
@SuppressLint("SimpleDateFormat")
public  class CrashHandler implements UncaughtExceptionHandler {

    public  static String TAG  =  "MyCrash";
    //  系统默认的UncaughtException处理类
    private  Thread.UncaughtExceptionHandler  mDefaultHandler;

    private  static  CrashHandler  instance  =  new  CrashHandler();
    private Context mContext;

    //  用来存储设备信息和异常信息
    private Map<String,  String> infos  =  new HashMap<String,  String>();

    //  用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter  =  new SimpleDateFormat("yyyy-MM-dd");

    /**  保证只有一个CrashHandler实例*/
    private CrashHandler()  {
    }

    /**  获取CrashHandler实例  ,单例模式  */
    public  static  CrashHandler  getInstance()  {
        return  instance;
    }

    /**
     *  初始化
     */
    public  void  init(Context context)  {
        mContext  =  context;
        //  获取系统默认的UncaughtException处理器
        mDefaultHandler  =  Thread.getDefaultUncaughtExceptionHandler();
        //  设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     *  当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public  void  uncaughtException(Thread thread, Throwable ex)  {
        if  (!handleException(ex)  &&  mDefaultHandler  !=  null)  {
            //  如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread,ex);
        }  else  {
//            SystemClock.sleep(5000);



            /**上传错误日志*/
//          up_log();
        }
    }

    /**
     *  自定义错误处理,收集错误信息  发送错误报告等操作均在此完成.
     *  @param  ex
     *  @return  true:如果处理了该异常信息;  否则返回false.
     */
    private  boolean  handleException(Throwable ex)  {
        if  (ex  ==  null)
            return  false;
        try  {
            //  使用Toast来显示异常信息
            new  Thread()  {
                @Override
                public  void  run()  {
                    Looper.prepare();
                    Toast.makeText(mContext,  "很抱歉,程序出现异常,即将重启.",
                            Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            //  收集设备参数信息
            collectDeviceInfo(mContext);
            //  保存日志文件
            saveCrashInfoFile(ex);
            SystemClock.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            sb.append("\r\n" + date + "\n");
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);

            String fileName = writeFile(sb.toString());
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }
        return null;
    }

    //将错误日志记录
    private String writeFile(String sb) throws Exception {
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + ".txt";
        if (ExistSDCard()) {
            String path = getGlobalpath();
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            FileOutputStream fos = new FileOutputStream(path + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
        }
        return fileName;
    }

    //是否存在sd卡
    private boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        else
            return false;
    }



    //获取缓存路径
    private String getGlobalpath(){
        Log.e("缓存文件的路径",mContext.getExternalCacheDir().toString()+"");
        return mContext.getExternalCacheDir().toString();
    }



}