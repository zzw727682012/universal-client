package com.universal.client.http;

import com.universal.client.core.MessageDecode;
import com.universal.client.core.MessageEncode;
import com.universal.client.core.MessageHandler;
import com.universal.client.core.Protocol;

import java.io.IOException;
import java.text.ParseException;

public class HttpProtocol implements Protocol {

    private String protocolName;
    private MessageDecode messageDecode;
    private MessageEncode messageEncode;
    private MessageHandler messageHandler;

    public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {

    }

    @Override
    public String getProtocolName() {
        return protocolName;
    }

    @Override
    public MessageDecode getMessageDecode() {
        return messageDecode;
    }

    @Override
    public MessageEncode getMessageEncode() {
        return messageEncode;
    }

    @Override
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public void init(String protocolName) {

    }
}
