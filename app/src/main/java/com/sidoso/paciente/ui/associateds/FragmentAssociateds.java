package com.sidoso.paciente.ui.associateds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sidoso.paciente.AssociatedActivity;
import com.sidoso.paciente.R;
import com.sidoso.paciente.adapter.AssociatedAdapter;
import com.sidoso.paciente.http.VolleySingleton;
import com.sidoso.paciente.model.Associados;
import com.sidoso.paciente.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.sidoso.paciente.config.Constants.API_URL;
import static com.sidoso.paciente.config.Constants.FILE_PREFERENCES;

public class FragmentAssociateds extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerAssociateds;
    private AssociatedAdapter associatedAdapter;
    private SharedPreferences mUserSaved;

    private static List<Associados> associados = new ArrayList<>();
    private static boolean requestStarted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_associateds, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_associateds);
        progressBar.setVisibility(requestStarted == true ? View.VISIBLE : View.GONE);

        recyclerAssociateds = (RecyclerView) view.findViewById(R.id.list_associateds);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerAssociateds.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recyclerAssociateds.setLayoutManager(layoutManager);
        recyclerAssociateds.setHasFixedSize(true);

        mUserSaved = getContext().getSharedPreferences(FILE_PREFERENCES, MODE_PRIVATE);

        if(associados.isEmpty() && !requestStarted){
            getAssociados(mUserSaved.getInt("userId", 0), mUserSaved.getString("tokenApi", ""));
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
                        intent.putExtra("aType", a.getType());
                        intent.putExtra("aEmail", a.getEmail());
                        intent.putExtra("aPhoneMain", a.getPhone_main());
                        intent.putExtra("aPhoneSecondary", a.getPhone_secondary());
                        intent.putExtra("aPhotoUrl", a.getPhotoUrl());
                        intent.putExtra("aLat", a.getLat());
                        intent.putExtra("aLng", a.getLng());

                        startActivity(intent);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {}
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
                }
        ));
        return view;
    }

    private void getAssociados(int idUser, final String tokenApi){
        String url = API_URL.concat("paciente/"+idUser+"/associados/");

        isLoading(true);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Associados a;
                isLoading(false);

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);

                        a = new Associados();
                        a.setId(object.getInt("id"));
                        a.setName(object.getString("name"));
                        a.setType(object.getString("type"));
                        a.setPhone_main(object.getString("phone_main"));
                        a.setPhone_secondary(object.getString("phone_secondary"));
                        a.setEmail(object.getString("email"));
                        a.setCnpj(object.getString("cnpj"));
                        a.setLat(new Double(object.getString("latitude")));
                        a.setLng(new Double(object.getString("longitude")));
                        a.setPhotoUrl(object.getString("logo"));

                        associados.add(a);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                associatedAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                isLoading(false);

                if(networkResponse == null){
                    Log.e("ErrorResponseMessage", error.getClass().toString());
                }else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);

                        Log.e("ErrorMessageProf", response.toString());
                        Toast.makeText(getContext(), response.getString("error"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", tokenApi);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void isLoading(Boolean y){
        FragmentAssociateds.requestStarted = y;
        if(y){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
