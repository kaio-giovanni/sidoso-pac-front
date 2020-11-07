package com.sidoso.paciente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sidoso.paciente.R;
import com.sidoso.paciente.model.Associados;

import java.util.List;

public class AssociatedAdapter extends RecyclerView.Adapter<AssociatedAdapter.AssociatedViewHolder> {
    private List<Associados> associados;

    public AssociatedAdapter(List<Associados> associados){
        this.associados = associados;
    }

    public class AssociatedViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView type;
        //ImageView photo;

        public AssociatedViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name_associated);
            type = itemView.findViewById(R.id.tv_type_associated);
            //set photo
        }
    }

    @NonNull
    @Override
    public AssociatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.associated_adapter_item, parent, false);
        return new AssociatedAdapter.AssociatedViewHolder(chatItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AssociatedViewHolder holder, int position) {
        Associados a = associados.get(position);
        holder.name.setText(a.getName());
        holder.type.setText(a.getType());
    }

    @Override
    public int getItemCount() {
        return associados.size();
    }
}
