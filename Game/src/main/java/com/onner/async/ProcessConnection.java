package com.onner.async;
import com.onner.global.Connection;
import com.onner.global.Global;
import com.onner.global.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

public class ProcessConnection implements Runnable {
    private Player player;
    private static String namePlayer = "Player";
    private String response = "";

    public ProcessConnection() {
        new Connection();
        obtainName();
        createUsers();
        sendInitialData();
        initUser();
    }

    private void sendInitialData() {
        String playerData = serializePlayer(player);
        Connection.sendMessage(playerData);
    }

    @Override
    public void run() {
        try {
            while (!Global.stateOfPlay) {
                response = Connection.receiveMessage();
                if (response != null && !response.isEmpty()) {
                    if (response.startsWith("CONECTAR")) {
                        String jsonResponse = response.substring("CONECTAR".length()).trim();
                        if (jsonResponse.startsWith("[") && jsonResponse.endsWith("]")) {
                            processServerResponse(jsonResponse);
                        } else {
                            System.out.println("Respuesta CONECTAR no contiene un JSON válido: " + response);
                        }
                    }
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            System.err.println("Hilo interrumpido: " + ex.getMessage());
        } finally {
            System.out.println("Cerrando conexión");
            Connection.closeConnection();
        }
    }


    public void createUsers() {
        Global.id = UUID.randomUUID().toString().substring(0, 6);
        player = new Player(Global.id, namePlayer, 0);
    }

    public String serializePlayer(Player player) {
        return player.toJson();
    }

    public static void obtainName() {
        String name = JOptionPane.showInputDialog("Ingrese su nombre:");
        if (name == null || name.trim().isEmpty()) {
            name = "Player";
        }
        namePlayer = name;
    }

    private void initUser() {
        Global.players.add(player);
    }

    public void processServerResponse(String response) {
        if (response != null && !response.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                updatePlayers(jsonArray);
            } catch (Exception e) {
                System.err.println("Error al procesar la respuesta del servidor: " + e.getMessage());
            }
        }
    }


    private void updatePlayers(JSONArray jsonArray) {
        List<Player> updatedPlayers = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject playerJson = jsonArray.getJSONObject(i);
            String id = playerJson.optString("id", null);
            String name = playerJson.optString("name", null);
            int points = playerJson.optInt("point", 0);
            boolean status = playerJson.optBoolean("status", false);

            Player player = new Player();
            player.setId(id);
            player.setName(name);
            player.setPoint(points);
            player.setStatus(status);
            updatedPlayers.add(player);
        }

        Global.players.clear();
        Global.players.addAll(updatedPlayers);

        System.out.println("Lista de jugadores actualizada: " + Global.players);
    }
}
