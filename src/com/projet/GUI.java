package com.projet;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class GUI {
    JFrame frame;
    JPanel panel;

    public GUI() {

        frame = new JFrame("Java Project");

        JButton buttonNewGame = new JButton("New game");
        buttonNewGame.addActionListener(new newGame());
        JButton buttonExitGame = new JButton("Exit");
        buttonExitGame.addActionListener(new exitGame());
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(1, 2));
        panel.add(buttonNewGame);
        panel.add(buttonExitGame);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setSize(500, 200);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new GUI();
    }

    static class newGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame frame2 = new JFrame("Number of players");

            frame2.setVisible(true);
            frame2.setSize(500, 500);

            JLabel label = new JLabel("Choose the number of players please");
            JPanel panelPlayers = new JPanel();
            frame2.add(panelPlayers);
            panelPlayers.add(label);

            JButton twoPlayersBtn;
            JButton threePlayersBtn;
            JButton fourPlayersBtn;
            JButton fivePlayersBtn;
            JButton sixPlayersBtn;

            String twoPlayersBtnName = "twoPlayersBtn";
            String threePlayersBtnName = "threePlayersBtn";
            String fourPlayersBtnName = "fourPlayersBtn";
            String fivePlayersBtnName = "fivePlayersBtn";
            String sixPlayersBtnName = "sixPlayersBtn";

            twoPlayersBtn = new JButton("Two");
            threePlayersBtn = new JButton("Three");
            fourPlayersBtn = new JButton("Four");
            fivePlayersBtn = new JButton("Five");
            sixPlayersBtn = new JButton("Six");

            twoPlayersBtn.setActionCommand(twoPlayersBtnName);
            threePlayersBtn.setActionCommand(threePlayersBtnName);
            fourPlayersBtn.setActionCommand(fourPlayersBtnName);
            fivePlayersBtn.setActionCommand(fivePlayersBtnName);
            sixPlayersBtn.setActionCommand(sixPlayersBtnName);

            panelPlayers.add(twoPlayersBtn);
            panelPlayers.add(threePlayersBtn);
            panelPlayers.add(fourPlayersBtn);
            panelPlayers.add(fivePlayersBtn);
            panelPlayers.add(sixPlayersBtn);

            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            twoPlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            threePlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            fourPlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            fivePlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sixPlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPlayers.setLayout(new BoxLayout(panelPlayers, BoxLayout.Y_AXIS));

            class twoPlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayers = 2;
                    //get the names of the players
                    //init the game
                    //display map
                    //play
                    //end page
                }
            }

            class threePlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayers = 3;
                }
            }

            class fourPlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayers = 4;
                }
            }

            class fivePlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayers = 5;
                }
            }

            class sixPlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayers = 6;
                }
            }
        }
    }


    static class exitGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
