package wing.com.a12306.api;

import wing.com.a12306.config.UrlsEnum;
import wing.com.a12306.http.Session;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description 获取js脚本
 * @Version 1.0
 */
public class GetJS {

    private Session session;

    public GetJS(Session session) {
        this.session = session;
    }

    public void send() {
        this.session.httpClient.send(UrlsEnum.GET_JS);
    }
}
