package client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client2 extends JFrame {
    private JButton[] botones;
    private JLabel[] puntajesLabels;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client2() {
        //this.setLocationRelativeTo(null);
        this.setTitle("Juego de Letras");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setLayout(new BorderLayout());

        JPanel PLAYSPACE = new JPanel(null);
        PLAYSPACE.setBackground(new java.awt.Color(252, 194, 52));
        this.add(PLAYSPACE);

        JPanel space = new JPanel(null);
        space.setBounds(300,100,670,470);
        space.setBackground(new java.awt.Color(99, 66, 14));
        space.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(181, 120, 22), 5));
        PLAYSPACE.add(space, BorderLayout.CENTER);

        JPanel playerSpace = new JPanel(null);
        playerSpace.setBounds(20,100,250,470);
        playerSpace.setBackground(new java.awt.Color(203, 132, 28));
        playerSpace.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 76, 1), 3));
        PLAYSPACE.add(playerSpace, BorderLayout.SOUTH);

        JPanel player = new JPanel(null);
        player.setBackground(new java.awt.Color(236, 222, 152));
        player.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(142, 103, 22), 2));
        playerSpace.add(player, BorderLayout.SOUTH);


        generateButtons(space);

        JPanel puntajesPanel = new JPanel(new GridLayout(1, 2));
        puntajesLabels = new JLabel[2];
        for (int i = 0; i < 2; i++) {
            puntajesLabels[i] = new JLabel("Jugador " + (i+1) + ": 0");
            puntajesPanel.add(puntajesLabels[i]);
        }
        this.add(puntajesPanel, BorderLayout.SOUTH);

        conectarAlServidor();
    }

    private void generateButtons(JPanel playSpace) {
        playSpace.removeAll();
        int buttonWidth = 70;
        int buttonHeight = 70;
        int padding = 25;
        int[] buttonsPerRow = {6, 7, 7, 6};
        char letter = 'A';

        botones = new JButton[26];
        int index = 0;

        for (int row = 0; row < buttonsPerRow.length; row++) {
            int buttonsInThisRow = buttonsPerRow[row];
            int xOffset = (playSpace.getWidth() - (buttonsInThisRow * buttonWidth + (buttonsInThisRow - 1) * padding)) / 2;
            for (int col = 0; col < buttonsInThisRow; col++) {
                if (letter > 'Z') break;
                JButton button = new JButton(String.valueOf(letter));
                button.setFont(new Font("Noto Sans", Font.BOLD, 36));
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setBackground(Color.decode("#f0f0f0"));
                int x = xOffset + col * (buttonWidth + padding);
                int y = row * (buttonHeight + padding);
                button.setBounds(x, y + 60, buttonWidth, buttonHeight);
                playSpace.add(button);

                botones[index] = button;
                botones[index].addActionListener(new BotonListener());
                index++;

                letter++;
            }
        }
        playSpace.revalidate();
        playSpace.repaint();
    }

    private class BotonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton boton = (JButton) e.getSource();
            String letra = boton.getText();
            out.println(letra);
        }
    }

    private void conectarAlServidor() {
        try {
            socket = new Socket("192.168.137.28", 6400);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new ServidorListener()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServidorListener implements Runnable {
        public void run() {
            try {
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    String[] partes = mensaje.split(" ");
                    if (partes[0].equals("PINTAR")) {
                        SwingUtilities.invokeLater(() -> pintarBoton(partes[1].charAt(0)));
                    } else if (partes[0].equals("PUNTOS")) {
                        int jugador = Integer.parseInt(partes[1]);
                        int puntos = Integer.parseInt(partes[2]);
                        SwingUtilities.invokeLater(() -> actualizarPuntaje(jugador, puntos));
                    } else if (partes[0].equals("FIN")) {
                        int ganador = Integer.parseInt(partes[1]);
                        SwingUtilities.invokeLater(() -> mostrarGanador(ganador));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pintarBoton(char letra) {
        for (JButton boton : botones) {
            if (boton.getText().charAt(0) == letra) {
                boton.setBackground(Color.YELLOW);
            } else {
                boton.setBackground(null);
            }
        }
    }

    private void actualizarPuntaje(int jugador, int puntos) {
        puntajesLabels[jugador].setText("Jugador " + (jugador+1) + ": " + puntos);
    }

    private void mostrarGanador(int ganador) {
        JOptionPane.showMessageDialog(this, "¡El Jugador " + (ganador+1) + " ha ganado!");
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client2().setVisible(true));
    }
}