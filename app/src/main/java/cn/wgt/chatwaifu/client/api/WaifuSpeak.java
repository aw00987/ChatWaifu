package cn.wgt.chatwaifu.client.api;

import java.io.InputStream;

interface WaifuSpeak {
    InputStream toVoice(String text, String lang, String waifuName);
}
