package client.models;

import client.controllers.ViewController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    private static final String AUTH_CMD_PREFIX = "/auth";
    private static final String AUTHOK_CMD_PREFIX = "/authok";
    private static final String AUTHERR_CMD_PREFIX = "/autherr";
    private static final String PRIVATE_MSG_CMD_PREFIX ="/w";
    private static final String CLIENT_MSG_CMD_PREFIX ="/clientMsg";
    private static final String SERVER_MSG_CMD_PREFIX ="/serverMsg";

    private static final String SERVER_ADRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    private final String host;
    private final int port;

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private Socket socket;

    private String username;

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public Network() {
        this(SERVER_ADRESS, SERVER_PORT);
    }

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.out.println("Соединение не было установлено!");
            e.printStackTrace();
            return false;
        }

    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitMessage(ViewController viewController) {

       Thread thread = new Thread( () -> {
           try { while (true) {

                   String message = dataInputStream.readUTF();
                   viewController.appendMessage(message);

           }
           } catch (IOException e) {
               e.printStackTrace();
               System.out.println("Соединение потеряно!");
           }
       });
       thread.setDaemon(true);
       thread.start();
    }


    public String sendAuthCommand(String login, String password) {
        try {
            dataOutputStream.writeUTF(String.format("%s %s %s", AUTH_CMD_PREFIX, login, password));
            String response = dataInputStream.readUTF();
            if(response.startsWith(AUTHOK_CMD_PREFIX)) {
                this.username = response.split("\\s+", 2)[1];
                return null;
            }
            else {
                return response.split("\\s+", 2)[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String getUsername() {
        return username;
    }
//    public void se3ndPrivateMessage(String message, String recepient)throws IOException{
//        String
//    }
}
