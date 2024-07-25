package com.onner.async;

import javax.swing.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessClickMe implements Runnable {
    private final ConcurrentLinkedQueue<JButton> clickQueue = new ConcurrentLinkedQueue<>();

    public ProcessClickMe() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            JButton clickedButton = clickQueue.poll();
            if (clickedButton != null) {
                System.out.println("Botón clickeado: " + clickedButton.getText());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void addClick(JButton button) {
        clickQueue.add(button);
    }
}
