package wing.com.a12306.api;

import cn.hutool.http.HttpResponse;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import wing.com.a12306.config.UrlConfig;
import wing.com.a12306.config.UrlsEnum;
import wing.com.a12306.exception.J12306Exception;
import wing.com.a12306.http.Session;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description 车票
 * @Version 1.0
 */
public class Ticket {

    private static final Log log = LogFactory.get();

    private Session session;
    private String trainDate;
    private String fromStation;
    private String toStation;
    private String tempCookie;

    public Ticket(Session session, String trainDate, String fromStation, String toStation) {
        // 进入查询车票页面
        session.httpClient.send(UrlsEnum.INIT_TICKET);
        log.info("进入查询车票页面，开始查票...");

        this.tempCookie = session.getCookie();
        this.session = session;
        this.trainDate = trainDate;
        this.fromStation = fromStation;
        this.toStation = toStation;
    }

    private HttpResponse query(String type) {
        UrlsEnum urlsEnum;
        switch (type) {
            case "A":
                urlsEnum = UrlsEnum.QUERY_A_TICKET;
                break;
            case "Z":
                urlsEnum = UrlsEnum.QUERY_Z_TICKET;
                break;
            default:
                throw new J12306Exception("无效的查票接口");
        }
        this.session = new Session();
        this.session.setCookie(this.tempCookie);
        UrlConfig urlConfig = urlsEnum.getUrlConfig();
        urlConfig.setUrl(urlConfig.getUrl()
                .replace("{0}", trainDate)
                .replace("{1}", this.fromStation)
                .replace("{2}", this.toStation));
        urlsEnum.setUrlConfig(urlConfig);

        return this.session.httpClient.send(urlsEnum);
    }

    public HttpResponse queryA() {
        return this.query("A");
    }

    public HttpResponse queryZ() {
        return this.query("Z");
    }
}
