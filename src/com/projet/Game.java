package com.projet;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    //Attributes
    private int nbPlayer;

    //Constructor
    public Game(int _nbPlayer) {
        this.nbPlayer = _nbPlayer;
    }

    //Methods
    public ArrayList<Player> createPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 1; i <= nbPlayer; i++) {
            System.out.println("Player number " + i);

            System.out.println("Enter the name of the player:");
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();

            Player player = new Player(i, name, null);
            players.add(player);
        }
        for (Player player : players) {
            System.out.println(player);
        }
        return players;
    }
}
