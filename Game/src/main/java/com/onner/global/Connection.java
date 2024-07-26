package com.onner.global;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;

public class Connection {
    private final String ip = "192.168.1.2";
    private final int port = 6400;
    private static Socket clientSocket;
    private static PrintWriter printWriter;
    private static BufferedReader bufferedReader;

    public Connection() {
        initializeConnection();
    }

    private void initializeConnection() {
        try {
            clientSocket = new Socket(ip, port);
            printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Conectado al servidor...!!!");
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    public static void sendMessage(String message) {
        try {
            if (printWriter != null && !clientSocket.isClosed()) {
                printWriter.println(message);
                printWriter.flush(); // Asegúrate de que el mensaje se envíe inmediatamente
                System.out.println("Mensaje enviado: \"" + message + "\"");
            } else {
                System.err.println("El PrintWriter no está disponible o el socket está cerrado.");
            }
        } catch (Exception e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }


    public static String receiveMessage() {
        try {
            if (bufferedReader != null) {
                String message = bufferedReader.readLine();
                if (message != null) {
                    return message;
                } else {
                    System.err.println("El servidor ha cerrado la conexión.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al recibir datos del servidor: " + e.getMessage());
        }
        return null;
    }


    public static void closeConnection() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

