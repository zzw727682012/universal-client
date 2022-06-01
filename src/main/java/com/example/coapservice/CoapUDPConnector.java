//package com.example.coapservice;
//package com.com.example.coapservice;
//
//import com.com.example.comment.AddressUtil;
//import com.com.example.core.Connector;
//
//
//
//
//import org.eclipse.californium.core.CoapServer;
//import org.eclipse.californium.core.network.CoapEndpoint;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
//import java.net.InetSocketAddress;
//
//@Component
//public class CoapUDPConnector implements Connector {
//    private final static Logger LOGGER = LoggerFactory.getLogger(CoapUDPConnector.class);
//    private CoapServer coapServer;
//    private CoapEndpoint coapEndpoint;
//    private InetSocketAddress inetSocketAddress;
//    @Autowired
//    private CoapProtocol coapProtocol;
//    private boolean isStart;
//
//    public void start() {
//        if (isStart) {
//            LOGGER.info("coap over UDP connector is start");
//            return;
//        }
//        coapServer = new CoapServer();
//        inetSocketAddress = new InetSocketAddress(AddressUtil.getCurrentAddress(), coapProtocol.getCoapProtocolConfig().getPort());
//        coapEndpoint = new  CoapEndpoint.Builder().setInetSocketAddress(inetSocketAddress)
//    }
//
//
//    @Override
//    public void stop() {
//
//    }
//
//
//    public void send() {
//
//    }
//}
//
