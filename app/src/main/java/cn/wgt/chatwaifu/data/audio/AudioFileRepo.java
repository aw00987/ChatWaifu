package cn.wgt.chatwaifu.data.audio;

import java.io.InputStream;

public interface AudioFileRepo {

    AudioFile createTmpAudioFile();

    void deleteAudioFile(String audioFileId);

    void saveAudioFile(AudioFile audioFile, InputStream inputStream);

    void clearAllCache();

    void play(AudioFile audioFile);
}
