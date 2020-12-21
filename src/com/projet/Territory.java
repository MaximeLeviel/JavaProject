package com.projet;

import java.util.ArrayList;

public class Territory {
    //Attributes
    private final int ID;
    private int playerID;
    private int strength;
    private ArrayList<Integer> neighbors;

    //Constructor
    public Territory(int _ID, int _PlayerID, int _strength, ArrayList<Integer> _neighbors){
        this.ID = _ID;
        this.playerID = _PlayerID;
        this.strength = _strength;
        this.neighbors = _neighbors;
    }

    //Getters
    public int getID(){
        return this.ID;
    }

    public int getPlayerID(){
        return this.playerID;
    }

    //Setters
    public void setNeighbors(ArrayList<Integer> _neighbors){
        this.neighbors = _neighbors;
    }

    public void setStrength(int _strength){
        this.strength = _strength;
    }

    public void setPlayerID(int _playerID){
        this.playerID = _playerID;
    }

    //Methods
    public String displayTerritory(){ //Display dans la carte
        if(this.ID < 10 && this.strength < 10){
            return "[" + this.ID + "]{" + this.strength + "}(" + this.playerID + ")  ";
        }
        else if (this.ID < 10 || this.strength < 10){
            return "[" + this.ID + "]{" + this.strength + "}(" + this.playerID + ") ";
        }
        else{
            return "[" + this.ID + "]{" + this.strength + "}(" + this.playerID + ")";
        }
    }

    public String displayForPlayer(){
        StringBuilder result = new StringBuilder("Territory [" + this.ID + "]: strength {" + this.strength + "} neighbors: ");
        result.append(neighbors);
        return result.toString();
    }

    @Override // A voir si utile plus tard
    public String toString() {
        StringBuilder result = new StringBuilder("\n\n------ Territory ");
        result.append(ID);
        result.append("------");
        result.append("\nStrength: ");
        result.append(strength);
        result.append("\nNeigbors: ");
        result.append(neighbors);
        result.append("\nBelongs to player: ");
        result.append(playerID);
        System.out.println("\n");
        return result.toString();
    }
}
