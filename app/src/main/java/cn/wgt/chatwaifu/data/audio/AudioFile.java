package cn.wgt.chatwaifu.data.audio;

import java.io.File;
import java.util.UUID;

public class AudioFile {
    String id;
    File file;

    public AudioFile(File file) {
        this.id = UUID.randomUUID().toString();
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

}
