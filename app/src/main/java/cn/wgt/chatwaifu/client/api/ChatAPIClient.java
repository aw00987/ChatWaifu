package cn.wgt.chatwaifu.client.api;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.whisper.Transcriptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.wgt.chatwaifu.entity.ChatMessage;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

public class ChatAPIClient {

    OpenAiClient chatClient;
    OpenAiClient whisperClient;

    public ChatAPIClient(String apiKey) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS))
                .build();
        List<String> apiKeys = Collections.singletonList(apiKey);
        this.chatClient = new OpenAiClient.Builder()
                .okHttpClient(httpClient)
                .apiHost("https://api.openai.com/v1/chat/completions")
                .apiKey(apiKeys)
                .build();
        this.whisperClient = new OpenAiClient.Builder()
                .okHttpClient(httpClient)
                .apiHost("https://api.openai.com/v1/audio/transcriptions")
                .apiKey(apiKeys)
                .build();
    }

    public String getAnswer(List<ChatMessage> messages) {
        ChatCompletion chat = new ChatCompletion();
        chat.setModel("gpt-3.5-turbo");
        List<Message> msgList = new ArrayList<>();
        for (ChatMessage message : messages) {
            msgList.add(Message.builder()
                    .role(message.getRole())
                    .content(message.getContentText())
                    .build()
            );
        }
        chat.setMessages(msgList);
        ChatCompletionResponse resp = chatClient.chatCompletion(chat);
        List<ChatChoice> choices = resp.getChoices();
        ChatChoice chatChoice = choices.get(0);
        return chatChoice.getMessage().getContent();
    }

    public String audio2Text(File audioFile, String lang) {
        Transcriptions transcriptions = new Transcriptions();
        transcriptions.setModel("whisper-1");
        transcriptions.setLanguage(lang);
        return whisperClient.speechToTextTranscriptions(
                audioFile, transcriptions
        ).getText();
    }

}
