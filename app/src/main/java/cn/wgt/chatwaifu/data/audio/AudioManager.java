package cn.wgt.chatwaifu.data.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class AudioManager implements AutoCloseable {

    private final Context context;

    private final File audioFileDir;

    private boolean playing = false;
    private MediaPlayer mediaPlayer = null;
    private File currentPlayFile = null;

    private boolean recording = false;
    private MediaRecorder currentAudioRecorder = null;
    private File currentAudioFile = null;

    /**
     * 构造函数，传入Context以便获取内部存储路径
     */
    public AudioManager(Context context) {
        this.context = context;
        this.audioFileDir = new File(context.getFilesDir(), "audio");
        if (!audioFileDir.exists()) {
            audioFileDir.mkdir();
        }
    }

    private MediaRecorder createAudioRecorder(File outputFile) {
        MediaRecorder mediaRecorder = new MediaRecorder(this.context);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  // 设置录音源为麦克风
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 输出格式为MP4
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);  // 编码格式为AAC
        mediaRecorder.setAudioSamplingRate(44100);  // 设置采样率
        mediaRecorder.setAudioEncodingBitRate(96000);  // 设置编码比特率
        mediaRecorder.setOutputFile(outputFile);  // 录音结果保存的文件路径
        return mediaRecorder;
    }

    private File createAudioFile() {
        return new File(this.audioFileDir, UUID.randomUUID().toString() + ".mp3");
    }

    /**
     * 开始录音
     */
    public void startRecording() {
        if (this.recording) {
            stopRecording();// 如果正在录音，则结束上一次录音，并开始新的录音
        }
        this.currentAudioFile = createAudioFile();
        this.currentAudioRecorder = createAudioRecorder(this.currentAudioFile);
        try {
            this.currentAudioRecorder.prepare();  // 准备录音
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.currentAudioRecorder.start();    // 开始录音
        this.recording = true;
    }

    /**
     * 结束录音
     */
    public void stopRecording() {
        if (this.recording) {
            this.currentAudioRecorder.stop();  // 停止录音
            this.currentAudioRecorder.release();  // 释放资源
            this.currentAudioFile = null;
            this.currentAudioRecorder = null;  // 设置为null，避免重复释放
            this.recording = false;
        }
    }

    /**
     * 获取录音结果的文件
     */
    public File getCurrentRecordedAudioFile() {
        return this.currentAudioFile;  // 返回录音文件
    }

    /**
     * 通过录音ID（也是文件名前缀）获取文件
     */
    public File getAudioFileById(String audioId) {
        return new File(this.audioFileDir, audioId + ".mp3");
    }

    public File saveAudioFile(InputStream inputStream) {
        File audioFile = this.createAudioFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(audioFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return audioFile;
    }

    public void startPlay(String audioId) {
        if (playing) {
            stopPlay();
            mediaPlayer = new MediaPlayer();

        }
        try {
            File audioFile = this.getAudioFileById(audioId);
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            // 处理错误，例如文件不存在
        }
    }

    public void stopPlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        mediaPlayer.release();
    }

    @Override
    public void close() throws Exception {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        stopRecording();
        if (currentAudioRecorder != null) {
            currentAudioRecorder.release();
        }
    }
}
