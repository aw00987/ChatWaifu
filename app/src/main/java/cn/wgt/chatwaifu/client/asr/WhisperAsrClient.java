package cn.wgt.chatwaifu.client.asr;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.File;

import cn.wgt.chatwaifu.data.audio.AudioFileRepo;
import cn.wgt.chatwaifu.data.audio.DefaultAudioRepo;
import cn.wgt.chatwaifu.data.audio.AudioFile;

public class WhisperAsrClient implements AsrClient {

    Context context;
    AudioFileRepo audioFileRepo;
    MediaRecorder currentRecorder = null;
    AudioFile currentAudioFile = null;

    public WhisperAsrClient(AudioFileRepo audioFileRepo, Context context) {
        this.context = context;
        this.audioFileRepo = audioFileRepo;
    }

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
        currentAudioFile = audioFileRepo.createTmpAudioFile();
        currentRecorder = createMediaRecorder(context, currentAudioFile.getFile());
        currentRecorder.prepare();
        currentRecorder.start();
    }

    @Override
    public void stopRecognize() {
        if (currentRecorder != null) {
            currentRecorder.stop();
            currentRecorder.reset();
            currentRecorder.release();
            currentRecorder = null;
            currentAudioFile = null;
        }
    }

    public AudioFile getRecordResult() {

    }
}
