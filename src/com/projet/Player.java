package com.projet;

import java.util.ArrayList;

public class Player {
    //Attributes
    private final int ID;
    private final String name;
    private ArrayList<Territory> territories;
    private int nbDices;

    //Constructor
    public Player(int _ID, String _name){
        this.ID = _ID;
        this.name = _name;
        this.territories = new ArrayList<>();
        this.nbDices = 0;
    }

    //Getters
    public ArrayList<Territory> getTerritories(){
        return this.territories;
    }

    public int getNbDices(){
        return this.nbDices;
    }

    //Setters
    public void setNbDices(int _nbDices){
        this.nbDices = _nbDices;
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
        StringBuilder result = new StringBuilder("-------------------- Player number ");
        result.append(ID+1);
        result.append(" --------------------");
        result.append("\nName: ");
        result.append(name);
        result.append("\n");
        result.append("Number of territory: " + territories.size() + "\n");
        result.append("Number of dices: " + nbDices + "\n\n");
        for(Territory territory : territories){
            result.append(territory.displayForPlayer());
            result.append("\n");
        }

        //result.append(territories); Plus détaillé mais prend plus de place
        System.out.println("\n");
        return result.toString();
    }


}
