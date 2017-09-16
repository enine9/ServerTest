package example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import static example.HttpServer.closeServer;

public class ListenThread extends Thread{
    ServerSocket serverSocketThis;
    Thread receiveThread = null;
    Socket socket = null;

    public ListenThread(ServerSocket serverSocket){
        this.serverSocketThis = serverSocket;
    }

    @Override
    public void run() {
        while (!closeServer){
            try {
                socket = serverSocketThis.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!closeServer){
                receiveThread = new Thread(new serverThread(socket));
                receiveThread.start();
            }
            System.out.println("ListenThread is closed" + new Date().getTime());
        }
    }

}

