package com.universal.client.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;
import java.util.concurrent.*;

public class SimpleHttpServer implements Runnable {

    private final int port;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 50, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public SimpleHttpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.info("create server on port:{}", port);

            while (true) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(10);
                threadPool.execute(new SocketProcessor(socket));

            }
        } catch (IOException e) {
            logger.error("create server error", e);
        }
    }


    private class SocketProcessor implements Runnable {
        private Logger logger = LoggerFactory.getLogger(this.getClass());

        private Socket socket;

        public SocketProcessor(Socket socket) throws UnsupportedEncodingException {
            logger.info("received socket {}", socket.toString());
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            OutputStream raw = null;
            try {
                raw = new BufferedOutputStream(socket.getOutputStream());
                Writer out = new OutputStreamWriter(raw);

                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder requestLine = new StringBuilder();

                char[] buf = new char[8192];
                while (true) {
                    int c = reader.read(buf);
                    if (c == -1) {
                        break;
                    }
                    requestLine.append(buf).append("\r\n");
                }

                String request = requestLine.toString();
                logger.info(socket.getRemoteSocketAddress() + " " + request);

                String[] tokens = request.split("\\s+");
                String method = tokens[0];
                String version = tokens[2];
                if (method.equals("GET")) {
                    String fileName = tokens[1];
                    String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                    String response = "ZZWQOQ";
                    sendHeader(out, "HTTP/1.1 200 OK", contentType, response.length());
                    out.write(response);
                    out.flush();
                } else { // method does not equal "GET"
                    String body = new StringBuilder("<HTML>\r\n").append("<HEAD><TITLE>Not Implemented</TITLE>\r\n").append("</HEAD>\r\n").append("<BODY>").append("<H1>HTTP Error 501: Not Implemented</H1>\r\n").append("</BODY></HTML>\r\n").toString();
                    if (version.startsWith("HTTP/")) { // send a MIME header
                        sendHeader(out, "HTTP/1.1 501 Not Implemented", "text/html; charset=utf-8", body.length());
                    }
                    out.write(body);
                }
            } catch (IOException e) {
                logger.error("Socket error", e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void sendHeader(Writer out, String responseCode, String contentType, int length) throws IOException {
            out.write(responseCode + "\r\n");
            Date now = new Date();
            out.write("Date: " + now + "\r\n");
            out.write("Server: SimpleHttpServer 2.0\r\n");
            out.write("Content-length: " + length + "\r\n");
            out.write("Content-type: " + contentType + "\r\n\r\n");
            out.flush();
        }

    }
}
