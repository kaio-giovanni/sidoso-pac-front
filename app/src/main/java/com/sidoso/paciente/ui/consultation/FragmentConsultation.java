package com.sidoso.paciente.ui.consultation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sidoso.paciente.R;
import com.sidoso.paciente.adapter.ConsultaAdapter;
import com.sidoso.paciente.http.VolleySingleton;
import com.sidoso.paciente.model.Consulta;
import com.sidoso.paciente.model.Paciente;
import com.sidoso.paciente.model.Profissao;
import com.sidoso.paciente.model.Profissional;
import com.sidoso.paciente.ui.professionals.FragmentProfessionals;
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
import static com.sidoso.paciente.utils.Helper.stringToDate;

public class FragmentConsultation extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerConsultation;
    private TextView tv_empty;
    private ConsultaAdapter consultaAdapter;
    private SharedPreferences mUserSaved;

    private static List<Consulta> consultas = new ArrayList<Consulta>();
    private static boolean requestStarted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultation, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_consultation);
        progressBar.setVisibility(requestStarted == true ? View.VISIBLE : View.GONE);

        recyclerConsultation = (RecyclerView) view.findViewById(R.id.list_consultation);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerConsultation.setLayoutManager(layoutManager);
        recyclerConsultation.setHasFixedSize(true);

        tv_empty = (TextView) view.findViewById(R.id.tv_empty_consultation);

        mUserSaved = getContext().getSharedPreferences(FILE_PREFERENCES, MODE_PRIVATE);

        if(consultas.isEmpty() && !requestStarted){
            recyclerConsultation.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
            getConsultas(mUserSaved.getInt("userId", 0), mUserSaved.getString("tokenApi", ""));
        }

        consultaAdapter = new ConsultaAdapter(consultas);
        recyclerConsultation.setAdapter(consultaAdapter);

        recyclerConsultation.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                recyclerConsultation,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {}
                    @Override
                    public void onLongItemClick(View view, int position) {}
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
                }
        ));

        return view;
    }

    private void getConsultas(int idUser, final String tokenApi){
        String url = API_URL.concat("paciente/"+idUser+"/consulta/");

        isLoading(true);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Consulta consulta = new Consulta();
                Profissional profissional = new Profissional();
                Profissao profissao;
                Paciente paciente = new Paciente();
                isLoading(false);

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        JSONObject profissionalJson = object.getJSONObject("profissional");
                        JSONObject profissaoJson = profissionalJson.getJSONObject("profissao");
                        JSONObject pacienteJson = object.getJSONObject("paciente");

                        profissao = new Profissao(profissaoJson.getInt("id"), profissaoJson.getString("name"));

                        profissional.setId(profissionalJson.getInt("id"));
                        profissional.setName(profissionalJson.getString("name"));
                        profissional.setProfissao(profissao);

                        paciente.setId(pacienteJson.getInt("id"));
                        paciente.setName(pacienteJson.getString("name"));
                        paciente.setDt_birth(pacienteJson.getString("birth"));
                        paciente.setGenre(pacienteJson.getString("genre"));
                        paciente.setPhone1(pacienteJson.getString("phone_main"));
                        paciente.setEmail(pacienteJson.getString("email"));

                        consulta.setId(object.getInt("id"));
                        consulta.setTitle(object.getString("title"));
                        consulta.setDate(stringToDate(object.getString("date"), "yyyy-MM-dd'T'HH:mm"));
                        consulta.setStatus(object.getString("status"));
                        consulta.setProfissional(profissional);
                        consulta.setPaciente(paciente);
                        consulta.setObs(object.getString("obs"));

                        consultas.add(consulta);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tv_empty.setVisibility(View.GONE);
                    recyclerConsultation.setVisibility(View.VISIBLE);
                }
                consultaAdapter.notifyDataSetChanged();
                ;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                isLoading(false);

                if(networkResponse == null){
                    Log.e("ErrorHttpResponse", error.getClass().toString());
                }else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
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
        FragmentConsultation.requestStarted = y;
        if(y){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
