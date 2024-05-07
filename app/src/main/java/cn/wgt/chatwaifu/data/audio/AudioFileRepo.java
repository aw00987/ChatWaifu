package cn.wgt.chatwaifu.data.audio;

import java.io.InputStream;

public interface AudioFileRepo {

    AudioFile createAudioFile();

    AudioFile createAudioFile(InputStream inputStream);

    void deleteAudioFile(String id);

    //todo: void clearAllCache();
}
