package com.sidoso.paciente.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sidoso.paciente.R;

public class FragmentSettings extends Fragment {
    private ListView lt_settings;
    private ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        lt_settings = (ListView) view.findViewById(R.id.lv_settings);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.list_settings_fragment, android.R.layout.simple_list_item_1);
        lt_settings.setAdapter(adapter);

        lt_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), adapter.getItem(i), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
