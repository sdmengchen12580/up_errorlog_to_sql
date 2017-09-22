package com.example.yunwen.textuptxt;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by yunwen on 2017/9/22.
 */

public interface UpTxtApi {


    /**上传txt文件*/
    @Multipart
    @POST("material/jQueryFileUpload")
    Call<UpTxtBean> upPicFile(@Part MultipartBody.Part avatar,
                              @Query("type") int type,
                              @Query("groupId") int number,
                              @Query("access_token") String access_token);

    /**
     * 获取token
     */
    @Headers("Cache-Control:public,max-age=43200")
    @POST("token/getToken")
    Call<Access_Token> getAccess_Token(@Query("appId") String appId,
                                       @Query("secret") String secret);
}
