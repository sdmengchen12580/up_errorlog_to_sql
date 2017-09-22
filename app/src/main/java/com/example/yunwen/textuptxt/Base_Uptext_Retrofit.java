package com.example.yunwen.textuptxt;

import android.content.Context;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yunwen on 2017/9/22.
 */

public class Base_Uptext_Retrofit {


    public static final String baseurl = "http://v4.faqrobot.net/";

    private static Context mcontext;

    private OkHttpClient okHttpClient;

    /**上传图文的api*/
    UpTxtApi upTxtApi;

    /**上传文件*/
    private Retrofit retrofit_uptxt;

    /**getaccesstoken*/
    private Retrofit retrofit_getaccesstoken;

    /**********************************************单例start*******************************************/
    private static Base_Uptext_Retrofit base_retrofit;

    public static Base_Uptext_Retrofit get_retrofit(Context context){

        if (base_retrofit==null){
            base_retrofit = new Base_Uptext_Retrofit(context);
        }
        return base_retrofit;
    }

    private Base_Uptext_Retrofit(Context context){
        mcontext = context;
        /**拦截器*/
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        /**共用一个okhttp*/
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        /**获取accesstoken*/
        retrofit_getaccesstoken = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /**上传txt*/
        retrofit_uptxt=new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

/**********************************************单例end*******************************************/



    /**上传图文的api*/
    public UpTxtApi up_txt(){
        if (upTxtApi == null){
            upTxtApi = retrofit_uptxt.create(UpTxtApi.class);
        }
        return upTxtApi;
    }

    /**拿到getaccesstoken的api*/
    public UpTxtApi get_accesstoken_api(){
        if (upTxtApi == null){
            upTxtApi = retrofit_getaccesstoken.create(UpTxtApi.class);
        }
        return upTxtApi;
    }
}
