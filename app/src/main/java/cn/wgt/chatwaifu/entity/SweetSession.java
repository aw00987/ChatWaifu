package cn.wgt.chatwaifu.entity;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.wgt.chatwaifu.client.api.ChatAPIClient;
import cn.wgt.chatwaifu.data.audio.AudioFile;
import cn.wgt.chatwaifu.data.waifu.Waifu;

public class SweetSession {

    String sessionId;
    String sessionName;
    String lang;
    Waifu waifu;
    List<Utterance> utteranceList;
    ChatAPIClient apiClient;

    public SweetSession(
            String sessionName, String lang, Waifu waifu,
            @Nullable List<Utterance> utteranceList, ChatAPIClient chatAPIClient
    ) {
        this.sessionId = UUID.randomUUID().toString();
        this.sessionName = sessionName;
        this.lang = lang;
        this.waifu = waifu;
        this.utteranceList = utteranceList == null ? new ArrayList<>() : utteranceList;
        this.apiClient = chatAPIClient;
    }

    public Utterance getUtteranceByIndex(int index) {
        return this.utteranceList.get(index);
    }

    public int getUtteranceNum() {
        return utteranceList.size();
    }

    /**
     * 适用于键盘输入
     */
    public void userAsk(String words) {
        Utterance utterance = new Utterance(Utterance.Speaker.ME, words);
        utteranceList.add(utterance);
    }

    /**
     * 适用于语音输入
     */
//    public void userSpeak(AudioFile voice) {
//        String words = apiClient.audio2Text(voice);
//        Utterance utterance = new Utterance(Utterance.Speaker.ME, words);
//        utterance.setVoice(voice);
//        utteranceList.add(utterance);
//    }
    public void waifuAnswer() {
        String answer = "我爱你";/* todo：暂时写死
        apiClient.nextMessage(waifu.getHypnosis(), utteranceList);
        */
        Utterance utterance = new Utterance(Utterance.Speaker.WAIFU, answer);
        utteranceList.add(utterance);
    }

//    public void waifuSpeak(String words) {
//        Utterance utterance = new Utterance(Utterance.Speaker.WAIFU, words);
//        utteranceList.add(utterance);
//        /*
//        new Thread(() -> {
//            AudioFile voice = apiClient.text2audio(words, lang, waifu.getName());
//            utterance.setVoice(voice);
//        }).start();
//        */
//    }

    public String toLog() {
        StringBuilder sb = new StringBuilder();
        for (Utterance utterance : utteranceList) {
            sb.append(utterance.getSpeaker().getName()).append(":")
                    .append(utterance.words).append("\n");
        }
        return sb.toString();
    }
}
