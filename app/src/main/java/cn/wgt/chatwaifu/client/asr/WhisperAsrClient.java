package cn.wgt.chatwaifu.client.asr;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.File;

import cn.wgt.chatwaifu.client.api.ChatAPIClient;

public class WhisperAsrClient implements AsrClient {
    MediaRecorder recorder = null;
    File recordFile;
    IAsrCallback callback;
    ChatAPIClient apiClient;
    Context context;
    String lang;

    public WhisperAsrClient(Context context, ChatAPIClient apiClient, String lang, IAsrCallback callback) {

        this.context = context;
        this.apiClient = apiClient;
        this.lang = lang;
        this.callback = callback;

        //todo：临时文件存放处？
        this.recordFile = new File(
                context.getFilesDir().getAbsolutePath() + "/whisper.m4a"
        );
    }

    @Override
    public void startRecognize() {
        try {
            recorder = new MediaRecorder(context);
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setOutputFile(recordFile.getAbsolutePath());
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    @Override
    public void stopRecognize() {
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                new Thread(() -> {
                    try {
                        callback.onResult(apiClient.audio2Text(this.recordFile, lang));
                    } catch (Exception e) {
                        if (callback != null) {
                            callback.onError(e.getMessage());
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    @Override
    public void cancelRecognize() {
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    @Override
    public void destroy() {
        cancelRecognize();
    }
}
