package com.projet;

import java.util.ArrayList;
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
            for(int i = 0; i < (p.getTerritories().size() - 1); i++){//All territories except the last one
                do {
                    strength = (int) (Math.random() * 8 + 1); //between 1 and 8

                } while ((totalDices - (p.getNbDices() + strength) < (p.getTerritories().size()-cpt))// [number of remaining dices < number of remaining territories] = not enough dices for remaining territories
                        || (totalDices - (p.getNbDices() + strength) > 8 * (p.getTerritories().size()-cpt)));//Or [number of remaining dices > 8 * number of remaining territories] = too much dices left for remaining territories

                cpt++;
                p.setNbDices(strength + p.getNbDices());//Add the dices to player
                p.getTerritories().get(i).setStrength(strength);//associate a strength to the territory
            }
            p.getTerritories().get(p.getTerritories().size() - 1).setStrength(totalDices-p.getNbDices()); //Last territory filled with the remaining dices (we made sure it was between 1 and 8)
            p.setNbDices((totalDices));//Add the dices to player
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

    //MAIN
    public static void main(String[] Arg){
        int opt = 1;
        do {
            int nbPlayer, line, column;

            System.out.println("======================= WELCOME TO RISK =======================\n");

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
            System.out.println("-------------- SETTINGS --------------\n");
            Game game = new Game(nbPlayer, nbTerritories);

            //Creation of the map
            Map myMap = new Map(line, column); //Create a map of dimension line*column (empty)
            myMap.createMap(); //Fill the map with Territories ID

            //Creation of the players
            game.createPlayers();
            game.initMap(myMap.map); //Fill the territories with a strength and a

            //Display
            game.displayMap(myMap.map);
            game.displayPlayers();

            System.out.println("\n-------------------- START OF THE GAME --------------------");
            int player = (int) (Math.random() * nbPlayer + 1);
            System.out.println("Player " + player + " will begin:");

            do{
                System.out.println("GAME GAME GAME");
                line = checkInput(4, 9);
            }
            while(!game.endCondition());

            System.out.println("\n--------------------- END OF THE GAME ---------------------");
            System.out.println("Do you want to start a new game ? : 1. Yes  2. No");
            opt = checkInput(1, 2);
        }
        while(opt != 2);


    }
}
