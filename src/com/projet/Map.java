package com.projet;

import java.util.Arrays;

public class Map {
    //Attributes
    public Territory map[][];

    //Constructor
    public Map(int line, int column){
        this.map = new Territory[line][column];
    }

    //Methods
    public void initMap(){
        int cpt = 1;
        for(int i = 0; i < this.map.length; i++){
            for(int j = 0; j < this.map[0].length; j++){
                int player = (int)(Math.random() * 2 + 1); //( * 2 + 1) = from 1 to 2
                int strength = (int)(Math.random() * 20 + 1);
                this.map[i][j] = new Territory(cpt, player, strength, null);
                cpt++;
            }
        }
    }

    public void displayMap(){
        System.out.println("------------------------------------------- MAP -------------------------------------------");
        for (Territory[] territories : this.map) {
            for (int j = 0; j < this.map[0].length; j++) {
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
