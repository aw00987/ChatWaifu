package cn.wgt.chatwaifu.entity;

import java.io.File;
import java.util.UUID;

public class AudioFile {
    String id;
    File file;

    public AudioFile() {
        this.id = UUID.randomUUID().toString();
    }

    public AudioFile(File file) {
        this();
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
