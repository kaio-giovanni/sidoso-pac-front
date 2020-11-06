package com.sidoso.paciente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidoso.paciente.R;
import com.sidoso.paciente.model.Message;
import com.sidoso.paciente.model.Paciente;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messages;
    private Integer idP;
    private String emailP;

    public MessageAdapter(List<Message> messages, Paciente p){
        this.messages = messages;

        idP = p.getId();
        emailP = p.getEmail();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        TextView message;
        TextView messageDate;


        public MessageViewHolder(@NonNull View itemView){
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.tv_author_message);
            message = (TextView) itemView.findViewById(R.id.tv_message);
            messageDate = (TextView) itemView.findViewById(R.id.tv_message_date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isUserLocal(messages.get(position))){
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View messageItem;
        if(viewType == 1){
            messageItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_send_adapter_item_, parent, false);
        }else{
            messageItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_received_adapter_item, parent, false);
        }

        return new MessageAdapter.MessageViewHolder(messageItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        if(isUserLocal(message)){
            holder.author.setText("Você");
        }
        holder.message.setText(message.getMessage());
        holder.messageDate.setText(message.getSenderAt());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private boolean isUserLocal(Message m){
        if(m.getSenderId() == this.idP && m.getReceptorEmail() != this.emailP){
            return true;
        }
        return false;
    }
}
