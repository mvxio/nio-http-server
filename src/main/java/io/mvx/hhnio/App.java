package io.mvx.hhnio;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App
{
    public static void main (String[] args) {
        try {
            HTTPServer server = new HTTPServer(new InetSocketAddress(1234));
            server.start();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
