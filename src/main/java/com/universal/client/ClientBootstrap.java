package com.universal.client;


import com.universal.client.server.SimpleHttpNIOServer;
import com.universal.client.server.SimpleHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientBootstrap {

    private static Logger logger = LoggerFactory.getLogger(ClientBootstrap.class);
    public static void main(String[] args) {
        logger.info("aaaaaaaaaaaaaaaaaaaaaaaa");
        SimpleHttpNIOServer server = new SimpleHttpNIOServer(8080);
        Thread thread = new Thread(server);
        thread.start();

        SimpleHttpServer server1 = new SimpleHttpServer(8081);
        Thread thread1 = new Thread(server1);
        thread1.start();

    }


}
