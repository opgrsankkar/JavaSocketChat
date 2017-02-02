import java.io.*;
import java.net.*;

public class Client {
    static Socket clientSocket;
    public static void main(String[] args) throws Exception{
        clientSocket = new Socket("",6789);
        Thread tIncoming = new Thread(new Incoming());
        Thread tOutgoing = new Thread(new Outgoing());
        tIncoming.start();
        tOutgoing.start();
    }
}

class Incoming implements Runnable {
    public void run() {
        String msgFromServer="";
        try {
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(Client.clientSocket.getInputStream()));
            while (true) {
                msgFromServer = inFromServer.readLine();
                System.out.println(msgFromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Outgoing implements Runnable {
    public void run() {
        String msgToServer="";
        try{
            DataOutputStream outToServer = new DataOutputStream(Client.clientSocket.getOutputStream());
            BufferedReader inFromUser = new BufferedReader(
                    new InputStreamReader(System.in));
            while(true){
                msgToServer = inFromUser.readLine();
                outToServer.writeBytes(msgToServer + "\n");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}