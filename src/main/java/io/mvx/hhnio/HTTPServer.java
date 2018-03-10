package io.mvx.hhnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class HTTPServer {
    private final HTTPRequestHandler httpRequestHandler = new HTTPRequestHandler();
    private final Selector selector = Selector.open();
    private final ServerSocketChannel serverChannel = ServerSocketChannel.open();
    private boolean running = true;

    private void processSelectionKey(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }

        if (key.isAcceptable()) {
            SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            try {
                // Get SocketChannel.
                SocketChannel clientChannel = (SocketChannel) key.channel();
                // Create HTTPSession object and attach it to the key.
                if (!(key.attachment() instanceof HTTPSession)) {
                    key.attach(new HTTPSession(clientChannel));
                }
                // Get http session.
                HTTPSession httpSession = (HTTPSession) key.attachment();
                // Read request string.
                String requestString = httpSession.read();
                HTTPRequest httpRequest = new HTTPRequest(requestString);
                HTTPResponse httpResponse = httpRequestHandler.processRequest(httpRequest);
                httpSession.write(httpResponse);

            } finally {
                // Close http session.
                if (key.attachment() instanceof HTTPSession) {
                    HTTPSession httpSession = (HTTPSession) key.attachment();
                    httpSession.close();
                }
            }
        }
    }

    HTTPServer(InetSocketAddress address) throws IOException {
        serverChannel.socket().bind(address);
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("HTTPServer running on port " + address.getPort());
    }

    private void shutdown() {
        running = false;
        try {
            selector.close();
            serverChannel.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void start() {
        while (running) {
            try {
                selector.selectNow();
                Iterator<SelectionKey> selectedKeysIterator = selector.selectedKeys().iterator();

                while (selectedKeysIterator.hasNext()) {
                    SelectionKey key = selectedKeysIterator.next();
                    selectedKeysIterator.remove();
                    processSelectionKey(key);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
                shutdown();
            }
        }
    }
}
