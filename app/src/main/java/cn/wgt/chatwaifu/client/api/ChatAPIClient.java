package cn.wgt.chatwaifu.client.api;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.wgt.chatwaifu.data.audio.AudioFileRepo;
import cn.wgt.chatwaifu.data.audio.AudioFile;
import cn.wgt.chatwaifu.entity.Utterance;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class ChatAPIClient {

    public static final String OPENAI_API_KEY = "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";

    private static final ChatAPIClient instance = new ChatAPIClient();

    public static ChatAPIClient getInstance() {
        return instance;
    }

    OkHttpClient httpClient;
    AudioFileRepo audioFileRepo;
    Gson jsonUtil = new Gson();

    private ChatAPIClient() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS))
                .build();
    }

    public String nextMessage(String preSettings, List<Utterance> utteranceList) {
        JsonArray msgList = new JsonArray();
        JsonObject bg = new JsonObject();
        bg.addProperty("role", Utterance.Speaker.BG.getGptRoleEnum());
        bg.addProperty("content", preSettings);
        msgList.add(bg);
        for (Utterance utterance : utteranceList) {
            JsonObject msg = new JsonObject();
            msg.addProperty("role", utterance.getSpeaker().getGptRoleEnum());
            msg.addProperty("content", utterance.getWords());
            msgList.add(msg);
        }
        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.addProperty("model", "gpt-4o-mini");
        requestBodyJson.add("messages", msgList);
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBodyJson.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new RuntimeException("Request failed: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class AudioRequestBody extends RequestBody {

        InputStream inputStream;

        public AudioRequestBody(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public MediaType contentType() {
            return MediaType.parse("audio/wav");
        }

        @Override
        public void writeTo(@NonNull BufferedSink sink) throws IOException {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                sink.write(buffer, 0, bytesRead);
            }
        }
    }

    //todo: 对接WhisperAPI
    public String audio2Text(AudioFile audioFile) {
        File file = audioFile.getFile();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            // 创建请求体，设置音频数据
            RequestBody audioRequestBody = new AudioRequestBody(inputStream);
            // 创建Multipart请求
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "audio.wav", audioRequestBody)
                    .addFormDataPart("model", "whisper-1")  // 如果需要其他参数可以在这里添加
                    .build();
            // 创建请求
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/audio/transcriptions/")
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                    .build();
            // 发送请求并处理响应
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // 获取响应的文本并解析为JsonObject
                return jsonUtil.fromJson(
                        response.body().string(), JsonObject.class
                ).get("text").getAsString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //todo: 对接gpt-sovits
    public AudioFile text2audio(String text, String lang, String waifuName) {
        Request req = new Request.Builder()
                .url("https://api.waifu.im/v2/speak/")
                .addHeader("lang", lang)
                .addHeader("waifuName", waifuName)
                .post(RequestBody.create(MediaType.get("text/plain; charset=utf-8"), text))
                .build();

        try (Response resp = httpClient.newCall(req).execute()) {
            if (resp.body() == null) {
                throw new RuntimeException("响应体为空");
            }
            AudioFile audioFile = audioFileRepo.createTmpAudioFile();
            audioFileRepo.saveAudioFile(audioFile, resp.body().byteStream());
            return audioFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
