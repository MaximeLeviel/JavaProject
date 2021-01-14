package com.projet;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends Game{

    public GUI(int _nbPlayer, int _nbTerritories) {
        super(_nbPlayer, _nbTerritories);
    }

    @Override
    public void createPlayers() {
        //TODO : Demander les noms (optionel)
        players = new ArrayList<>();
        for(int i = 0; i < nbPlayer; i++){
            Player player = new Player(i, "Player " + i);
            players.add(player);
        }
    }

    private void play(Maps myMap){
        JFrame gameFrame = new JFrame("Map");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel masterPanel = new JPanel();

        final int[] player = {(int) (Math.random() * nbPlayer)};
        JPanel playerPanel = new JPanel();
        JLabel playerLabel = new JLabel("Joueur " + (player[0] + 1) + ", à ton tour");
        playerPanel.add(playerLabel);

        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(myMap.map[0].length, myMap.map.length));
        ArrayList<JButton> buttons = new ArrayList<>();
        Color[] colors = new Color[] {Color.GRAY, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.PINK};
        for(int i = 0; i < nbTerritories; i++){
            JButton button = new JButton();
            try {
                button.setText("" + myMap.findStrenghById(i + 1));
                int id = myMap.findPlayerById(i + 1);
                button.setBackground(colors[id + 1]);
            } catch (Maps.NonexistentIdException e) {
                e.printStackTrace();
            }
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton source = (JButton) e.getSource();
                    int id = buttons.indexOf(source);
                    System.out.println(id);

                    player[0] = (player[0] + 1) % nbPlayer;
                    playerLabel.setText("Joueur " + (player[0] + 1) + ", à ton tour");
                    playerPanel.revalidate();
                    System.out.println(player[0]);
                }
            });
            buttons.add(button);
            mapPanel.add(button);
        }

        gameFrame.setSize(500, 500);
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
        masterPanel.add(playerPanel);
        masterPanel.add(mapPanel);
        gameFrame.getContentPane().add(masterPanel);
        gameFrame.setVisible(true);

        System.out.println("test");
    }

    public static void main(String[] args) {
        JFrame launchFrame = new JFrame("Settings");
        launchFrame.setSize(500, 500);

        JPanel launchPanel = new JPanel();
        launchPanel.setLayout(new BoxLayout(launchPanel, BoxLayout.Y_AXIS));

        JPanel playerNbPanel = new JPanel();
        playerNbPanel.setLayout(new BoxLayout(playerNbPanel, BoxLayout.X_AXIS));
        JLabel playerNbLabel = new JLabel("Choose the number of players please");
        Integer[] playerNb = new Integer[] {2, 3, 4, 5, 6};
        JComboBox<Integer> jComboBoxPlayerNb = new JComboBox<>(playerNb);
        jComboBoxPlayerNb.setMaximumSize(new Dimension(50, 25));
        playerNbPanel.add(playerNbLabel);
        playerNbPanel.add(jComboBoxPlayerNb);

        //JPanel CSVPanel = new JPanel();
        //CSVPanel.setLayout(new BoxLayout(launchPanel, BoxLayout.X_AXIS));

        JPanel linesPanel = new JPanel();
        linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.X_AXIS));
        JLabel linesLabel = new JLabel("Choose the number of lines please");
        Integer[] lines = new Integer[] {4, 5, 6, 7, 8, 9};
        JComboBox<Integer> jComboBoxLines = new JComboBox<>(lines);
        jComboBoxLines.setMaximumSize(new Dimension(50, 25));
        linesPanel.add(linesLabel);
        linesPanel.add(jComboBoxLines);

        JPanel columnsPanel = new JPanel();
        columnsPanel.setLayout(new BoxLayout(columnsPanel, BoxLayout.X_AXIS));
        JLabel columnsLabel = new JLabel("Choose the number of columns please");
        Integer[] columns = new Integer[] {4, 5, 6, 7, 8, 9};
        JComboBox<Integer> jComboBoxColumns = new JComboBox<>(columns);
        jComboBoxColumns.setMaximumSize(new Dimension(50, 25));
        columnsPanel.add(columnsLabel);
        columnsPanel.add(jComboBoxColumns);

        JButton validateButton = new JButton("Validate");
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int lines = (int)jComboBoxLines.getSelectedItem();
                int columns = (int) jComboBoxColumns.getSelectedItem();
                int nbTerritories = lines * columns;
                int nbPlayer = (int)jComboBoxPlayerNb.getSelectedItem();

                //Creation of the map
                Maps myMap = new Maps(lines, columns);
                myMap.createMap();

                //Creation of the game
                GUI gui = new GUI(nbPlayer, nbTerritories);

                //Creation of the players
                gui.createPlayers();
                gui.initMap(myMap.map); //Fill the territories with a strength and a player's ID
                myMap.initNeighbors(); //Fill the neighbors for each territory

                gui.play(myMap);

                System.out.print("Test");
            }
        });

        launchPanel.add(playerNbPanel);
        launchPanel.add(linesPanel);
        launchPanel.add(columnsPanel);
        launchPanel.add(validateButton);

        launchFrame.getContentPane().add(launchPanel);
        launchFrame.setVisible(true);
    }

    static class exitGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
