package cn.wgt.chatwaifu.data.audio;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class DefaultAudioRepo implements AudioFileRepo {

    MediaPlayer mediaPlayer = new MediaPlayer();
    File audioFileDir;

    public DefaultAudioRepo(Context context) {
        //data/user/0/[your.package.name]/files
        String filesDir = context.getFilesDir().getAbsolutePath();
        audioFileDir = new File(filesDir + "/audio/");
        audioFileDir.mkdirs();
    }

    @Override
    public AudioFile createTmpAudioFile() {
        File outputFile = new File(audioFileDir + "/" + UUID.randomUUID().toString());
        return new AudioFile(outputFile);
    }

    @Override
    public void saveAudioFile(AudioFile audioFile, InputStream inputStream) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(audioFile.getFile())) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAllCache() {
        for (File file : audioFileDir.listFiles()) {
            file.delete();
        }
    }

    @Override
    public void play(AudioFile audioFile) {
        try {
            mediaPlayer.setDataSource(audioFile.getFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            // 处理错误，例如文件不存在
        }
    }

    @Override
    public void deleteAudioFile(String id) {
        File file = new File(audioFileDir + "/" + id);
        file.delete();
    }
}
