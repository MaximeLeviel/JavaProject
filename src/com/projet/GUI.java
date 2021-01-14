package com.projet;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends Game{

    private final Color[] colors;

    public GUI(int _nbPlayer, int _nbTerritories) {
        super(_nbPlayer, _nbTerritories);
        colors = new Color[] {Color.GRAY, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.PINK};
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

    @Override
    public boolean attack(Player playerAttacker, ArrayList<Integer> listAttack, Territory[][] map) {
        boolean win = true;
        Territory attacker, defender = null;
        Player playerDefender = null;
        attacker = playerAttacker.getTerritories().get(listAttack.get(0));
        outerloop:
        for(Territory[] t : map){//We need to get the Territory of ID defender
            for(Territory territory : t){
                if(territory.getID() == listAttack.get(1)){
                    defender = territory;
                    playerDefender = players.get(territory.getPlayerID());
                    break outerloop; //allows to break out of the nested for loops once we've found our territory
                }
            }
        }

        int diceAttacker = throwDices(attacker.getStrength());
        int diceDefender = throwDices(defender.getStrength());

        if(diceAttacker > diceDefender){
            playerDefender.setNbDices(playerDefender.getNbDices() - defender.getStrength()); //We remove the number of dices of the defending territory for the defender
            defender.setStrength(attacker.getStrength()-1); //We change the number of dices of the defending territories by the remaining dices
            attacker.setStrength(1); //We leave one dice to the attacking territory
            defender.setPlayerID(attacker.getPlayerID()); // The defending territory now belongs to the attacker
            playerAttacker.addToTerritories(defender); // We add this territory to the territories of the attacker
            playerDefender.deleteToTerritories(defender.getID()); //We remove the territory from the defender
        }
        else{
            win = false;
            playerAttacker.setNbDices(playerAttacker.getNbDices() - (attacker.getStrength()-1));//We remove the dices of the territory from the player (except 1)
            attacker.setStrength(1); //We replace the dices of the attacking territories by 1
        }
        return win;
    }

    private void addData(ArrayList<JButton> buttons, JPanel mapPanel, Maps myMap){
        for(int i = 0; i < buttons.size(); i++){
            JButton button = buttons.get(i);
            int id = 0;
            try {
                id = myMap.findPlayerById(i + 1);
                button.setText("" + myMap.findStrenghById(i + 1));
            } catch (Maps.NonexistentIdException e) {
                e.printStackTrace();
            }
            button.setBackground(colors[id + 1]);
        }

        mapPanel.revalidate();
    }

    private void removeColor(ArrayList<JButton> buttons, JPanel mapPanel, int attacker, int player){
        ArrayList<Integer> listAttack = players.get(player).getDefenders(attacker);
        for (int i = 0; i < buttons.size(); i++){
            JButton button = buttons.get(i);
            if(!listAttack.contains(i + 1)){
                button.setBackground(colors[0]);
            }
        }
        mapPanel.revalidate();
    }

    private void play(Maps myMap){
        final boolean[] processing = {false};
        final Integer[] attacker = {null};
        final Integer[] defender = {null};

        JFrame gameFrame = new JFrame("Map");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel masterPanel = new JPanel();

        //Declare before the map because we need to access it from within the actionListener of the map
        JButton previousButton = new JButton("Previous");

        //Determine which player starts
        final int[] player = {(int) (Math.random() * nbPlayer)};

        //Declare the top part, with information
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        JLabel playerLabel = new JLabel("Player " + (player[0] + 1) + ", it's your turn");
        JLabel colorLabel = new JLabel("Your color");
        colorLabel.setForeground(colors[player[0] + 1]);
        JLabel instructionLabel = new JLabel("Choose which territory you want to attack with or select 'Finish' to end your turn.");
        playerPanel.add(playerLabel);
        playerPanel.add(colorLabel);
        playerPanel.add(instructionLabel);

        //Declare the map
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(myMap.map[0].length, myMap.map.length));
        ArrayList<JButton> buttons = new ArrayList<>();
        for(int i = 0; i < nbTerritories; i++){
            JButton button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton source = (JButton) e.getSource();
                    String strength = e.getActionCommand();
                    int id = buttons.indexOf(source) + 1;
                    System.out.println(id);
                    boolean win = true;

                    if(!processing[0]){
                        if(players.get(player[0]).getTerritory(id) != null && !strength.equals("1")){
                            attacker[0] = id;
                            instructionLabel.setText("Now, choose which neighbor you want to attack.");
                            playerPanel.revalidate();
                            processing[0] = true;
                            removeColor(buttons, mapPanel, attacker[0], player[0]);
                            previousButton.setVisible(true);
                        }
                        else if(strength.equals("1")){
                            instructionLabel.setText("You can't attack with a territory with one dice.");
                            playerPanel.revalidate();
                        }
                        else{
                            instructionLabel.setText("You chose a territory that is not yours. Please choose an other one.");
                            playerPanel.revalidate();
                        }

                    }
                    else{
                        if(players.get(player[0]).isAttackable(id, attacker[0])){
                            defender[0] = id;
                            ArrayList<Integer> attackList = new ArrayList<>(){
                                {
                                    add(attacker[0]);
                                    add(defender[0]);
                                }
                            };
                            win = attack(players.get(player[0]), attackList, myMap.map);
                            if (!win){
                                instructionLabel.setText("Sorry, you lost.");
                                playerPanel.revalidate();
                                endTurn(player, playerPanel, playerLabel, colorLabel, processing, buttons, mapPanel, myMap);
                            }
                            else{
                                instructionLabel.setText("Congratulations, you won ! Choose which territory you want to attack with or select 'Finish' to end your turn.");
                                playerPanel.revalidate();
                                processing[0] = false;
                            }
                        }
                        else{
                            instructionLabel.setText("You can't attack this territory, please choose an other one to attack or click on previous.");
                            playerPanel.revalidate();
                        }
                        addData(buttons, mapPanel, myMap);
                    }
                }
            });
            buttons.add(button);
            mapPanel.add(button);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previous(processing, previousButton, instructionLabel, playerPanel, buttons, mapPanel, myMap);
            }
        });
        previousButton.setVisible(false);

        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurn(player, playerPanel, playerLabel, colorLabel, processing, buttons, mapPanel, myMap);
            }
        });

        addData(buttons, mapPanel, myMap);

        buttonPanel.add(previousButton);
        buttonPanel.add(finishButton);

        gameFrame.setSize(500, 500);
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
        masterPanel.add(playerPanel);
        masterPanel.add(mapPanel);
        masterPanel.add(buttonPanel);
        gameFrame.getContentPane().add(masterPanel);
        gameFrame.setVisible(true);

        System.out.println("test");
    }

    private void endTurn(int[] player, JPanel playerPanel, JLabel playerLabel, JLabel colorLabel, boolean[] processing,
                         ArrayList<JButton>  buttons, JPanel mapPanel, Maps myMap){
        bonusDices(players.get(player[0]));
        player[0] = (player[0] + 1) % nbPlayer;
        playerLabel.setText("Joueur " + (player[0] + 1) + ", à ton tour");
        colorLabel.setForeground(colors[player[0] + 1]);
        playerPanel.revalidate();
        processing[0] = false;
        addData(buttons, mapPanel, myMap);
    }

    private void previous(boolean[] processing, JButton previousButton, JLabel instructionLabel, JPanel playerPanel,
                          ArrayList<JButton> buttons, JPanel mapPanel, Maps myMap){
        processing[0] = false;
        previousButton.setVisible(false);
        instructionLabel.setText("Choose which territory you want to attack with or select 'Finish' to end your turn.");
        playerPanel.revalidate();
        addData(buttons, mapPanel, myMap);
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
}
