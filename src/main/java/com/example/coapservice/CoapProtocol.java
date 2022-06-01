//package com.example.coapservice;
//
//import com.universal.client.core.Message;
//import com.universal.client.core.MessageHandlerFactory;
//import com.universal.client.protocol.Protocol;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CoapProtocol implements Protocol {
//
//    @Autowired
//    private CoapProtocolConfig coapProtocolConfig;
//
//    public String getProtocolName() {
//        return null;
//    }
//
//    public MessageHandlerFactory getMessageHandlerFactory(Message message) {
//        return null;
//    }
//
//    @Override
//    public RawDataInterceptor getRawDataInterceptor() {
//        return null;
//    }
//
//    public CoapProtocolConfig getCoapProtocolConfig() {
//        return coapProtocolConfig;
//    }
//
//    public void setCoapProtocolConfig(CoapProtocolConfig coapProtocolConfig) {
//        this.coapProtocolConfig = coapProtocolConfig;
//    }
//}
