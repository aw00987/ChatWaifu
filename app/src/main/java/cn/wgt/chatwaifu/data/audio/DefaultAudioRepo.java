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
    public AudioFile createAudioFile(String fileName) {
        File outputFile = new File(audioFileDir + "/" + fileName + ".mp3");
        return new AudioFile(outputFile);
    }

    @Override
    public AudioFile createAudioFile(InputStream inputStream) {
        AudioFile audioFile = createAudioFile("默认");

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
