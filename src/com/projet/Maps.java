package com.projet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Maps {
    //Attributes
    public Territory[][] map;

    //Constructor
    Maps(String path){
        ArrayList<ArrayList<Territory>> tmpMap = new ArrayList<>();
        try {
            File CSV = new File(path);
            Scanner table = new Scanner(CSV);
            int i = 0;
            while (true){
                String line = table.nextLine();
                String[] elements = line.split(",");

                tmpMap.add(new ArrayList<>());

                for(int j = 0; j < elements.length; j++){
                    String[] datastr1 = elements[j].split("-");
                    int id;
                    if (j == 0 && i == 0){
                        id = Integer.parseInt(datastr1[0].substring(1, 2));
                    }
                    else{
                        id = Integer.parseInt(datastr1[0]);
                    }
                    String[] datastr2 = datastr1[1].split("/");
                    ArrayList<Integer> data = new ArrayList<>();
                    for (int z = 0; z < datastr2.length; z++){
                        data.add(Integer.parseInt(datastr2[z]));
                    }
                    tmpMap.get(i).add(new Territory(id,  0, 0, data));
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.print("The file with the specified name doesn't exist, please specify a new one. \n");
        } catch (NoSuchElementException e){
            this.map = new Territory[tmpMap.size()][tmpMap.get(0).size()];
            for (int i = 0; i < this.map.length; i++){//convert the temporary ArrayList into final Array
                for (int j = 0; j < this.map[i].length; j++){
                    this.map[i][j] = tmpMap.get(i).get(j);
                }
            }
            System.out.print("The map was successfully loaded. \n");
        }
    }

    public Maps(int line, int column){
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

    public int findPlayerById(int id) throws NonexistentIdException {
        for(int i = 0; i < this.map.length; i++){
            for(int j = 0; j < this.map[i].length; j++){
                return map[i][j].getPlayerID();
            }
        }
        throw new NonexistentIdException("Id doesn't exist.");
    }

    public static class NonexistentIdException extends Throwable {
        public NonexistentIdException(String errorMessage){
            super(errorMessage);
        }
    }
}
