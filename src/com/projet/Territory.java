package com.projet;

import java.util.ArrayList;

public class Territory {
    //Attributes
    private final int ID;
    private int playerID;
    private int strength;
    private final ArrayList<Integer> neighbor;

    //Constructor
    public Territory(int _ID, int _PlayerID, int _strength, ArrayList<Integer> _neighbor){
        this.ID = _ID;
        this.playerID = _PlayerID;
        this.strength = _strength;
        this.neighbor = _neighbor;
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

    @Override // A voir si utile plus tard
    public String toString() {
        StringBuilder result = new StringBuilder("Territory: ");
        result.append(ID);
        result.append("\nStrength: ");
        result.append(strength);
        result.append("\nNeigbors: ");
        /*for (Integer n : neighbor) {
            result.append(n);
        }*/
        System.out.println("\n");
        return result.toString();
    }
}
