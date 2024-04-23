package cn.wgt.chatwaifu.entity;

import com.unfbx.chatgpt.entity.chat.Message;

public class ChatMessage {
    Message.Role role;
    String contentText;

    public ChatMessage(Message.Role role, String contentText) {
        this.role = role;
        this.contentText = contentText;
    }

    public Message.Role getRole() {
        return role;
    }

    public String getContentText() {
        return contentText;
    }
}
