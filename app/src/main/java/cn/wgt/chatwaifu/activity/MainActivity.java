package cn.wgt.chatwaifu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.wgt.chatwaifu.R;
import cn.wgt.chatwaifu.client.api.ChatAPIClient;
import cn.wgt.chatwaifu.data.ChatMessageAdapter;
import cn.wgt.chatwaifu.entity.SweetSession;

public class MainActivity extends AppCompatActivity {

    //UI
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;

    //Data
    SweetSession defaultSession;
    ChatMessageAdapter chatMessageAdapter;

    //GPT
    ChatAPIClient chatAPIClient;
    public static final String YOUR_API_KEY = "sk-proj-W0525eTx5i66nRsNdp7QT3BlbkFJqEy1KaYCeCNHxAaYgE2C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //link view;
        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        //setup data
        this.defaultSession = new SweetSession();
        this.chatMessageAdapter = new ChatMessageAdapter(defaultSession.getUtteranceList());

        //setup recyclerView
        recyclerView.setAdapter(chatMessageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        //发送问题按钮
        sendButton.setOnClickListener((v) -> {
            //输入
            String question = messageEditText.getText().toString().trim();
            defaultSession.userSpeak(question);
            messageEditText.setText("");
            welcomeTextView.setVisibility(View.GONE);
            refreshView();

            //输出
            defaultSession.waifuAnswer();
            refreshView();
        });

        this.chatAPIClient = new ChatAPIClient(YOUR_API_KEY);
    }


    private void refreshView() {
        chatMessageAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(chatMessageAdapter.getItemCount());
    }
}