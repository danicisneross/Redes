package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    private String TypeFile(File file){
        String nombre = file.getName();
        if(nombre.endsWith(".html")){
            return "text/html";
        } else if (nombre.endsWith(".txt")) {
            return "text/plain";
        } else if (nombre.endsWith(".gif")) {
            return "image/gif";
        } else if (nombre.endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream";
        }
    }

    private void procesarMetodos(String metodo, PrintWriter sOutput, String archivo, BufferedOutputStream output, String ifModified) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/p1-files" + archivo);
        String respuesta = "200 OK";
        boolean enviar;

        if(!file.exists() || file.isDirectory()){
            respuesta = "404 Not Found";
            file = new File(System.getProperty("user.dir") + "/p1-files/error404.html");
        }

        if (metodo.equals("GET")){
            enviar = true;
            if (ifModified != null){
                String dateOfMofificated = new SimpleDateFormat("EEE dd MMM HH:mm:ss z yyyy", new Locale("US")).format(file.lastModified());
                if (ifModified.equals("If-Modified-Since: " + dateOfMofificated)){
                    respuesta = "304 Not Modified";
                    enviar = false;
                }
            }
        } else if(metodo.equals("HEAD")){
            enviar = false;
        } else {
            respuesta = "400 Bad Request";
            file = new File(System.getProperty("user.dir") + "/p1-files/error400.html");
            enviar = true;
        }
        sOutput.println("HTTP/1.0 " + respuesta + "\n" +
                "Date: " + new SimpleDateFormat("EEE dd MMM HH:mm:ss z yyyy",
                new Locale("US")).format(new Date()) + "\n" +
                "Sever: WebServer_RD" + "\n" +
                "Content-Length: " + file.length() + "\n" +
                "Content-Type: " + TypeFile(file) + "\n" +
                "Last-Modified: " + new SimpleDateFormat("EEE dd MMM HH:mm:ss z yyyy",
                new Locale("US")).format(file.lastModified()) + "\n");
        sOutput.flush();

        if(enviar){
            long sizeFile = file.length();
            byte[] array = new byte[(int) sizeFile];
            int leido;
            FileInputStream input = new FileInputStream(file); //leemos el archivo
            BufferedInputStream inputStream = new BufferedInputStream(input);

            leido = inputStream.read(array, 0, (int) sizeFile); //leemos el archivo en el array de bytes
            output.write(array, 0, leido); //se escribe el contenido en el flujo de salida
            output.flush(); //enviamos al destino el contenido del bufer del flujo de salida

            //Close streams
            inputStream.close();
            input.close();
        }
    }

    public void run() {
        try {
            // This code processes HTTP requests and generate HTTP responses

            // Set the input channel
            BufferedReader sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Set the output channel
            PrintWriter sOutput = new PrintWriter(socket.getOutputStream(), true);
            BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

            String received = sInput.readLine();
            if(received != null){
                System.out.println("Peticion HTTP recibida de: " + socket.getInetAddress() + "\n" + received);
                String [] espacio = received.split(" "); //separamos received en subcadenas
                String ifModified = null;
                while (!received.equals("")){
                    received = sInput.readLine();
                    if(received.contains("If-Modified-Since")){
                        ifModified = received;
                    }
                    System.out.println(received);
                }
                procesarMetodos(espacio[0], sOutput, espacio[1], outputStream, ifModified);
            }
            //Close Streams
            outputStream.close();
            sInput.close();
            sOutput.close();

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close the client socket
            try{
                if(socket != null){
                    socket.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
