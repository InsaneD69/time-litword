package org.litteraworlds;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ServerListener {

    ArrayList<LoginProcessing> clientProcessingList = new ArrayList<>();

    public void startServerListener(){
        try(ServerSocket serverSocket = new ServerSocket(27015)){

            System.out.println("[SERVER_LISTENER] Started on port "+serverSocket.getLocalPort());

            ExecutorService instances = Executors.newCachedThreadPool();

            System.out.println("[SERVER_LISTENER] Starting listen on port "+serverSocket.getLocalPort());


            while(!serverSocket.isClosed()){
                Socket client = serverSocket.accept();
                LoginProcessing clientProcessing = new LoginProcessing(client);
                instances.execute(clientProcessing);
                clientProcessingList.add(clientProcessing);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }



}
