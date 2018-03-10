package io.mvx.hhnio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class HTTPSession {
    private SocketChannel clientChannel;
    private final int bufferCapacity = 2048;
    private final ByteBuffer buffer = ByteBuffer.allocate(bufferCapacity);
    private CharsetEncoder charsetEncoder = Charset.forName("UTF-8").newEncoder();

    HTTPSession(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    String read() throws IOException {
        buffer.limit(bufferCapacity);

        int bytesRead = clientChannel.read(buffer);
        if (bytesRead == -1) {
            // Connection is closed.
            throw new IOException();
        }

        buffer.flip();

        StringBuilder stringBuilder = new StringBuilder();
        while(buffer.hasRemaining()){
            stringBuilder.append((char) buffer.get());
        }

        buffer.clear();
        return stringBuilder.toString().trim();
    }

    private ByteBuffer stringToByteBuffer(String string) throws IOException {
        return charsetEncoder.encode(CharBuffer.wrap(string));
    }

    void write(HTTPResponse httpResponse) {
        final String lineBreak = "\r\n";
        String response = httpResponse.getHttpVersion() + " " + httpResponse.getStatusCode().getCode() + lineBreak +
                          "Content-type: " + httpResponse.getContentType().toString() + lineBreak +
                          lineBreak;
        try {
            clientChannel.write(stringToByteBuffer(response));
            if (httpResponse.getStatusCode() == HTTPResponseStatusCode.OK) {
                clientChannel.write(httpResponse.getFileContent());
            } else {
                clientChannel.write(stringToByteBuffer(httpResponse.getStatusCode().toString()));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void close() throws IOException {
        clientChannel.close();
    }
}
