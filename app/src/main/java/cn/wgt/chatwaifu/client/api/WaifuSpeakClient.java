package cn.wgt.chatwaifu.client.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WaifuSpeakClient implements WaifuSpeak {

    OkHttpClient httpClient;
    String url;

    public WaifuSpeakClient(OkHttpClient httpClient, String url) {
        this.httpClient = httpClient;
        this.url = url;
    }

    @Override
    public InputStream toVoice(String text, String lang, String waifuName) {
        Request req = new Request.Builder().url(url)
                .addHeader("lang", lang)
                .addHeader("waifuName", waifuName)
                .post(RequestBody.create(MediaType.get("text/plain; charset=utf-8"), text))
                .build();

        try (Response resp = httpClient.newCall(req).execute()) {
            assert resp.body() != null;
            return resp.body().byteStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
