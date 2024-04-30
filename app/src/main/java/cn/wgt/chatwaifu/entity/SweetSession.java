package cn.wgt.chatwaifu.entity;

import com.unfbx.chatgpt.entity.chat.BaseMessage;
import com.unfbx.chatgpt.entity.chat.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.wgt.chatwaifu.client.api.ChatAPIClient;

public class SweetSession {

    String sessionId;
    String sessionName;
    //todo: enum or db
    String waifuName;
    String lang;
    String hypnosis;//todo: env
    List<Utterance> utteranceList;

    boolean lock;//control page stuck

    ChatAPIClient apiClient;

    public SweetSession() {
        this.sessionId = UUID.randomUUID().toString();
        utteranceList = new ArrayList<>();
    }

    /**
     * 适用于键盘输入
     *
     * @param words
     */
    public void userSpeak(String words) {
        Utterance utterance = new Utterance(Utterance.Speaker.ME, words);
        utteranceList.add(utterance);
    }

    /**
     * 适用于语音输入
     *
     * @param voice
     */
    public void userSpeak(AudioFile voice) {
        Utterance utterance = new Utterance(Utterance.Speaker.ME, voice);
        utteranceList.add(utterance);
        new Thread(() -> {
            String words = apiClient.audio2Text(voice, lang);
            utterance.setWords(words);
        }).start();
    }

    public void waifuAnswer() {
        String answer = apiClient.nextMessage(this.toGPTMessages());
        waifuSpeak(answer);
    }

    public void waifuSpeak(String words) {
        Utterance utterance = new Utterance(Utterance.Speaker.WAIFU, words);
        utteranceList.add(utterance);
        new Thread(() -> {
            AudioFile voice = apiClient.text2audio(words, lang, waifuName);
            utterance.setVoice(voice);
        }).start();
    }

    public List<Utterance> getUtteranceList() {
        return utteranceList;
    }

    private List<Message> toGPTMessages() {
        List<Message> msgList = new ArrayList<>();
        msgList.add(Message.builder().role(BaseMessage.Role.SYSTEM).content(hypnosis).build());
        for (Utterance utterance : utteranceList) {
            msgList.add(utterance.toGPTMessage());
        }
        return msgList;
    }
}
