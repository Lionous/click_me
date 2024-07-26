package com.onner.async;

import com.onner.global.Global;
import com.onner.global.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProcessPlayer implements Runnable{

    private final JPanel playerSpace;

    public ProcessPlayer(JPanel space) {
        this.playerSpace = space;
    }

    @Override
    public void run() {
        while (!Global.stateOfPlay) {
            generatePanelForPlayer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void generatePanelForPlayer() {
        List<Player> playerList = Global.players;
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setOpaque(false);

        int yPosition = 10;
        int padding = 10;
        int panelWidth = 230;
        int panelHeight = 70;

        for (Player player : playerList) {
            JPanel playerPanel = playerIdPanel(player.getName(), player.getPoint(), player.getStatus());
            playerPanel.setBounds(5, yPosition, panelWidth, panelHeight);
            contentPanel.add(playerPanel);
            yPosition += panelHeight + padding;
        }
        // SCROLL
        contentPanel.setPreferredSize(new Dimension(panelWidth + 20, yPosition + 10)); // Añadir un poco de margen
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        playerSpace.removeAll();
        playerSpace.setLayout(new BorderLayout());
        playerSpace.add(scrollPane, BorderLayout.CENTER);

        playerSpace.revalidate();
        playerSpace.repaint();
    }

    private JPanel playerIdPanel(String playerName, int points, Boolean active) {
        JPanel panel = new JPanel();

        panel.setBackground(new java.awt.Color(236, 222, 152));
        panel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(142, 103, 22), 2));
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JLabel title;
        title = new JLabel(active ? "active" : "offline");
        title.setFont(new Font("Noto Sans", Font.PLAIN, 14));
        title.setForeground(new Color(117, 80, 154));
        panel.add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 30));

        JLabel name = new JLabel(playerName);
        name.setFont(new Font("Noto Sans", Font.BOLD, 28));
        name.setForeground(active ? new Color(56, 203, 35) : Color.decode("#f53407"));
        panel.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 23, -1, 40));

        JLabel unit = new JLabel("PTS");
        unit.setFont(new Font("Noto Sans", Font.BOLD, 18));
        unit.setForeground(new Color(29, 158, 249));
        panel.add(unit, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 40, 30));

        JLabel point = new JLabel(points+"");
        point.setFont(new Font("Monospaced", Font.BOLD, 48));
        point.setForeground(new Color(29, 158, 249));
        panel.add(point, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 60, 50));

        return panel;
    }
}
