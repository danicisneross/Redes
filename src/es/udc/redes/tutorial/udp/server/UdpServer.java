package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket socket = null;
        try {
            // Create a server socket
            socket = new DatagramSocket(Integer.parseInt(argv[0]));

            // Set maximum timeout to 300 secs
            socket.setSoTimeout(300000);

            while (true) {
                // Prepare datagram for reception
                byte[] array = new byte[1024];
                DatagramPacket dgramRec = new DatagramPacket(array, array.length);

                // Receive the message
                socket.receive(dgramRec);
                System.out.println("SERVER: Received "
                        + new String(dgramRec.getData(), 0, dgramRec.getLength())
                        + " from " + dgramRec.getAddress().toString() + ":"
                        + dgramRec.getPort());

                // Prepare datagram to send response
                DatagramPacket dgramSent = new DatagramPacket(array, dgramRec.getLength(), dgramRec.getAddress(), dgramRec.getPort());

                // Send response
                socket.send(dgramSent);
                System.out.println("SERVER: Sending "
                        + new String(dgramRec.getData(), 0, dgramRec.getLength())
                        + " from " + dgramRec.getAddress().toString() + ":"
                        + dgramRec.getPort());
            }
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // Close the socket
            if (socket != null){
                socket.close();
            }
        }
    }
}
