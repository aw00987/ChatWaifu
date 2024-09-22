package cn.wgt.chatwaifu.entity;

import java.util.Date;
import java.util.UUID;

import cn.wgt.chatwaifu.data.audio.AudioFile;

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
        BG("催眠咒语", "system"),
        ME("我", "user"),
        WAIFU("老婆", "assistant");
        private final String gptRoleEnum;
        private final String name;

        Speaker(String name, String gptRoleEnum) {
            this.name = name;
            this.gptRoleEnum = gptRoleEnum;
        }

        public String getGptRoleEnum() {
            return gptRoleEnum;
        }

        public String getName() {
            return name;
        }
    }

    String id;
    Speaker speaker = Speaker.ME;
    String words = "";
    Date time;
    AudioFile voice = null;

    public Utterance(Speaker speaker, String words) {
        this.id = UUID.randomUUID().toString();
        this.time = new Date();
        this.speaker = speaker;
        this.words = words;
    }

}
