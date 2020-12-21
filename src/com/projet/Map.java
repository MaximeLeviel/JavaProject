package com.projet;

import java.util.ArrayList;
import java.util.Arrays;

public class Map {
    //Attributes
    public Territory[][] map;

    //Constructor
    //TODO : Constructor avec map CSV

    public Map(int line, int column){
        this.map = new Territory[line][column];
    } //Create an empty 2D array

    //Methods
    public ArrayList<Integer> defineNeighbors(int i, int j){
        ArrayList<Integer> neighbors = new ArrayList<>();
        if (i != 0){//if the Territory is not at the first line = has neighbor at the top
            neighbors.add(this.map[i-1][j].getID());
        }
        if (i != this.map.length-1){//if the Territory is not at the last line = has neighbor at the bottom
            neighbors.add(this.map[i+1][j].getID());
        }
        if(j != 0){
            neighbors.add(this.map[i][j-1].getID());
        }
        if (j != ((this.map[0].length) - 1)){//if the Territory is not at the last column = has neighbor at the right
            neighbors.add(this.map[i][j+1].getID());
        }
        return neighbors;
    }

    public void createMap(){ //Fill the array with territories
        int cpt = 1;
        for(int i = 0; i < this.map.length; i++){
            for(int j = 0; j < this.map[0].length; j++){
                this.map[i][j] = new Territory(cpt, 0, 0, null);
                cpt++;
            }
        }
        for(int i = 0; i < this.map.length; i++){
            for(int j = 0; j < this.map[0].length; j++){
                this.map[i][j].setNeighbors(defineNeighbors(i,j)); //add the neighbors
            }
        }
    }
}
