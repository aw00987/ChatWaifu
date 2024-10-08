package cn.wgt.chatwaifu.data;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.wgt.chatwaifu.R;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    LinearLayout leftChatView, rightChatView;
    TextView leftTextView, rightTextView;

    public ChatMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        leftChatView = itemView.findViewById(R.id.left_chat_view);
        rightChatView = itemView.findViewById(R.id.right_chat_view);
        leftTextView = itemView.findViewById(R.id.left_chat_text_view);
        rightTextView = itemView.findViewById(R.id.right_chat_text_view);
    }

    public void rightText(String words) {
        this.leftChatView.setVisibility(View.GONE);
        this.leftTextView.setText("");
        this.rightChatView.setVisibility(View.VISIBLE);
        this.rightTextView.setText(words);
    }

    public void leftText(String words) {
        this.rightChatView.setVisibility(View.GONE);
        this.rightTextView.setText("");
        this.leftChatView.setVisibility(View.VISIBLE);
        this.leftTextView.setText(words);
    }
}