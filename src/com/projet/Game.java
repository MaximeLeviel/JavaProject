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

    //Methods
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

    public void displayPlayers(){
        for (Player player : this.players) {
            System.out.println(player);
        }
    }
}
