
package com.onner.socket;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {        
     try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado. Esperando conexión...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado.");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Leer el mensaje del cliente
            String messageFromClient = in.readLine();
            System.out.println("Mensaje del cliente: " + messageFromClient);

            // Responder al cliente
            out.println("Hola desde el servidor");

        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
   
}
