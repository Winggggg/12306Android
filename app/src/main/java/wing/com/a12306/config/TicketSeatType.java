package wing.com.a12306.config;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description 车票座席枚举
 * @Version 1.0
 */
public enum  TicketSeatType {

    SP_SEAT("P", "特等座"),
    L1_SEAT("M", "一等座"),
    L2_SEAT("O", "二等座"),
    NO_SEAT("O", "无座"),
    BUS_SEAT("9", "商务座"),
    HARD_SEAT("1", "硬座"),
    SOFT_SEAT("2", "软座"),
    HARD_SLEEPER("3", "硬卧"),
    SORT_SLEEPER("4", "软卧");

    private String code;
    private String name;

    TicketSeatType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TicketSeatType get(String code) {
        for (TicketSeatType ticketSeatType : TicketSeatType.values()) {
            if (ticketSeatType.getCode().equals(code)) {
                return ticketSeatType;
            }
        }
        throw new RuntimeException("无效的code：" + code);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
