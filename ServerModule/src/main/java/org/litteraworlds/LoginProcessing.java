package org.litteraworlds;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class LoginProcessing implements Runnable {

    private Socket client;

    private InputStream in;

    private OutputStream out;

    private BufferedWriter bufferedOut;


    public LoginProcessing(Socket client){
        this.client = client;
    }



    @Override
    public void run() {
        try{
            this.in = client.getInputStream();
            this.out = client.getOutputStream();

            BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(in));
            this.bufferedOut = new BufferedWriter(new OutputStreamWriter(out));

            System.out.println("[CLIENT_PROCESSING] Client "+client.getInetAddress()+" connected");

            ArrayList<String> bufferLoginHistory = ServerController.loginHistory;


            while(!client.isClosed()){
                String loginFromClient = bufferedIn.readLine();

                ServerController.loginHistory.add("[CLIENT]"+client.getInetAddress()+"|>"+"try to login with username: "+ loginFromClient);


            }
            client.close();
            System.out.println("[CLIENT_PROCESSING] Client "+client.getInetAddress()+" is disconnected");

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                in.close();
                out.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }



}
