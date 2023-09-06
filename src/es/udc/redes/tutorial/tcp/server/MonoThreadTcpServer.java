package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket serverSocket;
        Socket socket = null;
        try {
            // Create a server socket
            serverSocket = new ServerSocket(Integer.parseInt(argv[0]));

            // Set a timeout of 300 secs
            serverSocket.setSoTimeout(300000);

            while (true) {
                // Wait for connections
                socket = serverSocket.accept();

                // Set the input channel
                BufferedReader sIpunt = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Set the output channel
                PrintWriter sOutput = new PrintWriter(socket.getOutputStream(), true);

                // Receive the client message
                String received = sIpunt.readLine();
                System.out.println("SERVER: Received " + received
                        + " from " + socket.getInetAddress().toString()
                        + ":" + socket.getPort());

                // Send response to the client
                sOutput.println(received);
                System.out.println("SERVER: Sending " + received +
                        " to " + socket.getInetAddress().toString() +
                        ":" + socket.getPort());

                // Close the streams
                sOutput.close();
                sIpunt.close();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
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
