package wing.com.a12306.api;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import wing.com.a12306.cache.TicketCache;
import wing.com.a12306.config.Constants;
import wing.com.a12306.config.UrlsEnum;
import wing.com.a12306.dto.UserInfoDTO;
import wing.com.a12306.http.Session;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description 乘客信息
 * @Version 1.0
 */
public class PassengerDTOS {

    private Session session;
    private String repeatSubmitToken;
    private String seatType;

    public PassengerDTOS(Session session) {
        this.session = session;
    }

    public PassengerDTOS(Session session, String repeatSubmitToken, String seatType) {
        this.session = session;
        this.repeatSubmitToken = repeatSubmitToken;
        this.seatType = seatType;
    }

    /**
     * 获取乘客购票信息
     * @return passengerTicketStr
     */
    public String getPassengerTicketStr() {
        String passengerTicketStr = "{seatType},0,1,{name},1,{passengerIdCard},,N,{allEncStr}";
        // 从缓存中获取用户信息
        TicketCache ticketCache = TicketCache.getInstance();
        UserInfoDTO userInfo = (UserInfoDTO) ticketCache.get(Constants.USER_INFO_KEY);
        String idNo = userInfo.getIdNo();
        String name = userInfo.getName();

        HashMap<String, Object> formData = new HashMap<>();
        formData.put("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
        formData.put("_json_att", "");
        HttpResponse httpResponse = this.session.httpClient.send(UrlsEnum.GET_PASSENGERDTOS, formData);
        String body = httpResponse.body();
        JSON parse = JSONUtil.parse(body);
        JSONArray jsonArray = JSONUtil.parseArray(parse.getByPath("data.normal_passengers"));
        List<Object> list = jsonArray.stream().filter(object ->
                ((JSONObject) object).get("passenger_id_no").equals(idNo))
                .collect(Collectors.toList());
        String allEncStr = ((JSONObject) list.get(0)).get("allEncStr").toString();
        userInfo.setUserEncStr(allEncStr);

        ticketCache.put(Constants.USER_INFO_KEY, userInfo);

        return passengerTicketStr
                .replace("{seatType}", this.seatType)
                .replace("{name}", name)
                .replace("{passengerIdCard}", idNo)
                .replace("{allEncStr}", allEncStr);
    }

    public String getPassengerEncStr() {
        TicketCache ticketCache = TicketCache.getInstance();
        UserInfoDTO userInfo = (UserInfoDTO) ticketCache.get(Constants.USER_INFO_KEY);
        String idNo = userInfo.getIdNo();
        HashMap<String, Object> formData = new HashMap<>();
        formData.put("_json_att", "");
        HttpResponse httpResponse = this.session.httpClient.send(UrlsEnum.GET_PASSENGERDTOS, formData);
        String body = httpResponse.body();
        JSON parse = JSONUtil.parse(body);
        JSONArray jsonArray = JSONUtil.parseArray(parse.getByPath("data.normal_passengers"));
        List<Object> list = jsonArray.stream().filter(object ->
                ((JSONObject) object).get("passenger_id_no").equals(idNo))
                .collect(Collectors.toList());
        return ((JSONObject) list.get(0)).get("allEncStr").toString();
    }

    /**
     * 获取乘客信息
     * @return oldPassengerStr
     */
    public String getOldPassengerStr() {
        String oldPassengerStr = "{name},1,{passengerIdCard},1_";
        // 从缓存中获取用户信息
        TicketCache ticketCache = TicketCache.getInstance();
        UserInfoDTO userInfo = (UserInfoDTO) ticketCache.get(Constants.USER_INFO_KEY);
        String idNo = userInfo.getIdNo();
        String name = userInfo.getName();
        return oldPassengerStr.replace("{name}", name).replace("{passengerIdCard}", idNo);
    }
}
