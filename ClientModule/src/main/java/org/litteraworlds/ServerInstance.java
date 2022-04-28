package org.litteraworlds;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServerInstance {


    private Socket client;

    private InputStream in;

    private OutputStream out;

    private String clIP;

    private static MessageDigest hashGen;

    public Socket getClient() {
        return client;
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public ServerInstance(Socket client) throws IOException {
        this.client = client;
        this.in = client.getInputStream();
        this.out = client.getOutputStream();

    }




}
