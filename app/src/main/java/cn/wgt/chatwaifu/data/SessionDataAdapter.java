package cn.wgt.chatwaifu.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.wgt.chatwaifu.R;
import cn.wgt.chatwaifu.entity.SweetSession;
import cn.wgt.chatwaifu.entity.Utterance;

public class SessionDataAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {

    SweetSession session;

    public SessionDataAdapter(SweetSession session) {
        this.session = session;
    }

    public void resetSession(SweetSession session) {
        this.session = session;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ChatMessageViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        Utterance message = session.getUtteranceByIndex(position);
        if (message.getSpeaker() == Utterance.Speaker.ME) {
            holder.rightText(message.getWords());
        } else if (message.getSpeaker() == Utterance.Speaker.WAIFU) {
            holder.leftText(message.getWords());
        }
    }

    @Override
    public int getItemCount() {
        return session.getUtteranceNum();
    }

}
