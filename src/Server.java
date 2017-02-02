import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    static ServerSocket welcomeSocket;
    static ArrayList<Socket> clientList = new ArrayList<Socket>();
    static ArrayList<Thread> tIncoming = new ArrayList<Thread>();
    static Thread mOutgoing = new Thread(new ServerOutgoing());

    public static void main(String[] args) throws Exception {
        welcomeSocket = new ServerSocket(6789);
        try {
            mOutgoing.start();
            while (true) {
                Socket s = Server.welcomeSocket.accept();
                clientList.add(s);
                tIncoming.add(new Thread(new ServerIncoming(clientList.size() - 1)));
                tIncoming.get(clientList.size() - 1).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerIncoming implements Runnable {
    static int clientNumber = 0;

    public ServerIncoming(int i) {
        clientNumber = i;
    }

    public void run() {
        String msgFromClient = "";
        try {
            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader((Server.clientList.get(clientNumber)).getInputStream()));
            while (true) {
                msgFromClient = inFromClient.readLine();
                System.out.println("Client" + clientNumber + ": " + msgFromClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerOutgoing implements Runnable {
    static int clientNumber = 0;

    public void run() {
        String msgToClient = "";
        DataOutputStream outToClient;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        try {
            msgToClient = inFromUser.readLine();
            for (int i=0;i<Server.clientList.size();i++){
                outToClient = new DataOutputStream(Server.clientList.get(i).getOutputStream());
                outToClient.writeBytes("Server: " + msgToClient+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}