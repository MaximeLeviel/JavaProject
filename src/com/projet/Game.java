package com.projet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Game {
    //Attributes
    private final int nbPlayer;
    private final int nbTerritories;
    private ArrayList<Player> players;

    //Constructor
    public Game(int _nbPlayer, int _nbTerritories) {
        this.nbPlayer = _nbPlayer;
        this.nbTerritories = _nbTerritories;
    }

    //Methods for players
    public void createPlayers() {
        this.players = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < nbPlayer; i++) {

            System.out.println("\n------ Player number " + (i+1) + " ------");

            System.out.println("Enter the name of the player:");
            String name = sc.nextLine();

            Player player = new Player(i, name);
            this.players.add(player);
        }
    }

    public void displayPlayers(){
        for (Player player : this.players) {
            System.out.println(player);
        }
    }

    //Methods for map
    public void initMap(Territory[][] map){ //Give the strength and players to each territory
        int remainder = nbTerritories % nbPlayer;
        int totalDices = (nbTerritories/nbPlayer)*5; //Number of dices per player = average of 5 dices per territory
        int player, strength;

        //Dispatch the different territories equally (or add the excess randomly)
        int cpt = 1;
        for (Territory[] territories : map) {
            for (int j = 0; j < map[0].length; j++) {
                if(cpt <= nbTerritories - remainder){//Associate the same number of territories for each players
                    do {
                        player = (int) (Math.random() * nbPlayer); //associate a random player (from 0 to nbPlayer -1)
                    }
                    while (players.get(player).getTerritories().size() >= (nbTerritories / nbPlayer)); //if player has already its maximum number of territories (nbTerritories/nbPlayers) pull another player
                }
                else{//Associate the remaining territories in excess randomly
                    player = (int) (Math.random() * nbPlayer);
                }
                players.get(player).addToTerritories(territories[j]);//associate the territory to the player
                territories[j].setPlayerID(player);//associate the player's ID to the territory
                cpt++;
            }
        }

        //Dispatch the dices to the territories for each players
        for (Player p : players) {
            cpt = 1;
            for (Map.Entry<Integer, Territory> entry : p.getTerritories().entrySet()) {
                do {
                    strength = (int) (Math.random() * 8 + 1); //between 1 and 8

                } while ((totalDices - (p.getNbDices() + strength) < (p.getTerritories().size()-cpt))// [number of remaining dices < number of remaining territories] = not enough dices for remaining territories
                        || (totalDices - (p.getNbDices() + strength) > 8 * (p.getTerritories().size()-cpt)));//Or [number of remaining dices > 8 * number of remaining territories] = too much dices left for remaining territories

                cpt++;
                p.setNbDices(strength + p.getNbDices());//Add the dices to player
                entry.getValue().setStrength(strength);//associate a strength to the territory
            }
        }
    }



    public void displayMap(Territory[][] map){
        System.out.println("------------------------------------------- MAP -------------------------------------------");
        for (Territory[] territories : map) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(territories[j].displayTerritory() + "  ");
            }
            System.out.println("");
        }
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("[] : ID of the territory");
        System.out.println("{} : Strength of the territory");
        System.out.println("() : Owner of the territory");
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    public boolean endCondition(){
        //If a player has all territories
        for(Player player : players){
            if(player.getTerritories().size() == nbTerritories){
                return true;
            }
        }
        return false;
    }

    //START A GAME
    public static int checkInput(int min, int max){ //Check the inputs of the user if expected an integer with min and max values
        int input;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Enter a number between " + min +" and "+ max +": ");
            while (!sc.hasNextInt()) {
                System.out.println("Please enter an integer: ");
                sc.next(); // delete the last scanner
            }
            input = sc.nextInt();
        } while (input < min || input > max);
        return input;
    }

    public int throwDices(int nbDices){//Simulate the throw of nbDices of dices
        int dices = 0;
        for(int i = 0; i < nbDices; i++){
            int dice = (int) (Math.random() * 6 + 1); //between 1 and 6
            dices = dices + dice;
        }
        return dices;
    }

    public boolean attack(Player playerAttacker, ArrayList<Integer> listAttack, Territory[][] map){
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

        System.out.println("\n----------- Territory "+ listAttack.get(0) + " attacks territory " + listAttack.get(1) + " -----------");
        System.out.println("\nAttacker:\nPlayer " + (attacker.getPlayerID() + 1) + "\nNumber of dices: " + attacker.getStrength());
        System.out.println("\nDefender:\nPlayer " + (defender.getPlayerID() + 1) + "\nNumber of dices: " + defender.getStrength());

        System.out.println("Press enter to through the dices");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        int diceAttacker = throwDices(attacker.getStrength());
        int diceDefender = throwDices(defender.getStrength());

        System.out.println("Dices of the attacker : " + diceAttacker);
        System.out.println("Dices of the defender : " + diceDefender);

        if(diceAttacker > diceDefender){
            System.out.println("The attacker won the battle, the territory " + (defender.getID()) + " now belongs to player " + (playerAttacker.getID() + 1));
            playerDefender.setNbDices(playerDefender.getNbDices() - defender.getStrength()); //We remove the number of dices of the defending territory for the defender
            defender.setStrength(attacker.getStrength()-1); //We change the number of dices of the defending territories by the remaining dices
            attacker.setStrength(1); //We leave one dice to the attacking territory
            defender.setPlayerID(attacker.getPlayerID()); // The defending territory now belongs to the attacker
            playerAttacker.addToTerritories(defender); // We add this territory to the territories of the attacker
            playerDefender.deleteToTerritories(defender.getID()); //We remove the territory from the defender
        }
        else{
            System.out.println("The attacker looses the battle, player number " + (attacker.getPlayerID() + 1) + " looses " + (attacker.getStrength()-1) + " dices");
            win = false;
            attacker.setStrength(1); //We replace the dices of the attacking territories by 1
        }
        return win;
    }

    //MAIN
    public static void main(String[] Arg){
        int opt = 1;
        do {
            int nbPlayer, line, column;

            System.out.println("=================================== WELCOME TO RISK ===================================\n");

            System.out.println("-------------- NUMBER OF PLAYERS --------------\n");
            nbPlayer = checkInput(2, 6);
            System.out.println("\nNumber of players: " + nbPlayer);

            System.out.println("\n--------------- SIZE OF THE MAP ---------------\n");
            System.out.println("Enter the number of lines: ");
            line = checkInput(4, 9);
            System.out.println("Enter the number of columns: ");
            column = checkInput(4, 10);
            int nbTerritories = line*column;

            //Creation of the game
            System.out.println("\n------------- NAME OF THE PLAYERS -------------");
            Game game = new Game(nbPlayer, nbTerritories);

            //Creation of the map
            Maps myMap = new Maps(line, column); //Create a map of dimension line*column (empty)
            myMap.createMap(); //Fill the map with Territories ID

            //Creation of the players
            game.createPlayers();
            game.initMap(myMap.map); //Fill the territories with a strength and a

            //Display
            System.out.println("\n----------------------------------------- INITIALIZATION -----------------------------------------\n");
            game.displayMap(myMap.map);
            game.displayPlayers();

            //Just for better display
            System.out.println("Press enter to continue");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();


            System.out.println("\n------------------------------------ START OF THE GAME ------------------------------------");
            int player = (int) (Math.random() * nbPlayer);
            System.out.println("Player " + (player+1) + " will begin:");
            do{
                boolean win;
                int turn = 1;
                do{
                    game.displayMap(myMap.map);
                    ArrayList<Integer> listAttack = new ArrayList<>();
                    listAttack = game.players.get(player).attackTerritories(); //select the territory that will attack and the defender
                    win = game.attack(game.players.get(player), listAttack, myMap.map); //launch the attack

                    //Just for better display
                    System.out.println("Press the enter key to continue");
                    sc = new Scanner(System.in);
                    input = sc.nextLine();

                    if(win){
                        game.displayMap(myMap.map);
                        System.out.println(game.players.get(player));
                        turn = game.players.get(player).endTurn(game);
                    }
                }
                while(win && turn != 0);

                //TODO : implémenter le gains de dés a la fin d'un tour
                if(player != (nbPlayer-1)){//We go to the next player
                    player = player + 1;
                }
                else{
                    player = 0; //We go back to first player
                }
            }
            while(!game.endCondition());

            System.out.println("\n------------------------------------ END OF THE GAME ------------------------------------");
            System.out.println("Do you want to start a new game ? : 1. Yes  2. No");
            opt = checkInput(1, 2);
        }
        while(opt != 2);


    }
}
