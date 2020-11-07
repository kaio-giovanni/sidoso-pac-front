package com.sidoso.paciente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidoso.paciente.R;
import com.sidoso.paciente.model.Profissional;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Profissional> profissionais;

    public ChatAdapter(List<Profissional> profissionais){
        this.profissionais = profissionais;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView name;
        TextView prof_job;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.professional_img);
            name = itemView.findViewById(R.id.professional_name);
            prof_job = itemView.findViewById(R.id.professional_job);
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_item, parent, false);
        return new ChatViewHolder(chatItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Profissional p = profissionais.get(position);
        //holder.photo.setImageBitmap(p.getPhotoUrl());
        holder.name.setText(p.getName());
        holder.prof_job.setText(p.getProfissao().getName());
    }

    @Override
    public int getItemCount() {
        return profissionais.size();
    }
}
