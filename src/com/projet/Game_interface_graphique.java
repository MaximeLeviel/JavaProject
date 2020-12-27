package com.projet;
import com.projet.Game;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class Game_interface_graphique {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Project");
        frame.setVisible(true);
        frame.setSize(500, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        JButton newGame = new JButton("New Game");
        panel.add(newGame);
        newGame.addActionListener(new newGame());

        JButton exitGame = new JButton("Exit Game");
        panel.add(exitGame);
        exitGame.addActionListener(new exitGame());
    }

    static class newGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFrame frame2 = new JFrame("Number of players");

            frame2.setVisible(true);
            frame2.setSize(500, 500);

            JLabel label = new JLabel("Choose the number of players please");
            JPanel panel = new JPanel();
            frame2.add(panel);
            panel.add(label);

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

            panel.add(twoPlayersBtn);
            panel.add(threePlayersBtn);
            panel.add(fourPlayersBtn);
            panel.add(fivePlayersBtn);
            panel.add(sixPlayersBtn);

            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            twoPlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            threePlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            fourPlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            fivePlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sixPlayersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            class twoPlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    //set the number of players to 2
                    //get the names of the players
                    //init the game
                    //dispatch the dices
                    //display map
                    //play
                    //end page
                }
            }

            class threePlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayer = 3;
                }
            }

            class fourPlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayer = 4;
                }
            }

            class fivePlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayer = 5;
                }
            }

            class sixPlayersBtn implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    final int nbPlayer = 6;
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

