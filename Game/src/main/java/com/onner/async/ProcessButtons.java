package com.onner.async;

import com.onner.global.Connection;
import com.onner.global.Global;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessButtons implements Runnable {

    private long currentTime = 0;
    private int secondsQuantity = 3;
    private JLabel labelCounter = null;
    private JPanel playSPace = null;
    private final List<JButton> buttons = new ArrayList<>();
    private Set<JButton> paintedButtons = new HashSet<>();
    private ProcessClickMe processClickMe = null;

    public ProcessButtons(JLabel counter, JPanel space, ProcessClickMe processClickMe) {
        this.labelCounter = counter;
        this.playSPace = space;
        this.processClickMe = processClickMe;
        this.generateButtons();
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis() / 1000;

        try {
            while (Global.stateOfPlay){
                long elapsedTime = (System.currentTimeMillis() / 1000) - startTime;

                this.labelCounter.setText(((System.currentTimeMillis() / 1000) - this.currentTime) + "");
                if(Global.collision || this.minutesElapsed(this.currentTime, secondsQuantity)) {
                    this.currentTime = System.currentTimeMillis() / 1000;
                    Global.collision = false;

                    if (elapsedTime >= secondsQuantity) {
                        paintRandomButtons();
                    }
                }
                System.out.println(Connection.receiveMessage());
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessButtons.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean minutesElapsed(long initTime, int secondsQuantity) {
        long currentTimeTemp = System.currentTimeMillis() / 1000;
        return ((currentTimeTemp - initTime) >= secondsQuantity);
    }

    private char letraActual = 'A';

    private void paintRandomButtons() {
        String response = Connection.receiveMessage();

        if (response != null && response.startsWith("PINTAR:")) {
            char letra = response.substring("PINTAR:".length()).trim().charAt(0);
            System.out.println("Letra recibida para pintar: " + letra);

            if (letra == 1) {
                letraActual = letra;
                paintButtonsBasedOnLetter(letraActual);
            }
        }
    }

    private void paintButtonsBasedOnLetter(char letra) {
        if (buttons.isEmpty()) {
            return;
        }
        Color defaultColor = Color.decode("#f0f0f0");
        for (JButton button : buttons) {
            button.setBackground(defaultColor);
            button.setOpaque(true);
        }
        int buttonIndex = letra - 'A'; // Por ejemplo, mapea A a 0, B a 1, etc.
        if (buttonIndex >= 0 && buttonIndex < buttons.size()) {
            JButton buttonToPaint = buttons.get(buttonIndex);
            buttonToPaint.setBackground(Color.decode("#e33c02"));
            buttonToPaint.setOpaque(true);
        }
    }

    public void generateButtons() {
        playSPace.removeAll();
        int buttonWidth = 70;
        int buttonHeight = 70;
        int padding = 25;
        int[] buttonsPerRow = {6, 7, 7, 6};
        char letter = 'A';
        for (int row = 0; row < buttonsPerRow.length; row++) {
            int buttonsInThisRow = buttonsPerRow[row];
            int xOffset = (playSPace.getWidth() - (buttonsInThisRow * buttonWidth + (buttonsInThisRow - 1) * padding)) / 2;
            for (int col = 0; col < buttonsInThisRow; col++) {
                if (letter > 'Z') break;
                JButton button = new JButton(String.valueOf(letter));
                button.setFont(new java.awt.Font("Noto Sans", Font.BOLD, 36));
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setBackground(Color.decode("#f0f0f0"));
                int x = xOffset + col * (buttonWidth + padding);
                int y = row * (buttonHeight + padding);
                button.setBounds(x, y + 60, buttonWidth, buttonHeight);
                playSPace.add(button);
                buttons.add(button);

                // Thread
                button.addActionListener(e -> processClickMe.addClick(button));
                letter++;
            }
        }
        playSPace.revalidate();
        playSPace.repaint();
    }

    public void stop() {
        Global.stateOfPlay = false;
    }
}
