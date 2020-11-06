package com.sidoso.paciente.ui.professionals;

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
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sidoso.paciente.ConversationActivity;
import com.sidoso.paciente.R;
import com.sidoso.paciente.adapter.ChatAdapter;
import com.sidoso.paciente.http.VolleySingleton;
import com.sidoso.paciente.model.Profissao;
import com.sidoso.paciente.model.Profissional;
import com.sidoso.paciente.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.sidoso.paciente.config.Constants.API_URL;
import static com.sidoso.paciente.config.Constants.FILE_PREFERENCES;

public class FragmentProfessionals extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerChat;
    private ChatAdapter chatAdapter;
    private SharedPreferences mUserSaved;

    private static List<Profissional> profissionais = new ArrayList<>();
    private static boolean requestStarted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_professionals, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_professionals);
        progressBar.setVisibility(requestStarted == true ? View.VISIBLE : View.GONE);

        recyclerChat = (RecyclerView) view.findViewById(R.id.list_conversation);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerChat.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setHasFixedSize(true);

        mUserSaved = getContext().getSharedPreferences(FILE_PREFERENCES, MODE_PRIVATE);

        //get data from api
        if (profissionais.isEmpty() && !requestStarted){
            getProfissionais(mUserSaved.getInt("userId", 0), mUserSaved.getString("tokenApi", ""));
        }

        chatAdapter = new ChatAdapter(profissionais);
        recyclerChat.setAdapter(chatAdapter);

        recyclerChat.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                recyclerChat,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), ConversationActivity.class);
                        Profissional p = profissionais.get(position);
                        intent.putExtra("profId", p.getId());
                        intent.putExtra("profName", p.getName());
                        intent.putExtra("profBirth", p.getBirth());
                        intent.putExtra("profCpf", p.getCpf());
                        intent.putExtra("profPhoneMain", p.getPhoneMain());
                        intent.putExtra("profEmail", p.getEmail());

                        intent.putExtra("pacienteId", mUserSaved.getInt("userId", 0));
                        intent.putExtra("pacienteEmail", mUserSaved.getString("userEmail", ""));

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

    private void getProfissionais(int idUser, final String tokenApi){
        String url = API_URL.concat("paciente/"+idUser+"/profissionais/");

        isLoading(true);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Profissional p;
                isLoading(false);

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        JSONObject profissao = object.getJSONObject("profissao");
                        p = new Profissional(
                                object.getInt("id"),
                                object.getString("name"),
                                object.getString("photo"),
                                object.getString("birth"),
                                object.getString("cpf"),
                                object.getString("genre"),
                                object.getString("phone_main"),
                                object.getString("email"),
                                new Profissao(profissao.getInt("id"), profissao.getString("name"))
                        );
                        profissionais.add(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                chatAdapter.notifyDataSetChanged();
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

                        if(networkResponse.statusCode == 403){
                            //refresh token
                            Log.e("RefreshTokenAPI", "Token expired ".concat(response.getString("message")));
                            refreshTokenApi(API_URL.concat("login/paciente/"));
                        }
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

    private void refreshTokenApi(String url){
        Log.i("RefreshingTokenApi", "Update Token API");
        isLoading(true);

        String email = mUserSaved.getString("userEmail", "");
        String password = mUserSaved.getString("userPassword", "");

        JSONObject object;
        try{
            object = new JSONObject();
            object.put("email", email);
            object.put("password", password);
        }catch (JSONException e){
            object = null;
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                isLoading(false);
                SharedPreferences.Editor prefsEditor = mUserSaved.edit();
                try{
                    JSONObject headers = response.getJSONObject("headers");
                    prefsEditor.putString("tokenApi", headers.getString("Authorization"));

                    prefsEditor.commit();

                    getProfissionais(mUserSaved.getInt("userId", 0), headers.getString("Authorization"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading(false);
                NetworkResponse networkResponse = error.networkResponse;
                if(networkResponse == null){
                    Log.e("LoginError",error.getClass().toString());
                }else{
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
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try{
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonResponse, HttpHeaderParser.parseCacheHeaders(response));
                }catch (UnsupportedEncodingException e){
                    return Response.error(new ParseError(e));
                }catch(JSONException je){
                    return Response.error(new ParseError(je));
                }
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void isLoading(Boolean y){
        FragmentProfessionals.requestStarted = y;
        if(y){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
