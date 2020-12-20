package com.projet;

import java.util.ArrayList;

public class Territory {
    //Attributes
    private final int ID;
    private int playerID;
    private int strength;
    private final ArrayList<String> neighbor;

    //Constructor
    public Territory(int _ID, int _PlayerID, int _strength, ArrayList<String> _neighbor){
        this.ID = _ID;
        this.playerID = _PlayerID;
        this.strength = _strength;
        this.neighbor = _neighbor;
    }

    //Methods

}
