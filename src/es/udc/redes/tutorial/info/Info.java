package es.udc.redes.tutorial.info;

import java.io.File;
import java.text.SimpleDateFormat;

public class Info {

    public static void main(String[] args) {
        File pathArchivo;
        if(args.length == 1){
            pathArchivo = new File(args[0]);
            if(pathArchivo.exists()) {
                String name = pathArchivo.getName();
                String ext = name.substring(name.lastIndexOf(".") +1);
                SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                System.out.println("Information of file: ");
                System.out.println("Name: " + name);
                System.out.println("Size: " + pathArchivo.length());
                System.out.println("Date of last modification: " + formatDate.format(pathArchivo.lastModified()));
                System.out.println("Extension: " + ext);
                System.out.println("Absolute path: " + pathArchivo.getAbsolutePath());

            } else {
                System.out.println("File don't exist");
            }
        } else {
            System.out.println("Format:  es.udc.redes.tutorial.info.Info <ruta relativa>");
        }
    }
}
