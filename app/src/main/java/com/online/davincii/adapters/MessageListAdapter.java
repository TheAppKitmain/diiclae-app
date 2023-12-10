package com.online.davincii.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.davincii.R;

import org.jetbrains.annotations.NotNull;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageHolder> {

    public MessageListAdapter() {
    }

    @NonNull
    @NotNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public class MessageHolder extends RecyclerView.ViewHolder {

        public MessageHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
