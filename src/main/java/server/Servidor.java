package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Servidor {
    private static final int PUERTO = 6400;
    private static final int PUNTOS_PARA_GANAR = 4;
    private static final int INTERVALO_CAMBIO = 3000; // 3 segundos
    private static List<ManejadorCliente> clientes = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(2);
    private static Random random = new Random();
    private static char letraActual;
    private static boolean juegoEnCurso = false;

    public static void main(String[] args) throws Exception {
        System.out.println("Servidor iniciando...");
        ServerSocket listener = new ServerSocket(PUERTO);

        try {
            while (clientes.size() < 2) {
                Socket socket = listener.accept();
                ManejadorCliente manejadorCliente = new ManejadorCliente(socket);
                clientes.add(manejadorCliente);
                pool.execute(manejadorCliente);
            }
            System.out.println("Iniciando Juego");
            iniciarJuego();

        } finally {
            listener.close();
            pool.shutdown();
        }
    }

    private static void iniciarJuego() {
        juegoEnCurso = true;
        new Thread(() -> {
            while (juegoEnCurso) {
                cambiarLetra();
                try {
                    Thread.sleep(INTERVALO_CAMBIO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void cambiarLetra() {
        letraActual = (char) ('A' + random.nextInt(26));
        broadcast("PINTAR " + letraActual);
    }

    private static void broadcast(String mensaje) {
        for (ManejadorCliente cliente : clientes) {
            cliente.enviar(mensaje);
        }
    }

    private static class ManejadorCliente implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private int puntos = 0;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String entrada = in.readLine();
                    if (entrada.equals(String.valueOf(letraActual))) {
                        puntos++;
                        broadcast("PUNTOS " + clientes.indexOf(this) + " " + puntos);
                        if (puntos >= PUNTOS_PARA_GANAR) {
                            broadcast("FIN " + clientes.indexOf(this));
                            juegoEnCurso = false;
                            break;
                        }
                        cambiarLetra();
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                try { socket.close(); } catch (IOException e) {}
            }
        }

        public void enviar(String mensaje) {
            out.println(mensaje);
        }
    }
}