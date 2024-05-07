package cn.wgt.chatwaifu.client.asr;

import cn.wgt.chatwaifu.data.audio.AudioFile;

public interface AsrClient {

    interface IAsrCallback {
        void onError(String msg);

        void onResult(AudioFile audioFile);
    }

    void startRecognize();

    void stopRecognize();

    void cancelRecognize();

    void destroy();
}
