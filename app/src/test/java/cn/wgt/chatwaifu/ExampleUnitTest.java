package cn.wgt.chatwaifu;

import org.junit.Test;

import cn.wgt.chatwaifu.client.api.ChatAPIClient;
import cn.wgt.chatwaifu.data.waifu.Waifu;
import cn.wgt.chatwaifu.entity.SweetSession;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void checkConnectionToOpenAI() {
        Waifu waifu = new Waifu();
        waifu.setHypnosis("你是一个优秀的人");
        waifu.setName("小龙女");
        waifu.setId("10045");
        SweetSession session = new SweetSession(
                "默认会话", "zh-cn", waifu, null, ChatAPIClient.getInstance()
        );
        session.userAsk("你好");
        session.waifuAnswer();
        System.out.println(session.toLog());
    }
}