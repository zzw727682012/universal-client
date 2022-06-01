package com.example.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(AddressUtil.class);

    public static String getCurrentAddress() {

        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("get ip address error");
        }
        return null;
    }
}
