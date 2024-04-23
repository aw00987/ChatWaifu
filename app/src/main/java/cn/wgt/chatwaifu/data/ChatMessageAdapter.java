package cn.wgt.chatwaifu.data;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unfbx.chatgpt.entity.chat.BaseMessage;

import java.util.List;

import cn.wgt.chatwaifu.R;
import cn.wgt.chatwaifu.entity.ChatMessage;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {

    List<ChatMessage> messageList;

    public ChatMessageAdapter(List<ChatMessage> messageList) {
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
        ChatMessage message = messageList.get(position);
        if (message.getRole().equals(BaseMessage.Role.USER)) {
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(message.getContentText());
        } else {
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTextView.setText(message.getContentText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

}
