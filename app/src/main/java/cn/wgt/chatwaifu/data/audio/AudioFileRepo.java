package cn.wgt.chatwaifu.data.audio;

import java.io.InputStream;

import cn.wgt.chatwaifu.entity.AudioFile;

public interface AudioFileRepo {

    AudioFile createAudioFile();

    AudioFile createAudioFile(InputStream inputStream);

    void deleteAudioFile(String id);
}
