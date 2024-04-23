package cn.wgt.chatwaifu.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unfbx.chatgpt.entity.chat.BaseMessage;

import java.util.ArrayList;
import java.util.List;

import cn.wgt.chatwaifu.R;
import cn.wgt.chatwaifu.client.api.ChatAPIClient;
import cn.wgt.chatwaifu.data.ChatMessageAdapter;
import cn.wgt.chatwaifu.entity.ChatMessage;

public class MainActivity extends AppCompatActivity {

    //UI
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;

    //Data
    List<ChatMessage> messageList;
    ChatMessageAdapter chatMessageAdapter;

    //gpt
    ChatAPIClient chatAPIClient;
    public static final String YOUR_API_KEY = "sk-proj-W0525eTx5i66nRsNdp7QT3BlbkFJqEy1KaYCeCNHxAaYgE2C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        //setup recycler view
        recyclerView.setAdapter(new ChatMessageAdapter(new ArrayList<>()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        //发送问题按钮
        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            runOnUiThread(() -> {
                addChatMessage(new ChatMessage(BaseMessage.Role.USER, question));
                new Thread(() -> {
                    String answer = chatAPIClient.getAnswer(messageList);
                    runOnUiThread(() ->
                            addChatMessage(new ChatMessage(BaseMessage.Role.ASSISTANT, answer))
                    );
                }).start();
                messageEditText.setText("");
                welcomeTextView.setVisibility(View.GONE);
            });
        });

        this.chatAPIClient = new ChatAPIClient(YOUR_API_KEY);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addChatMessage(ChatMessage chatMessage) {
        messageList.add(chatMessage);
        chatMessageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(chatMessageAdapter.getItemCount());
    }
}