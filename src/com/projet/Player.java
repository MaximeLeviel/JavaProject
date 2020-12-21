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

    //Setters
    public void initTerritories(ArrayList<Territory> _territories){
        this.territories = _territories;
    }

    public void addToTerritories(Territory value){
        this.territories.add(value);
    }

    public void deleteToTerritories(int index){
        this.territories.remove(index);
    }

    //Methods
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\n-------------------- Player number ");
        result.append(ID);
        result.append("--------------------");
        result.append("\nName: ");
        result.append(name);
        result.append("\n");
        for(Territory territory : territories){
            result.append(territory.displayForPlayer());
            result.append("\n");
        }
        //result.append(territories); Plus détaillé mais prend plus de place
        System.out.println("\n");
        return result.toString();
    }


}
