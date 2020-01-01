package wing.com.a12306.exception;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description  自定义异常类
 * @Version 1.0
 */
public class J12306Exception extends RuntimeException {
    private String msg;

    public J12306Exception(String message) {
        super(message);
        this.msg = message;
    }

    public J12306Exception(int errorCode, String message) {
        super(message);
        this.msg = message;
    }

    public J12306Exception(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
    }

    public String getMsg() {
        return msg;
    }
}
