package cn.wgt.chatwaifu.client.api;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.whisper.Transcriptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.wgt.chatwaifu.data.audio.AudioFileRepo;
import cn.wgt.chatwaifu.entity.AudioFile;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

public class ChatAPIClient {

    private static final ChatAPIClient instance = new ChatAPIClient();

    private ChatAPIClient() {
    }

    public static ChatAPIClient getInstance() {
        return instance;
    }

    OpenAiClient chatClient;
    OpenAiClient whisperClient;
    WaifuSpeak waifuSpeakClient;
    AudioFileRepo audioFileRepo;

    public ChatAPIClient(String apiKey) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS))
                .build();
        List<String> apiKeys = Collections.singletonList(apiKey);
        this.chatClient = new OpenAiClient.Builder()
                .okHttpClient(httpClient)
                .apiHost("https://api.openai.com/v1/chat/completions/")
                .apiKey(apiKeys)
                .build();
        this.whisperClient = new OpenAiClient.Builder()
                .okHttpClient(httpClient)
                .apiHost("https://api.openai.com/v1/audio/transcriptions/")
                .apiKey(apiKeys)
                .build();
        this.waifuSpeakClient = new WaifuSpeakClient(httpClient,
                "https://api.waifu.im/v2/speak/"//todo：等大任给
        );
    }

    public String nextMessage(List<Message> msgList) {
        ChatCompletion chat = new ChatCompletion();
        chat.setModel("gpt-3.5-turbo");
        chat.setMessages(msgList);
        ChatCompletionResponse resp = chatClient.chatCompletion(chat);
        List<ChatChoice> choices = resp.getChoices();
        ChatChoice chatChoice = choices.get(0);
        return chatChoice.getMessage().getContent();
    }

    public String audio2Text(AudioFile audioFile, String lang) {
        Transcriptions transcriptions = new Transcriptions();
        transcriptions.setModel("whisper-1");
        transcriptions.setLanguage(lang);
        return whisperClient.speechToTextTranscriptions(
                audioFile.getFile(), transcriptions
        ).getText();
    }

    public AudioFile text2audio(String text, String lang, String waifuName) {
        AudioFile audioFile;
        try (InputStream is = waifuSpeakClient.toVoice(text, lang, waifuName)) {
            audioFile = audioFileRepo.createAudioFile(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return audioFile;
    }

}
