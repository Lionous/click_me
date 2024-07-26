package com.onner.async;

import com.onner.global.Connection;
import com.onner.global.Global;

import javax.swing.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessClickMe implements Runnable {
    private final ConcurrentLinkedQueue<JButton> clickQueue = new ConcurrentLinkedQueue<>();


    public ProcessClickMe() {
        new Thread(this).start();
    }

    @Override
    public void run() {

        try {
            while (Global.stateOfPlay) {
                JButton clickedButton = clickQueue.poll();
                if (clickedButton != null) {
                    System.out.println("Botón clickeado: " + clickedButton.getText());
                    Connection.sendMessage(Global.id + clickedButton.getText());
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.err.println("Hilo interrumpido: " + e.getMessage());
        }
    }

    public void addClick(JButton button) {
        clickQueue.add(button);
    }
}
