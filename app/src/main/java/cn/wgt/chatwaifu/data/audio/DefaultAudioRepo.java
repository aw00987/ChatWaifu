package cn.wgt.chatwaifu.data.audio;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DefaultAudioRepo implements AudioFileRepo {

    File audioFileDir;

    public DefaultAudioRepo(Context context) {
        //data/user/0/[your.package.name]/files
        String filesDir = context.getFilesDir().getAbsolutePath();
        audioFileDir = new File(filesDir + "/audio/");
        audioFileDir.mkdirs();
    }

    @Override
    public AudioFile createAudioFile() {
        AudioFile audioFile = new AudioFile();
        File outputFile = new File(audioFileDir, audioFile.getId() + ".mp3");
        audioFile.setFile(outputFile);
        return audioFile;
    }

    @Override
    public AudioFile createAudioFile(InputStream inputStream) {
        AudioFile audioFile = createAudioFile();

        try (FileOutputStream fileOutputStream = new FileOutputStream(audioFile.getFile())) {
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

    @Override
    public void deleteAudioFile(String id) {
        new File(audioFileDir, id + ".mp3").delete();
    }
}
