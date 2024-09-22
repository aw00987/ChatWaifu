package cn.wgt.chatwaifu.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.wgt.chatwaifu.R;
import cn.wgt.chatwaifu.client.api.ChatAPIClient;
import cn.wgt.chatwaifu.client.asr.AsrClient;
import cn.wgt.chatwaifu.client.asr.WhisperAsrClient;
import cn.wgt.chatwaifu.data.SessionDataAdapter;
import cn.wgt.chatwaifu.data.audio.AudioFile;
import cn.wgt.chatwaifu.data.audio.AudioFileRepo;
import cn.wgt.chatwaifu.data.audio.DefaultAudioRepo;
import cn.wgt.chatwaifu.data.waifu.Waifu;
import cn.wgt.chatwaifu.entity.SweetSession;
import cn.wgt.chatwaifu.entity.Utterance;

public class MainActivity extends AppCompatActivity {

    //UI
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    ImageButton recordButton;
    ImageButton playButton;
    WhisperAsrClient whisperAsrClient;

    //Data
    AudioFileRepo audioFileRepo;
    SweetSession defaultSession;
    SessionDataAdapter sessionDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioFileRepo = new DefaultAudioRepo(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        recordButton = findViewById(R.id.record_btn);
        playButton = findViewById(R.id.play_btn);

        //todo：后续从提供Waifu的持久化和增删改查
        Waifu waifu = new Waifu();
        waifu.setHypnosis("你是一个优秀的人");
        waifu.setName("小龙女");
        waifu.setId("10045");
        this.defaultSession = new SweetSession(
                "默认会话", "zh-cn", waifu, null, ChatAPIClient.getInstance()
        );

        this.sessionDataAdapter = new SessionDataAdapter(defaultSession);

        recyclerView.setAdapter(sessionDataAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        //发送问题按钮
        sendButton.setOnClickListener((v) -> {
            //输入
            String question = messageEditText.getText().toString().trim();
            defaultSession.userAsk(question);
            messageEditText.setText("");
            welcomeTextView.setVisibility(View.GONE);
            refreshView();

            //输出
            new Thread(() -> {
                defaultSession.waifuAnswer();
                runOnUiThread(this::refreshView);
            }).start();
        });

        this.whisperAsrClient = new WhisperAsrClient(this, new AsrClient.IAsrCallback() {
            @Override
            public void onError(String msg) {
                //todo: do nothing
            }

            @Override
            public void onResult(AudioFile audioFile) {
                new Thread(() -> {
                    defaultSession.userSpeak(audioFile);
                    defaultSession.waifuAnswer();
                    runOnUiThread(MainActivity.this::refreshView);
                }).start();
            }
        });

        recordButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                whisperAsrClient.startRecognize();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                whisperAsrClient.stopRecognize();
            }
            return true;
        });

        playButton.setOnClickListener((v) -> {
            Utterance lastUtterance = defaultSession.getUtteranceByIndex(defaultSession.getUtteranceNum() - 1);
            if (lastUtterance.getSpeaker() == Utterance.Speaker.WAIFU) {
                AudioFile voice = lastUtterance.getVoice();
                if (voice != null) {
                    audioFileRepo.play(voice);
                }
            }
        });

    }

    private void refreshView() {
        sessionDataAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(sessionDataAdapter.getItemCount());
    }
}