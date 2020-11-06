package com.sidoso.paciente.interfaces;

import com.sidoso.paciente.listener.MessageListener;

public interface IMessage {
    public void notify(MessageListener listener);
}
