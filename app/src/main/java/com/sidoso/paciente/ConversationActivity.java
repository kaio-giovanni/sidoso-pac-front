package com.sidoso.paciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sidoso.paciente.adapter.MessageAdapter;
import com.sidoso.paciente.dao.MessageDAO;
import com.sidoso.paciente.interfaces.IMessage;
import com.sidoso.paciente.listener.MessageListener;
import com.sidoso.paciente.model.Message;
import com.sidoso.paciente.model.Paciente;
import com.sidoso.paciente.model.Profissao;
import com.sidoso.paciente.model.Profissional;
import com.sidoso.paciente.websocket.WebSocket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ConversationActivity extends AppCompatActivity  implements IMessage {

    private Toolbar toolbar;
    private ImageButton btnSendMsg;
    private EditText etMsg;
    private RecyclerView recyclerMessage;
    private Profissional profissional;
    private MessageListener messageListener;
    private MessageAdapter messageAdapter;
    private MessageDAO messageDAO;
    private Paciente user;
    private static List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Intent intent = getIntent();

        profissional = new Profissional();
        profissional.setId(intent.getIntExtra("profId", 0));
        profissional.setName(intent.getStringExtra("profName"));
        profissional.setEmail(intent.getStringExtra("profEmail"));
        profissional.setBirth(intent.getStringExtra("profBirth"));
        profissional.setPhoneMain(intent.getStringExtra("profPhoneMain"));
        profissional.setCpf(intent.getStringExtra("profCpf"));
        profissional.setProfissao(new Profissao(intent.getIntExtra("profissaoId", 0),
                intent.getStringExtra("profissaoName")));

        user = new Paciente();
        user.setId(intent.getIntExtra("pacienteId", 0));
        user.setEmail(intent.getStringExtra("pacienteEmail"));

        toolbar = (Toolbar) findViewById(R.id.tollbarConversation);
        toolbar.setTitle(profissional.getName());
        toolbar.setSubtitle(profissional.getProfissao().getName());
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        etMsg = (EditText) findViewById(R.id.et_conversation);

        btnSendMsg = (ImageButton) findViewById(R.id.btn_conversation);

        recyclerMessage = (RecyclerView) findViewById(R.id.rv_conversation);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMessage.setLayoutManager(layoutManager);

        messageDAO = new MessageDAO(getApplicationContext());

        messages = getMessagesByReceptorId(profissional.getId());

        messageAdapter = new MessageAdapter(messages, user);

        recyclerMessage.setAdapter(messageAdapter);

        messageListener = new MessageListener();
        messageListener.addListener(this);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = etMsg.getText().toString();
                Calendar currentDate = Calendar.getInstance();
                if(!(msg == "" || msg == null)){
                    Message txtMsg = new Message(user.getId(),
                            profissional.getId(),
                            profissional.getEmail(),
                            msg,
                            currentDate.get(Calendar.HOUR_OF_DAY) + ":" +
                            currentDate.get(Calendar.MINUTE));

                    WebSocket.sendMessage(txtMsg);
                    saveMessage(txtMsg);

                    etMsg.setText("");

                    scrollingRecyclerView();
                }
            }
        });
        scrollingRecyclerView();
    }

    private void scrollingRecyclerView(){
        recyclerMessage.scrollToPosition(messages.size() - 1);
    }

    @Override
    public void notify(MessageListener listener) {
        Message message = listener.getMessage();
        saveMessage(message);
        scrollingRecyclerView();
    }

    private void refreshMessageAdapter(){
        // execute in main thread (UI)
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                messageAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<Message> getMessagesByReceptorId(int receptorId){
        try {
            return messageDAO.findMessages(receptorId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Message>();
        }
    }

    private void saveMessage(Message msg){
        messages.add(msg);
        refreshMessageAdapter();
        try {
            messageDAO.insert(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}