package com.universal.client.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.*;

public class SimpleHttpNIOServer implements Runnable {

    private final int port;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 50, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public SimpleHttpNIOServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            logger.info("create server on port:{}", port);

            Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select() <= 0) {
                    continue;
                }
                // 直到 Selector 上注册了感兴趣的事件（此处是ACCEPT事件）
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                // 获取到每一个连接，交由线程池处理，传递连接的KEY
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    threadPool.execute(new RequestProcessor(key));
                    logger.error("threadPool {}", threadPool.toString());
                }

            }
        } catch (IOException e) {
            logger.error("create server error", e);
        }
    }


    private class RequestProcessor implements Runnable {
        private Logger logger = LoggerFactory.getLogger(this.getClass());

        private File rootDirectory;
        private String indexFileName = "index.html";
        private SocketChannel socketChannel;
        private byte[] content;
        private byte[] header;
        // 服务端传递的连接KEY
        private SelectionKey key;
        // 注意：此处的Selector不是上面服务端的Selector，而是针对于每个连接自身的Selector
        // 当连接建立后（ACCEPT完成），用于 客户端连接 的读写事件注册，并轮询这个 Selector 处理
        private Selector selector;


        public RequestProcessor(SelectionKey key) throws UnsupportedEncodingException {
            logger.info("create Socket {}", key);
            this.socketChannel = socketChannel;
            this.key = key;

        }

        private void doAccept() throws IOException {
            // 新连接进来，当前类的 Selector 属性为 null
            if (selector == null) {

            }
        }

        @Override
        public void run() {
            try {
                if (key.isAcceptable()) {
                    handleAccetable(key, selector);
                }
                if (key.isReadable()) {
                    handleReadable(key);
                    // 重要！！ 将SelectionKey切换为写模式
                    key.interestOps(SelectionKey.OP_WRITE);
                }
                if (key.isWritable()) {
                    handleWritable(key);
                    // 处理完响应操作，才能关掉客户端的SocketChannel
                    key.channel().close();
                }
            } catch (Exception ex) {
                key.cancel();
            }
        }

        private void handleAccetable(SelectionKey key, Selector selector) throws IOException {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            InetSocketAddress socketAddress = (InetSocketAddress) client.getRemoteAddress();
            String ip = socketAddress.getAddress().getHostAddress();
            logger.info("Accepted connection from " + ip + ":" + socketAddress.getPort());
            client.configureBlocking(false);
            // 只需要注册READ操作
            SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            clientKey.attach(byteBuffer);
        }

        private void handleReadable(SelectionKey key) throws IOException {
            logger.info("handleReadable");

            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
            byteBuffer.rewind();
            int n = client.read(byteBuffer);
            if (n < 1) {
                return;
            }
            // 打印HTTP请求报文出来
            byteBuffer.flip();
            byte[] array = byteBuffer.array();
            String str = new String(array, 0, n);
            logger.info("Receive request from client:----------------");
            logger.info(str);
        }

        private void handleWritable(SelectionKey key) throws IOException {
            logger.info("handleWritable");
            SocketChannel client = (SocketChannel) key.channel();
            client.configureBlocking(false);
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();


            StringBuilder builder = new StringBuilder();
            builder.append("HTTP/1.1 200 OK" + "\r\n");
            Date now = new Date();
            builder.append("Date: " + now + "\r\n");
            builder.append("Server: SimpleHttpServer 2.0\r\n");
            builder.append("Content-length: " + "ZZW Hey Client".length() + "\r\n");
            builder.append("Content-type: " + "application/json" + "\r\n\r\n");
            builder.append("ZZW Hey Client");
            byteBuffer.put(builder.toString().getBytes());
            byteBuffer.flip();
            // 输出HTTP响应报文
            client.write(byteBuffer);
        }


//        @Override
//        public void run() {
//            try {
//                // 处理Accept事件，与客户端建立连接
//                // 打开当前连接专属的 Selector
//                selector = Selector.open();
//                // doAccept
//                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
//                // 建立连接，获取客户端通道 SocketChannel
//                SocketChannel accept = serverSocketChannel.accept();
//                accept.configureBlocking(false);
//
//                // 为客户端连接注册读事件（READ）
//                accept.register(selector, SelectionKey.OP_READ);
//                // 移除掉 服务端Selector 中传递的当前的KEY，因为已经建立了连接
//                key.selector().selectedKeys().remove(key);
//
//                logger.info("Server Accepted selector{}, SelectionKey{}", selector, key);
//
//                // 开启客户端连接 Selector 轮询
//                A:while (true) {
//                    // 没有感兴趣的事件则 continue；
//                    if (selector.select() <= 0) {
//                        continue;
//                    }
//                    // 获取到感兴趣的事件，如 READ
//                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//                    while (iterator.hasNext()) {
//                        SelectionKey selectionKey = iterator.next();
//                        // 这一步是防止客户端断开连接后，没有正确关闭Socket导致服务端报错
//                        this.key = selectionKey;
//
//                        // 可读
//                        if (selectionKey.isReadable()) {
//                            // 获取到客户端 SocketChannel， 执行读操作
//                            SocketChannel channel = (SocketChannel) selectionKey.channel();
//                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//                            while (channel.read(byteBuffer) > 0) {
//                                String data=new String(byteBuffer.array(),"UTF-8");
//                                logger.info("data:{}", data);
//                                byteBuffer.flip();
//                            }
//                            // 读完后去除当前KEY，并重新注册写事件
//                            selector.selectedKeys().remove(selectionKey);
//                            channel.register(selector, SelectionKey.OP_WRITE);
//                        } else if (selectionKey.isWritable()) { // 可写
//
//                            // 获取客户端连接通道 SocketChannel
//                            SocketChannel channel = (SocketChannel) selectionKey.channel();
//                            // 执行写操作，而后去除当前KEY
//                            StringBuilder builder=new StringBuilder();
//                            builder.append("HTTP/1.1 200 OK"+ "\r\n");
//                            Date now = new Date();
//                            builder.append("Date: " + now + "\r\n");
//                            builder.append("Server: SimpleHttpServer 2.0\r\n");
//                            builder.append("Content-length: " + "ZZW Hey Client".length() + "\r\n");
//                            builder.append("Content-type: " + "application/json" + "\r\n\r\n");
//                            builder.append("ZZW Hey Client");
//                            channel.write(ByteBuffer.wrap(builder.toString().getBytes()));
//                            selector.selectedKeys().remove(selectionKey);
//
//                            // 这里默认写操作后释放客户端连接，当然也可以重新注册别的事件
//                            selectionKey.cancel();
//                            channel.socket().close();
//                            channel.close();
//                            accept.close();
//                            break A;
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                // 由于key值在每次循环都有赋值，所以当客户端断开连接后，捕获到异常后正确关闭Socket
//                key.cancel();
//                System.err.println("与客户端断开连接");
//            }
//        }

    }
}
