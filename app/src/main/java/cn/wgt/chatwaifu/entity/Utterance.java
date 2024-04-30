package cn.wgt.chatwaifu.entity;

import com.unfbx.chatgpt.entity.chat.BaseMessage;
import com.unfbx.chatgpt.entity.chat.Message;

import java.util.UUID;

public class Utterance {

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public AudioFile getVoice() {
        return voice;
    }

    public void setVoice(AudioFile voice) {
        this.voice = voice;
    }

    public String getId() {
        return id;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public enum Speaker {
        ME,
        WAIFU;
    }

    String id;
    Speaker speaker = Speaker.ME;
    String words = "";
    AudioFile voice = null;

    public Utterance() {
        this.id = UUID.randomUUID().toString();
    }

    public Utterance(Speaker speaker, AudioFile voice) {
        this();
        this.speaker = speaker;
        this.voice = voice;
    }

    public Utterance(Speaker speaker, String words) {
        this();
        this.speaker = speaker;
        this.words = words;
    }

    public Message toGPTMessage() {
        BaseMessage.Role role = null;
        switch (speaker) {
            case ME:
                role = BaseMessage.Role.USER;
                break;
            case WAIFU:
                role = BaseMessage.Role.ASSISTANT;
                break;
            default:
                break;
        }
        return Message.builder().role(role).content(this.words).build();
    }

}
