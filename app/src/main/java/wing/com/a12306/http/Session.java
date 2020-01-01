package wing.com.a12306.http;

import android.util.Log;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wing.com.a12306.config.Constants;
import wing.com.a12306.config.UrlConfig;
import wing.com.a12306.config.UrlsEnum;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description  会话类
 * @Version 1.0
 */
public class Session {

    public HttpClient httpClient;
    public String token;
    public String cookie;

    public Session() {
        this.httpClient = new HttpClient();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        if (StrUtil.isNotEmpty(this.cookie)) {
            this.cookie += ";";
        } else {
            this.cookie = "";
        }
        this.cookie += cookie;
    }

    public void setCookie(List<HttpCookie> cookies) {
        cookies.forEach(hc -> {
            if (StrUtil.isNotEmpty(this.cookie)) {
                this.cookie += ";";
            } else {
                this.cookie = "";
            }
            this.cookie += hc.toString();
        });
    }

    public class HttpClient {

        private HttpRequest httpRequest;


        private HttpClient() {
        }

        public HttpResponse send(UrlsEnum urlsEnum) {
            return this.send(urlsEnum, null, false);
        }

        public HttpResponse send(UrlsEnum urlsEnum, HashMap<String, Object> formData) {
            return this.send(urlsEnum, formData, false);
        }

        public HttpResponse sendAsync(UrlsEnum urlsEnum) {
            return this.send(urlsEnum, null, true);
        }

        public HttpResponse sendAsync(UrlsEnum urlsEnum, HashMap<String, Object> formData) {
            return this.send(urlsEnum, formData, true);
        }

        public HttpResponse send(UrlsEnum urlsEnum, HashMap<String, Object> formData, boolean async) {
            UrlConfig urlConfig = urlsEnum.getUrlConfig();
            if (this.httpRequest == null) {
                this.httpRequest = HttpUtil.createGet(urlConfig.getUrl());
                this.httpRequest.header("Host", Constants.HOST);
                this.httpRequest.header("Origin", Constants.ORIGIN);
                this.httpRequest.header("Connection", "keep-alive");
                this.httpRequest.header("Accept", Constants.ACCEPT);
            } else {
                this.httpRequest.setUrl(urlConfig.getUrl());
                this.httpRequest.setMethod(this.getMethod(urlConfig.getMethod()));
            }
            if (!"".equals(urlConfig.getReferer())) {
                this.httpRequest.header("Referer", urlConfig.getReferer());
            }
            // 设置cookie
            if (StrUtil.isNotBlank(cookie)) {
                this.httpRequest.header("Cookie", cookie);
            }
//            this.httpRequest.headers().forEach((k, v) -> System.out.println(k + "=" + v));
            // 设置表单参数
            if (formData != null) {
                this.httpRequest.form(formData);
            }
            if (async) {
                return this.httpRequest.executeAsync();
            } else {
                return this.httpRequest.execute();
            }
        }

        public HttpResponse send2(UrlsEnum urlsEnum, HashMap<String, Object> formData, boolean async) {
//            UrlConfig urlConfig = urlsEnum.getUrlConfig();
//            boolean isPost=urlConfig.getMethod().equals("post")?true:false;
//            FormBody.Builder formBuilder=new FormBody.Builder();
//            RequestBody requestBody=formBuilder.build();
//            if (formData!=null){
//                Set<Map.Entry<String, Object>> entrySet = formData.entrySet();
//                entrySet.forEach(stringObjectEntry -> formBuilder.add(stringObjectEntry.getKey(), (String) stringObjectEntry.getValue()));
//            }
//            Request.Builder builder = new Request.Builder()
//                    .url(urlConfig.getUrl())
//                    .addHeader("Host", Constants.HOST)
//                    .addHeader("Origin", Constants.ORIGIN)
//                    .addHeader("Connection", "keep-alive")
//                    .addHeader("Accept", Constants.ACCEPT);
//            if (isPost){
//               builder.post(requestBody);
//            }
//            // 设置cookie
//            if (StrUtil.isNotBlank(cookie)) {
//                builder.header("Cookie", cookie);
//            }
//            Request request = builder.build();
//            Response response = null;
//            try {
//                response = OkHttpUtil.getInstance().newCall(request).execute();
//                if (response==null){
//                    return new HttpResponse();
//                }
//                byte[] bytes = response.body().bytes();
//                result=new String(bytes,"utf-8");
//                Log.i(TAG,"result:"+result );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
            return null;
        }

        private Method getMethod(String methodStr) {
            return methodStr.equalsIgnoreCase("get") ? Method.GET : Method.POST;
        }

        public void setHeader(HashMap<String, String> headers) {
            headers.forEach((k, v) -> this.httpRequest.header(k, v));
        }

        public String getHeader(String name) {
            return this.httpRequest.header(name);
        }
    }

}
