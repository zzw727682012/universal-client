package com.universal.client.core;


public interface Protocol {

    public String getProtocolName();

    public MessageDecode getMessageDecode();

    public MessageEncode getMessageEncode();

    public MessageHandler getMessageHandler();

    public void init(String protocolName);

}
