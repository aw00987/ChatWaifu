package cn.wgt.chatwaifu.client.asr;

public interface AsrClient {
    interface IAsrCallback {
        void onError(String msg);

        void onResult(String result);
    }

    void startRecognize();

    void stopRecognize();

    void cancelRecognize();

    void destroy();
}
