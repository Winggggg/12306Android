package wing.com.a12306.api;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import wing.com.a12306.config.UrlsEnum;
import wing.com.a12306.http.Session;


import java.util.HashMap;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description 检查订单验证码
 * @Version 1.0
 */
public class CheckRandCodeAsync {

    private static final Log log = LogFactory.get();

    private Session session;
    private String randCode;
    private String repeatSubmitToken;

    public CheckRandCodeAsync(Session session, String randCode, String repeatSubmitToken) {
        this.session = session;
        this.randCode = randCode;
        this.repeatSubmitToken = repeatSubmitToken;
    }

    public boolean send() {
        HashMap<String, Object> formData = new HashMap<>();
        formData.put("randCode", this.randCode);
        formData.put("rand", "randp");
        formData.put("_json_att", "");
        formData.put("REPEAT_SUBMIT_TOKEN", this.repeatSubmitToken);

        HttpResponse httpResponse = this.session.httpClient.send(UrlsEnum.CHECK_RAND_CODE_ASYNC, formData);
        String body = httpResponse.body();
        JSON parse = JSONUtil.parse(body);
        log.info("CheckRandCodeAsync body = {}", body);
        String checked = (String) parse.getByPath("data.msg");
        return checked.equals("TRUE");
    }
}
