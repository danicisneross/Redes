package es.udc.redes.tutorial.copy;

import java.io.*;

public class Copy {

    public static void main(String[] args) throws IOException {
        Copy copy;
        if(args.length == 2) {
            copy = new Copy();
            copy.copyFile(args);
            System.out.println("File " + args[0] + " copy in file " + args[1]);
        } else {
            System.out.println("Format:  es.udc.redes.tutorial.copy.Copy <fichero origen> <fichero destino>");
            System.exit(-1);
        }
    }
    public void copyFile(String[] file) throws IOException {
        BufferedInputStream input = null;       //Bufer de entrada para secuencias de bytes
        BufferedOutputStream output = null;     //Bufer de salidad para secuencias de bytes

        try {
            input = new BufferedInputStream(new FileInputStream(file[0]));      //encapsulamos el archivo de bytes en Bufer para optimizacion
            output = new BufferedOutputStream(new FileOutputStream(file[1]));
            int success;

            while ((success = input.read()) != -1) {
                output.write(success);
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if(input != null) {
                input.close();
            }
            if(output != null){
                output.close();
            }
        }
    }
}
