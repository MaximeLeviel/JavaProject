package com.projet;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Game{

    private final Color[] colors;

    public GUI(int _nbPlayer, int _nbTerritories) {
        super(_nbPlayer, _nbTerritories);
        colors = new Color[] {Color.GRAY, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.PINK};
    }

    public GUI(Game game){
        super(game.nbPlayer, game.nbTerritories);
        colors = new Color[] {Color.GRAY, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.PINK};
        players = new ArrayList<>();
        players.addAll(game.players);
    }

    public void createPlayers(JFrame masterFrame) {
        JDialog nameFrame = new JDialog(masterFrame, "Player names");
        nameFrame.setModal(true);
        nameFrame.setLocationRelativeTo(null);
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));

        ArrayList<JTextField> nameTextFields = new ArrayList<>();

        for(int i = 0; i < nbPlayer; i++){
            JTextField nameTextField = new JTextField();
            nameTextField.setPreferredSize(new Dimension(150, 25));
            nameTextFields.add(nameTextField);

            JLabel nameLabel = new JLabel("Name " + (i + 1) + " :");
            namePanel.add(nameLabel);
            namePanel.add(nameTextField);
        }

        JButton validateButton = new JButton("Validate");
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                players = new ArrayList<>();
                for(int i = 0; i < nbPlayer; i++){
                    Player player;
                    if(nameTextFields.get(i).getText().isEmpty()){
                        player = new Player(i, "Player " + (i + 1));
                    }
                    else{
                        player = new Player(i, nameTextFields.get(i).getText());
                    }
                    players.add(player);
                }
                nameFrame.dispose();
            }
        });

        namePanel.add(validateButton);

        nameFrame.getContentPane().add(namePanel);
        nameFrame.pack();
        nameFrame.setVisible(true);
    }

    public static void loadSaveGame(){
        JFrame loadFrame = new JFrame();
        loadFrame.setTitle("Load a save");
        loadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loadFrame.setLocationRelativeTo(null);

        JPanel loadPanel = new JPanel();
        loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));
        
        JLabel loadLabel = new JLabel("Enter the name of the save");
        JTextField loadTextField = new JTextField();
        loadTextField.setPreferredSize(new Dimension(150, 25));

        JLabel errorLabel = new JLabel("Sorry, the file doesn't exist. Try again.");
        errorLabel.setVisible(false);

        JButton loadButton = new JButton("Validate");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = loadTextField.getText();
                try {
                    Save save = Save.deserialize(name + ".txt");
                    Game game = save.getGame();
                    GUI gui = new GUI(game);
                    Maps myMap = save.getMyMap();
                    int[] player = {save.getPlayer()};

                    gui.play(myMap, player);
                    loadFrame.dispose();
                } catch (IOException | ClassNotFoundException ioException) {
                    errorLabel.setVisible(true);
                }
        }
        });

        loadPanel.add(loadLabel);
        loadPanel.add(loadTextField);
        loadPanel.add(errorLabel);
        loadPanel.add(loadButton);
        loadFrame.getContentPane().add(loadPanel);
        loadFrame.pack();
        loadFrame.setVisible(true);
    }
    
    private void saveGame(Maps myMap, int player){
        JFrame saveFrame = new JFrame();
        saveFrame.setTitle("Save the gamae");
        saveFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        saveFrame.setLocationRelativeTo(null);

        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS));

        JLabel saveLabel = new JLabel("Enter the name of the save");
        JTextField loadTextField = new JTextField();
        loadTextField.setPreferredSize(new Dimension(150, 25));

        JButton saveButton = new JButton("Validate");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = loadTextField.getText();
                Save save = new Save(outerClass(), myMap, player);
                try{
                    save.serialize(name + ".txt");
                    popUpMessage("Saved", "Game saved, you can now leave.");
                } catch (IOException ioException) {
                    popUpMessage("Error", "Sorry, something went wrong during the save.");
                }
            }
        });

        savePanel.add(saveLabel);
        savePanel.add(loadTextField);
        savePanel.add(saveButton);
        saveFrame.getContentPane().add(savePanel);
        saveFrame.pack();
        saveFrame.setVisible(true);
    }

    private GUI outerClass(){
        return this;
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
                button.setEnabled(true);
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
                button.setEnabled(false);
            }
        }
        mapPanel.revalidate();
    }

    private void play(Maps myMap, int[] player){
        final boolean[] processing = {false};
        final Integer[] attacker = {null};
        final Integer[] defender = {null};
        final ArrayList<Integer>[] winners = new ArrayList[]{winners()};

        JFrame gameFrame = new JFrame("Map");
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);

        JPanel masterPanel = new JPanel();

        //Declare before the map because we need to access it from within the actionListener of the map
        JButton previousButton = new JButton("Previous");

        //Declare the top part, with information
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        JLabel playerLabel = new JLabel(players.get(player[0]).getName() + ", it's your turn");
        JLabel colorLabel = new JLabel("Your color");
        colorLabel.setForeground(colors[player[0] + 1]);
        JLabel instructionLabel = new JLabel("<html>Choose which territory you want to attack with or select 'Finish' to end your turn.</html>");
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
                            instructionLabel.setText("<html>Now, choose which neighbor you want to attack.</html>");
                            playerPanel.revalidate();
                            processing[0] = true;
                            removeColor(buttons, mapPanel, attacker[0], player[0]);
                            previousButton.setVisible(true);
                        }
                        else if(strength.equals("1")){
                            instructionLabel.setText("<html>You can't attack with a territory with one dice.</html>");
                            playerPanel.revalidate();
                        }
                        else{
                            instructionLabel.setText("<html>You chose a territory that is not yours. Please choose an other one.</html>");
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
                                popUpMessage("You lost.", "Sorry " + players.get(player[0]).getName() + ", you lost the dice roll.");
                                instructionLabel.setText("<html>Choose which territory you want to attack with or " +
                                        "select 'Finish' to end your turn.</html>");
                                playerPanel.revalidate();
                                endTurn(player, playerPanel, playerLabel, colorLabel, processing, buttons, mapPanel, myMap, gameFrame, winners[0]);
                            }
                            else{
                                winners[0] = winners();
                                if(winners[0].size() == 1){
                                    gameFrame.dispose();
                                    endOfGame(winners[0].get(0));
                                }
                                instructionLabel.setText("<html>Congratulations, you won ! Choose which territory you want to " +
                                        "attack with or select 'Finish' to end your turn.</html>");
                                playerPanel.revalidate();
                                processing[0] = false;
                            }
                        }
                        else{
                            instructionLabel.setText("<html>You can't attack this territory, please choose an other one to " +
                                    "attack or click on previous.</html>");
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
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame(myMap, player[0]);
            }
        });

        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bonus = bonusDices(players.get(player[0]));
                popUpMessage("You win.", "Congratulations, you have won " + bonus + " bonus dices.");
                endTurn(player, playerPanel, playerLabel, colorLabel, processing, buttons, mapPanel, myMap, gameFrame, winners[0]);
            }
        });

        addData(buttons, mapPanel, myMap);

        buttonPanel.add(saveButton);
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
                         ArrayList<JButton>  buttons, JPanel mapPanel, Maps myMap, JFrame gameFrame, ArrayList<Integer> winners){

        do{
            player[0] = (player[0] + 1) % nbPlayer;
        }while(!winners.contains(player[0]));
        playerLabel.setText(players.get(player[0]).getName() + ", it's your turn.");
        colorLabel.setForeground(colors[player[0] + 1]);
        playerPanel.revalidate();
        processing[0] = false;
        addData(buttons, mapPanel, myMap);
    }

    private void popUpMessage(String title, String message){
        JFrame popUpFrame = new JFrame(title);
        popUpFrame.setSize(300, 150);
        popUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        popUpFrame.setLocationRelativeTo(null);
        
        
        JPanel lostPanel = new JPanel();
        JLabel lostLabel = new JLabel("<html>" + message + "</html>");
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popUpFrame.dispose();
            }
        });
        lostPanel.add(lostLabel);
        lostPanel.add(okButton);
        popUpFrame.getContentPane().add(lostPanel);
        popUpFrame.setVisible(true);
    }

    private void endOfGame(int winner){
        JFrame finalFrame = new JFrame("Winner : " + players.get(winner).getName());
        finalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finalFrame.setSize(500, 500);
        finalFrame.setLocationRelativeTo(null);
        JPanel finalPanel = new JPanel();
        JLabel congratulationLabel = new JLabel("Congratulation " + players.get(winner).getName() + ", you won the " +
                "game and destroyed your friends :)");
        finalPanel.add(congratulationLabel);

        JButton newGameButton = new JButton("Play again !");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalFrame.dispose();
            }
        });
        finalPanel.add(newGameButton);
        finalFrame.getContentPane().add(finalPanel);
        finalFrame.setVisible(true);
    }

    private ArrayList<Integer> winners(){
        ArrayList<Integer> winners = new ArrayList<>();
        for (Player player : players) {
            if (player.getTerritories().size() != 0) {
                winners.add(player.getID());
            }
        }
        return winners;
    }

    private void previous(boolean[] processing, JButton previousButton, JLabel instructionLabel, JPanel playerPanel,
                          ArrayList<JButton> buttons, JPanel mapPanel, Maps myMap){
        processing[0] = false;
        previousButton.setVisible(false);
        instructionLabel.setText("<html>Choose which territory you want to attack with or select 'Finish' to end your turn.</html>");
        playerPanel.revalidate();
        addData(buttons, mapPanel, myMap);
    }

    public static void main(String[] args) {
        final boolean[] checked = {false};

        JFrame launchFrame = new JFrame("Settings");
        launchFrame.setSize(500, 500);
        launchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        launchFrame.setLocationRelativeTo(null);

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

        JPanel CSVCheckBoxPanel = new JPanel();
        CSVCheckBoxPanel.setLayout(new BoxLayout(CSVCheckBoxPanel, BoxLayout.X_AXIS));
        JLabel CSVCheckBoxLabel = new JLabel("Load map from CSV file.");
        JCheckBox CSVCheckBox = new JCheckBox();
        CSVCheckBoxPanel.add(CSVCheckBoxLabel);
        CSVCheckBoxPanel.add(CSVCheckBox);

        JPanel CSVPanel = new JPanel();
        CSVPanel.setLayout(new BoxLayout(CSVPanel, BoxLayout.Y_AXIS));
        JPanel CSVNamePanel = new JPanel();
        CSVNamePanel.setLayout(new BoxLayout(CSVNamePanel, BoxLayout.X_AXIS));
        JLabel CSVLabel = new JLabel("Enter the name of the file : ");
        JTextField CSVTextField = new JTextField();
        CSVTextField.setMaximumSize(new Dimension(150, 25));
        CSVNamePanel.add(CSVLabel);
        CSVNamePanel.add(CSVTextField);

        JPanel errorCSVPanel = new JPanel();
        errorCSVPanel.setLayout(new BoxLayout(errorCSVPanel, BoxLayout.X_AXIS));
        JLabel errorCSVLabel = new JLabel("The file specified doesn't exist. Please enter a new one");
        errorCSVLabel.setVisible(false);
        errorCSVPanel.add(errorCSVLabel);

        CSVPanel.add(CSVNamePanel);
        CSVPanel.add(errorCSVPanel);
        CSVPanel.setVisible(false);

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

        CSVCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checked[0] = !checked[0];
                CSVPanel.setVisible(checked[0]);
                linesPanel.setVisible(!checked[0]);
                columnsPanel.setVisible(!checked[0]);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton validateButton = new JButton("Validate");
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int nbTerritories;
                    Maps myMap;
                    int nbPlayer = (int)jComboBoxPlayerNb.getSelectedItem();
                    if(checked[0]){
                        String name = CSVTextField.getText();
                        myMap = new Maps(name + ".csv");
                        nbTerritories = myMap.map.length * myMap.map[0].length;
                    }
                    else{
                        int lines = (int)jComboBoxLines.getSelectedItem();
                        int columns = (int) jComboBoxColumns.getSelectedItem();
                        nbTerritories = lines * columns;

                        //Creation of the map
                        myMap = new Maps(lines, columns);
                        myMap.createMap();
                    }

                    //Creation of the game
                    GUI gui = new GUI(nbPlayer, nbTerritories);

                    //Creation of the players
                    gui.createPlayers(launchFrame);
                    gui.initMap(myMap.map); //Fill the territories with a strength and a player's ID
                    myMap.initNeighbors(); //Fill the neighbors for each territory

                    //Determine which player starts
                    int[] player = {(int) (Math.random() * nbPlayer)};

                    gui.play(myMap, player);
                } catch (FileNotFoundException fileNotFoundException) {
                    errorCSVLabel.setVisible(true);
                } catch (Exception ignored){}
            }
        });

        JButton loadButton = new JButton("Load a save");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI gui = new GUI(0, 0);
                loadSaveGame();
            }
        });

        buttonPanel.add(loadButton);
        buttonPanel.add(validateButton);

        launchPanel.add(playerNbPanel);
        launchPanel.add(CSVCheckBoxPanel);
        launchPanel.add(CSVPanel);
        launchPanel.add(linesPanel);
        launchPanel.add(columnsPanel);
        launchPanel.add(buttonPanel);

        launchFrame.getContentPane().add(launchPanel);
        launchFrame.setVisible(true);
    }
}
