package wing.com.a12306;

import java.util.LinkedHashMap;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONException;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import wing.com.a12306.api.AlternateOrder;
import wing.com.a12306.api.Login;
import wing.com.a12306.api.PassengerDTOS;
import wing.com.a12306.api.SubmitOrderRequest;
import wing.com.a12306.api.Ticket;
import wing.com.a12306.cache.TicketCache;
import wing.com.a12306.config.Constants;
import wing.com.a12306.config.TicketSeatType;
import wing.com.a12306.dto.TicketInfoDTO;
import wing.com.a12306.dto.UserInfoDTO;
import wing.com.a12306.exception.J12306Exception;
import wing.com.a12306.http.Session;
import wing.com.a12306.utils.J12306Util;
import wing.com.a12306.utils.StationUtil;
import wing.com.a12306.utils.YmlUtil;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description  12306抢票程序
 * @Version 1.0
 */
public class Go12306 {

    private static final Log log = LogFactory.get();

    private Session session;    // 会话保持

    private String username;    // 12306用户账号
    private String password;    // 密码

    private TicketCache ticketCache = TicketCache.getInstance();

    /*乘客订票相关参数*/
    private String trainDate;  // 乘车日期（2019-10-01）
    private String fromStation; // 出发站（IZQ）
    private String toStation;   // 到达站（FAQ）
    private String trainNums;   // 列车编号（D2834）。多个使用英文半角逗号分隔。目前暂时只能人工看列车编号啦
    private String seats;   // 列车座席。M,O,N分别代表：一等座、二等座、无座。目前只支持这三种选择

    private int queryCount = 0; // 刷票次数

    public static Go12306 newInstance() {
        return new Go12306();
    }

    public Go12306 initUser(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * 初始化订票参数信息
     * @param trainDate 乘车日期
     * @param fromStation 始发站
     * @param toStation 终点站
     * @param trainNums 列表车次
     * @param seats 座席类型：M、O、N
     */
    public Go12306 initBookTicketInfo(String trainDate, String fromStation, String toStation, String trainNums, String seats) {
        this.trainDate = J12306Util.formatDateStr(trainDate);
        final String fromStationCode = StationUtil.getStationCode(fromStation);
        if (fromStationCode == null) {
            throw new J12306Exception("无法找到始发站站点【" + fromStation + "】，请确保始发站点名正确。");
        }
        final String toStationCode = StationUtil.getStationCode(toStation);
        if (toStationCode == null) {
            throw new J12306Exception("无法找到到达站站点【" + fromStation + "】，请确保到达站点名正确。");
        }
        this.fromStation = fromStationCode;
        this.toStation = toStationCode;
        this.trainNums = trainNums;
        this.seats = seats;
        return this;
    }

    public void run() {
        // 构建会话
        this.session = new Session();
        // 开始登录
        Login login = new Login(this.session, this.username, this.password);
        UserInfoDTO userInfo = login.send();
        int tryLoginCount = 0;
        while (true) {
            if (userInfo == null) {
                if (tryLoginCount >= 5) {
                    throw new J12306Exception("无法登录，程序已终止，请手动重试登录");
                }
                log.error("登录失败，正在第{}次尝试登录", tryLoginCount++);
                userInfo = login.send();
            } else {
                break;
            }
        }
        // 用户信息保存到缓存中
        this.ticketCache.put(Constants.USER_INFO_KEY, userInfo);

        // 开始查询余票
        Ticket ticket = new Ticket(this.session, this.trainDate, this.fromStation, this.toStation);

        int querySpeed = (Integer) YmlUtil.get("j12306.ticket.queryspeed");

        // 计算刷票粒度
//        int intervalTime = querySpeed / threadPoolSize;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        int queryA302Count = 0, queryZ302Count = 0;
        String usingQuery = (String) YmlUtil.get("j12306.ticket.queryp");
        boolean isPutTime = false;
        stopLop: while (true) {
            HttpResponse httpResponse;
            try {
                if ("A".equals(usingQuery)) {
                    httpResponse = ticket.queryA();
                } else if ("Z".equals(usingQuery)) {
                    httpResponse = ticket.queryZ();
                } else {
                    throw new J12306Exception("查票接口异常，请确认config.yml[j12306.ticket.queryp]配置正确");
                }
            } catch (HttpException e) {
                log.error("请求超时，或无法访问，错误信息：{}", e.getMessage());
                continue;
            }

            String body = httpResponse.body();
            log.info("query tickets status = {}，body={}", httpResponse.getStatus(), body);

            if (httpResponse.getStatus() == Constants.REQ_SUCCESS_STATUS) {
                List<TicketInfoDTO> ticketInfoDTOS;
                try {
                    ticketInfoDTOS = J12306Util.parseTicketInfo(body);
                } catch (JSONException e) {
                    log.error("查询车票发生未知异常：{}", e.getMessage());
                    continue;
                }
                for (TicketInfoDTO ticketInfoDTO : ticketInfoDTOS) {
                    if (!ticketInfoDTO.isOnSale()) {
                        continue;
                    }
                    String trainNum = ticketInfoDTO.getTrainNum();
                    String canNotAlternateSeatType = ticketInfoDTO.getCanNotAlternateSeatType();

                    final LinkedHashMap<String, Boolean> seatsTicketInfo = J12306Util.getSeatsTicketInfo(this.seats, ticketInfoDTO);

                    // 先进行一次解码。避免提交后再编码一次导致参数失效
                    String secretStr = J12306Util.urlDecode(ticketInfoDTO.getSecretStr());
                    String leftTicket = J12306Util.urlDecode(ticketInfoDTO.getLeftTicket());
                    String trainNo = ticketInfoDTO.getTrainNo();
                    String fromStationCode = ticketInfoDTO.getFormStationTelecode();
                    String toStationCode = ticketInfoDTO.getToStationTelecode();
                    String trainLocation = ticketInfoDTO.getTrainLocation();

                    if (!J12306Util.noNeedTicket(seatsTicketInfo)) {
                        log.info("可预订车票信息：发车日期【{}】，车次【{}】，出发时间：{}，到达时间：{}，座席：",
                                trainDate, trainNum, ticketInfoDTO.getGoOffTime(), ticketInfoDTO.getArrivalTime());
                    }

                    // 跳过不是购票意向的车次
                    if (!this.trainNums.contains(trainNum)) {
                        continue;
                    }

                    // 候补订单
                    if (J12306Util.noNeedTicket(seatsTicketInfo)) {
                        if ((boolean) YmlUtil.get("j12306.ticket.alternate")) {
                            // 判断是否能候补订单
                            if (this.ticketCache.get(trainNo) != null) {
                                continue;
                            }
                            if (ticketInfoDTO.isCanAlternate()) {
                                final String allEncStr = new PassengerDTOS(this.session).getPassengerEncStr();
                                for (String seatCode : seatsTicketInfo.keySet()) {
                                    if ("".equals(canNotAlternateSeatType) || !canNotAlternateSeatType.contains(seatCode)) {
                                        try {
                                            log.info("准备提交候补订单：车次【{}】，发车日期【{}】，座席类型：{}", trainNum, trainDate, seatCode);
                                            AlternateOrder alternateOrder = new AlternateOrder(this.session, ticketInfoDTO.getSecretStr(), allEncStr, seatCode, trainNo);
                                            if (alternateOrder.checkFace()) {
                                                alternateOrder.getSuccessRate();
                                            }
                                        } catch (Exception e) {
                                            if (e instanceof J12306Exception) {
                                                log.info("抢票程序结束");
                                                break stopLop;
                                            }
                                            log.error("候补异常：{}，列车【】加入小黑屋关闭3分钟", e.getMessage(), trainNum);
                                            ticketCache.put(trainNo, trainNo, Constants.BLACK_ROOM_CACHE_EXP_TIME * 60);
                                        }
                                    }
                                }
                            }
                        }
                        continue;
                    }

                    // 跳过小黑屋中的车次
                    if (ticketCache.get(trainNum) != null) {
                        continue;
                    }

                    try {
                        for (String seatCode : seatsTicketInfo.keySet()) {
                            if (seatsTicketInfo.get(seatCode)) {
                                log.info("提交订单：车次【{}】，发车日期【{}】，座席类型：{}", trainNum, trainDate, TicketSeatType.get(seatCode).getName());
                                new SubmitOrderRequest(this.session, secretStr, seatCode, trainDate, fromStationCode, toStationCode, trainNo, trainNum, trainLocation)
                                        .send();
                            }
                        }
                    } catch (J12306Exception e) {
                        log.info("抢票程序结束：{}", e.getMsg());
                        break stopLop;
                    }
                }
                this.queryCount++;
                log.info("-------线程【{}】已为账号{}刷票{}次，启程日期：{}--------", Thread.currentThread().getName(), this.username, this.queryCount, trainDate);
            } else {
                log.error("-------线程【{}】无法获取车票信息，状态码：{}；程序会在{}次访问302后切换到另一个查询接口", Thread.currentThread().getName(), httpResponse.getStatus(), Constants.MAX_302);
                if (httpResponse.getStatus() == 302) {
                    if ("A".equals(usingQuery)) {
                        queryA302Count++;
                    }
                    if ("Z".equals(usingQuery)) {
                        queryZ302Count++;
                    }
                    if (queryA302Count >= Constants.MAX_302) {
                        usingQuery = "Z";
                    }
                    if (queryZ302Count >= Constants.MAX_302) {
                        usingQuery = "A";
                    }
                }
            }
            String puttime = (String) YmlUtil.get("j12306.ticket.puttime");
            if (!StrUtil.isNullOrUndefined(puttime)) {
                if (J12306Util.getCurrAftOneMinuteTime().equals(puttime.replace("-", ":"))) {
                    isPutTime = true;
                    querySpeed = (Integer) YmlUtil.get("j12306.ticket.puttimespeed");
                }
            }
            if (isPutTime && J12306Util.getAfter5MinuteTime(puttime.replace("-", ":")).equals(J12306Util.getCurrTime())) {
                isPutTime = false;
                querySpeed = (Integer) YmlUtil.get("j12306.ticket.queryspeed");
            }
            J12306Util.sleepM(querySpeed);
        }

    }


}
