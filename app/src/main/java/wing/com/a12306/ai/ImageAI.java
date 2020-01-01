package wing.com.a12306.ai;

/**
 * @Author: heweiye
 * @Date: 2020/1/1 18:10
 * @Description  图片验证码识别AI接口
 * @Version 1.0
 */
public interface ImageAI {

    /**
     * 打码
     * @return 图片识别码（如12306图片识别码：1,3,5。分别代码横排数起图片位置）
     */
    String printCode();

}
