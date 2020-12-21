package com.projet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    //Attributes
    private int nbPlayer;
    private int nbTerritories;
    private ArrayList<Player> players;

    //Constructor
    public Game(int _nbPlayer) {
        this.nbPlayer = _nbPlayer;
    }

    //Methods for players
    public void createPlayers() {
        this.players = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        for (int i = 1; i <= nbPlayer; i++) {

            System.out.println("\n------ Player number " + i + "------");

            System.out.println("Enter the name of the player:");
            String name = sc.nextLine();

            Player player = new Player(i, name, null);
            this.players.add(player);
        }
    }

    public void initTerritories(Territory[][] map){//associate the territories to the players
        for (Player player : players) {//Create and associate an array territory for each players
            ArrayList<Territory> territories = new ArrayList<>();
            player.initTerritories(territories);
        }
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++) {
                int p = map[i][j].getPlayerID()-1;
                players.get(p).addToTerritories(map[i][j]);
            }
        }
    }

    public void displayPlayers(){
        for (Player player : this.players) {
            System.out.println(player);
        }
    }

    //Methods for map
    public void initMap(Territory[][] map){ //Give the strength and players to each territory
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                //TODO : faire en sorte que chaque player ai le meme nombre de dés
                //TODO : s'assurer que chaque player ai un minimum de territoire
                int player = (int)(Math.random() * nbPlayer + 1);
                int strength = (int)(Math.random() * 8 + 1); //between 1 and 8
                map[i][j].setStrength(strength);
                map[i][j].setPlayerID(player);
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
}
