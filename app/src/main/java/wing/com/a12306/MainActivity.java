package wing.com.a12306;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class MainActivity extends AppCompatActivity {
    private static final Log log = LogFactory.get();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log.info("test========================================");
        new Thread(() -> selectTicket1()).start();
    }


    public  void selectTicket1() {
        Go12306.newInstance()
                .initUser("18219112889", "950906hwy")    // 用户名/密码
                .initBookTicketInfo("2020-01-21", // 乘车日期
                        "广州南",   // 始发站
                        "昆明南",   // 到达站
                        "D3835,D3858",    // 列车编号（D2834）。多个使用英文半角逗号分隔。目前暂时只能人工看列车编号啦
                        "M,O,1")    // 列车座席。P,M,O,N,4,3,2,1分别代表：商务特等座(P)、一等座(M)、二等座(O)、无座(N)、软卧(4)、硬卧(3)、软座(2)、硬座(1)。
                .run();
    }


}
