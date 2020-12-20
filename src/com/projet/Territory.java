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
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Territory: ");
        result.append(ID);
        result.append("\nStrength: ");
        result.append(strength);
        result.append("\nNeigbors: ");
        for (Integer n : neighbor) {
            result.append(n);
        }
        System.out.println("\n");
        return result.toString();
    }
}
