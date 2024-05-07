package cn.wgt.chatwaifu.client.asr;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.File;

import cn.wgt.chatwaifu.client.api.ChatAPIClient;
import cn.wgt.chatwaifu.data.audio.AudioFileRepo;
import cn.wgt.chatwaifu.data.audio.DefaultAudioRepo;
import cn.wgt.chatwaifu.data.audio.AudioFile;

public class WhisperAsrClient implements AsrClient {
    ChatAPIClient apiClient;
    AudioFileRepo audioFileRepo;
    Context context;
    IAsrCallback callback;

    public WhisperAsrClient(Context context, IAsrCallback callback) {
        this.context = context;
        this.apiClient = ChatAPIClient.getInstance();
        this.callback = callback;
        this.audioFileRepo = new DefaultAudioRepo(context);
    }

    MediaRecorder currentRecorder = null;
    AudioFile currentAudioFile = null;

    private MediaRecorder createMediaRecorder(Context context, File outputFile) {
        MediaRecorder recorder = new MediaRecorder(context);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        recorder.setOutputFile(outputFile);

        return recorder;
    }

    @Override
    public void startRecognize() {
        stopRecognize();
        try {
            currentAudioFile = audioFileRepo.createAudioFile();
            currentRecorder = createMediaRecorder(context, currentAudioFile.getFile());

            currentRecorder.prepare();
            currentRecorder.start();
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    @Override
    public void stopRecognize() {
        try {
            if (currentRecorder != null) {
                currentRecorder.stop();
                currentRecorder.reset();
                currentRecorder.release();

                callback.onResult(currentAudioFile);

                currentRecorder = null;
                currentAudioFile = null;
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
            if (currentRecorder != null) {
                currentRecorder.stop();
                currentRecorder.reset();
                currentRecorder.release();
                currentRecorder = null;
                currentAudioFile = null;
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
