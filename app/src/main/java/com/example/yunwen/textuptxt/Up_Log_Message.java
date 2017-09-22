package com.example.yunwen.textuptxt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public  class Up_Log_Message {
    private Context context;

    /**云问的key*/
    private final String yunwen_appid = "v6vJUtwosZoVOgez3D";
    private final String yunwen_secret = "iPh2SDkwxoC76D6A33E8";
    /**
     * 上传错误日志
     */
    private MultipartBody.Part body;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private DateFormat formatter  =  new SimpleDateFormat("yyyy-MM-dd");
    private String access_token;

    public Up_Log_Message(final Context context){
        this.context = context;
        /**先获取accesstokebn*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<Access_Token> call = Base_Uptext_Retrofit.get_retrofit(context).get_accesstoken_api().getAccess_Token(yunwen_appid, yunwen_secret);
                call.enqueue(new Callback<Access_Token>() {
                    @Override
                    public void onResponse(Call<Access_Token> call, Response<Access_Token> response) {
                        if(response.isSuccessful()){
                            Access_Token jsondatas = response.body();
                            access_token = jsondatas.getAccess_token();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String time = formatter.format(new Date());
                                    String fileName = "crash-" + time + ".txt";
                                    String All_Address = getGlobalpath() + fileName;
                                    /**如果文件不存在，就退出*/
                                    if(!new File(All_Address).exists()){
                                        Log.e("____________","文件不存在" );
                                        return;
                                    }
                                    Log.e("____________","文件存在" );
                                    Log.e("——————","准备上传:"+All_Address);
                                    body = prepareFilePart("file",All_Address);
                                    Call<UpTxtBean> call = Base_Uptext_Retrofit.get_retrofit(context).up_txt().upPicFile(body,4,0,access_token);
                                    call.enqueue(new Callback<UpTxtBean>() {
                                        @Override
                                        public void onResponse(Call<UpTxtBean> call, Response<UpTxtBean> response) {
                                            Log.e("——————————","成功" );
                                            if(response.isSuccessful()){
                                                Log.e("——————————","上传成功");
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<UpTxtBean> call, Throwable t) {
                                            Log.e("——————————","失败");
                                            Log.e("——————————","错误为："+t.toString());
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                    @Override
                    public void onFailure(Call<Access_Token> call, Throwable t) {
                        Log.e("attanin_accesstoken_has_error", t.toString());
                    }
                });
            }
        }).start();
    }


    /**获取缓存路径*/
    private String getGlobalpath(){
        Log.e("缓存文件的路径",context.getExternalCacheDir().toString()+"");
        return context.getExternalCacheDir().toString();
    }

    /**上传文件的工具类*/
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        File file = new File(fileUri);
        // 为file建立RequestBody实例
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        // MultipartBody.Part借助文件名完成最终的上传
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
