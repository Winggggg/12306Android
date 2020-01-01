package wing.com.a12306.dto;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description 车票信息
 * @Version 1.0
 */
public class TicketInfoDTO {

    private boolean onSale; // 是否开售
    private String secretStr;   // 密钥串
    private String trainNo;     // 列车号
    private String trainNum;    // 车次
    private String formStationName; // 始发站名称
    private String toStationName; // 终点站名称
    private String formStationTelecode; // 12306始发站代码
    private String toStationTelecode;   // 12306终点站代码
    private String goOffTime;   // 出发时间
    private String arrivalTime; // 到达时间
    private String lastTime;    // 历时时间
    private String leftTicket;
    private String trainLocation;

    private String businessSeat;    // 商务特等座
    private String l1Seat;  // 一等座
    private String l2Seat;  // 二等座
    private String l1SoftBerth; // 软卧一等卧
    private String l2HardBerth; // 硬卧二等卧
    private String hardSeat;    // 硬座
    private String softSeat;    // 软座
    private String noSeat;      // 无座
    private boolean canAlternate;   // 是否可候补。1代表能候补
    private String canNotAlternateSeatType; // 不能候补的座席

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public String getSecretStr() {
        return secretStr;
    }

    public void setSecretStr(String secretStr) {
        this.secretStr = secretStr;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainNum() {
        return trainNum;
    }

    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }

    public String getFormStationName() {
        return formStationName;
    }

    public void setFormStationName(String formStationName) {
        this.formStationName = formStationName;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public String getFormStationTelecode() {
        return formStationTelecode;
    }

    public void setFormStationTelecode(String formStationTelecode) {
        this.formStationTelecode = formStationTelecode;
    }

    public String getToStationTelecode() {
        return toStationTelecode;
    }

    public void setToStationTelecode(String toStationTelecode) {
        this.toStationTelecode = toStationTelecode;
    }

    public String getGoOffTime() {
        return goOffTime;
    }

    public void setGoOffTime(String goOffTime) {
        this.goOffTime = goOffTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getLeftTicket() {
        return leftTicket;
    }

    public void setLeftTicket(String leftTicket) {
        this.leftTicket = leftTicket;
    }

    public String getTrainLocation() {
        return trainLocation;
    }

    public void setTrainLocation(String trainLocation) {
        this.trainLocation = trainLocation;
    }

    public String getBusinessSeat() {
        return businessSeat;
    }

    public void setBusinessSeat(String businessSeat) {
        this.businessSeat = businessSeat;
    }

    public String getL1Seat() {
        return l1Seat;
    }

    public void setL1Seat(String l1Seat) {
        this.l1Seat = l1Seat;
    }

    public String getL2Seat() {
        return l2Seat;
    }

    public void setL2Seat(String l2Seat) {
        this.l2Seat = l2Seat;
    }

    public String getL1SoftBerth() {
        return l1SoftBerth;
    }

    public void setL1SoftBerth(String l1SoftBerth) {
        this.l1SoftBerth = l1SoftBerth;
    }

    public String getL2HardBerth() {
        return l2HardBerth;
    }

    public void setL2HardBerth(String l2HardBerth) {
        this.l2HardBerth = l2HardBerth;
    }

    public String getHardSeat() {
        return hardSeat;
    }

    public void setHardSeat(String hardSeat) {
        this.hardSeat = hardSeat;
    }

    public String getSoftSeat() {
        return softSeat;
    }

    public void setSoftSeat(String softSeat) {
        this.softSeat = softSeat;
    }

    public String getNoSeat() {
        return noSeat;
    }

    public void setNoSeat(String noSeat) {
        this.noSeat = noSeat;
    }

    public boolean isCanAlternate() {
        return canAlternate;
    }

    public void setCanAlternate(boolean canAlternate) {
        this.canAlternate = canAlternate;
    }

    public String getCanNotAlternateSeatType() {
        return canNotAlternateSeatType;
    }

    public void setCanNotAlternateSeatType(String canNotAlternateSeatType) {
        this.canNotAlternateSeatType = canNotAlternateSeatType;
    }

    @Override
    public String toString() {
        return "TicketInfoDTO{" +
                "secretStr='" + secretStr + '\'' +
                ", trainNo='" + trainNo + '\'' +
                ", trainNum='" + trainNum + '\'' +
                ", formStationName='" + formStationName + '\'' +
                ", toStationName='" + toStationName + '\'' +
                ", formStationTelecode='" + formStationTelecode + '\'' +
                ", toStationTelecode='" + toStationTelecode + '\'' +
                ", goOffTime='" + goOffTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", leftTicket='" + leftTicket + '\'' +
                ", trainLocation='" + trainLocation + '\'' +
                ", businessSeat='" + businessSeat + '\'' +
                ", l1Seat='" + l1Seat + '\'' +
                ", l2Seat='" + l2Seat + '\'' +
                ", l1SoftBerth='" + l1SoftBerth + '\'' +
                ", l2HardBerth='" + l2HardBerth + '\'' +
                ", hardSeat='" + hardSeat + '\'' +
                ", softSeat='" + softSeat + '\'' +
                ", noSeat='" + noSeat + '\'' +
                ", canAlternate=" + canAlternate +
                ", canNotAlternateSeatType='" + canNotAlternateSeatType + '\'' +
                '}';
    }
}
