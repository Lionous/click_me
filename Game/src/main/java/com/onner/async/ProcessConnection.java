package com.onner.async;
import com.onner.form.Space;
import com.onner.global.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessConnection {
    private int counter = 0;
    private static List<Player> players = new ArrayList<>();
    private JToggleButton newPlayerButton;

    public ProcessConnection() {
        addPlayersFromInput();
        createUsers();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> createUsers() {
        Player player1 = new Player();
        player1.setId("id-001");
        player1.setName("Alice");
        player1.setPoint(10);
        player1.setStatus(true);
        player1.setMessage("Hello Alice");

        Player player2 = new Player();
        player2.setId("id-002");
        player2.setName("Bob");
        player2.setPoint(15);
        player2.setStatus(false);
        player2.setMessage("Hello Bob");

        players.add(player1);
        players.add(player2);

        return players;
    }

    private void addPlayersFromInput() {
        newPlayerButton = Space.newPlayer;
        newPlayerButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nombre de jugador:");
            if (name == null || name.trim().isEmpty()) {
                return;
            }
            Player player = new Player();
            counter++;
            player.setName(name);
            player.setId("id-"+counter);
            player.setPoint(0);
            player.setStatus(true);
            player.setMessage("");

            System.out.println(player); // ----------------
            addPlayer(player);
        });
    }
}
