package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer {
    
    public static void main(String[] args) {
        if(args.length != 1){
            System.err.println("The format is not correct" + "\n" + "Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }
        ServerSocket serverSocket;
        Socket socket = null;

        try{
            // Create a server socket
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));

            // Set a timeout of 300 secs
            serverSocket.setSoTimeout(300000);

            while (true) {
                // Wait for connections
                socket = serverSocket.accept();

                // Create a ServerThread object, with the new connection as parameter
               ServerThread serverThread = new ServerThread(socket);

                // Initiate thread using the start() method
                serverThread.start();
            }
        } catch (SocketTimeoutException e){
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //Close the socket
            try {
                if (socket != null){
                    socket.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
