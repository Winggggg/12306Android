package wing.com.a12306.http;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 当前okhttp 不支持读写超时长时间
 * @author weiye
 * @date 2018/9/14
 */
public class OkHttpUtil {

    private static final String TAG=OkHttpUtil.class.getSimpleName();

    private static OkHttpClient okHttpClient;
    private static final int CONNECT_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 5;
    private static final int WRITE_TIMEOUT = 5;

    static {
        if (okHttpClient==null){
//            HttpLoggingInterceptorM loggingInterceptor=new HttpLoggingInterceptorM(new LogInterceptor());
//            loggingInterceptor.setLevel(HttpLoggingInterceptorM.Level.HEADERS);
            okHttpClient=new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
    }

    public static OkHttpClient getInstance(){
        return okHttpClient;
    }


    /**
     * 获取图片字节流 需要在子线程执行
     * @param url
     * @return
     */
    public byte[] requestRawData(String url){
        Request request=new Request.Builder()
                .url(url)
                .build();

        byte[] data=null;
        try {
            //改成同步阻塞返回，需要在子线程执行
            Response response=okHttpClient.newCall(request).execute();
            if (response.code()==200){
                data=response.body().bytes();
            }else{
                Log.e(TAG,"requestRawData code="+response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"requestRawData encounter  error:"+e.getMessage());
        }

        return data;
    }







}
