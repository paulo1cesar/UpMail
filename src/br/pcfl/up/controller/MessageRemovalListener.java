package br.pcfl.up.controller;

import br.pcfl.up.mail.Message;

public interface MessageRemovalListener {
    public void messageRemoved(Message message);
}
