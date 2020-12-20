package com.projet;

import java.util.ArrayList;

public class Player {
    //Attributes
    private int ID;
    private String name;
    private ArrayList<Territory> territories;

    //Constructor
    public Player(int _ID, String _name, ArrayList<Territory> _territories){
        this.ID = _ID;
        this.name = _name;
        this.territories = _territories;
    }

    //Methods
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Player: ");
        result.append(ID);
        result.append("\nName: ");
        result.append(name);
        result.append("\nTerritories: ");
        /*for (Territory t : territories) {
            result.append(t);
        }
        */
        System.out.println("\n");
        return result.toString();
    }


}
