package com.onner.socket;

import java.io.*;
import java.net.*;

public class Cliente {
public static void main(String[] args) {
        try (Socket socket = new Socket("200.200.3.35", 12345); // Reemplaza con la IP del servidor
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
             
            // Enviar un mensaje al servidor
            out.println("Hola desde el cliente");

            // Leer la respuesta del servidor
            String messageFromServer = in.readLine();
            System.out.println("Mensaje del servidor: " + messageFromServer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
