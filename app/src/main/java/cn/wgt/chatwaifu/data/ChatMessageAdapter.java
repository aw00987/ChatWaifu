package cn.wgt.chatwaifu.data;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unfbx.chatgpt.entity.chat.BaseMessage;

import java.util.List;

import cn.wgt.chatwaifu.R;
import cn.wgt.chatwaifu.entity.Utterance;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {

    List<Utterance> messageList;

    public ChatMessageAdapter(List<Utterance> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View chatView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, null);
        return new ChatMessageViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        Utterance message = messageList.get(position);
        if (message.getSpeaker() == Utterance.Speaker.ME) {
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(message.getWords());
        } else if (message.getSpeaker() == Utterance.Speaker.WAIFU) {
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTextView.setText(message.getWords());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

}
