package com.sidoso.paciente.ui.associateds;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sidoso.paciente.AssociatedActivity;
import com.sidoso.paciente.R;
import com.sidoso.paciente.adapter.AssociatedAdapter;
import com.sidoso.paciente.model.Associados;
import com.sidoso.paciente.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentAssociateds extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerAssociateds;
    private AssociatedAdapter associatedAdapter;

    private static List<Associados> associados = new ArrayList<>();
    private static boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_associateds, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_associateds);

        recyclerAssociateds = (RecyclerView) view.findViewById(R.id.list_associateds);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerAssociateds.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recyclerAssociateds.setLayoutManager(layoutManager);
        recyclerAssociateds.setHasFixedSize(true);

        if(associados.isEmpty()){
            getAssociados();
        }

        associatedAdapter = new AssociatedAdapter(associados);
        recyclerAssociateds.setAdapter(associatedAdapter);

        recyclerAssociateds.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                recyclerAssociateds,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Associados a = associados.get(position);

                        Intent intent = new Intent(getContext(), AssociatedActivity.class);
                        intent.putExtra("aName", a.getName());
                        intent.putExtra("aEmail", a.getEmail());
                        intent.putExtra("aPhoneMain", a.getPhone_main());
                        intent.putExtra("aPhoneSecondary", a.getPhone_secondary());
                        intent.putExtra("aPhotoUrl", a.getPhotoUrl());
                        intent.putExtra("aLat", a.getLat());
                        intent.putExtra("aLng", a.getLng());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        return view;
    }

    private void getAssociados(){
        Associados a;
        for(int i = 0; i < 10; i++){
            a = new Associados();
            a.setId(i);
            a.setName("Empresa "+i);
            a.setCnpj("000.000.000.000");
            a.setEmail("empresa."+i+"@gmail.com");
            a.setPhone_main("12356789");
            a.setPhone_secondary("987654321");
            a.setPhotoUrl("www.alskmlaksdm.com");
            a.setLat(-25.537963069609138);
            a.setLng(-50.899077230930324);
            associados.add(a);
        }
    }


    private void isLoading(Boolean y){
        this.isLoading = y;
        if(y){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
