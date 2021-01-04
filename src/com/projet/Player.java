package com.projet;

import java.util.*;

public class Player {
    //Attributes
    private final int ID;
    private final String name;
    private TreeMap<Integer, Territory> territories;
    private int nbDices;

    //Constructor
    public Player(int _ID, String _name){
        this.ID = _ID;
        this.name = _name;
        this.territories = new TreeMap<>();
        this.nbDices = 0;
    }

    //Getters
    public Map<Integer, Territory> getTerritories(){
        return this.territories;
    }

    public int getNbDices(){
        return this.nbDices;
    }

    public int getID(){
        return this.ID;
    }

    public String getName(){
        return this.name;
    }

    //Setters
    public void setNbDices(int _nbDices){
        this.nbDices = _nbDices;
    }
    public void addToTerritories(Territory value){
        this.territories.put(value.getID(),value);
    }
    public void deleteToTerritories(int index){
        this.territories.remove(index);
    }

    //Methods

    //Check the inputs of the user if expected an integer with an array of possible values
    public static int checkList(ArrayList<Integer> list){
        int input;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("The number must be between this values : " + list);
            while (!sc.hasNextInt()) {
                System.out.println("Please enter an integer: ");
                sc.next(); // delete the last scanner
            }
            input = sc.nextInt();
        } while (!list.contains(input));
        return input;
    }

    //Ask the user for the territory which will attack and the territory which will defend
    //TODO : if no territory can attack
    public ArrayList<Integer> attackTerritories(){
        ArrayList<Integer> listAttack = new ArrayList<>();//List of IDs of territories that can attack
        for (Map.Entry<Integer, Territory> entry : territories.entrySet()){//For each territory of the player
            if(entry.getValue().getStrength() != 1){//Strength must not be 1 to attack
                int cpt = 0;
                for(Integer n : entry.getValue().getNeighbors()){//For each neighbors of that territory
                    if(territories.containsKey(n)){
                        cpt++;
                    };
                }
                if(cpt != entry.getValue().getNeighbors().size()){//Can attack only if all neighbors are not owned by the player
                    listAttack.add(entry.getKey());//This territory can attack
                }
            }
        }
        System.out.println("From which territory will you attack ?");

        //If no territories can attack
        if (listAttack.size() == 0){
            System.out.println("Sorry you can not attack in this conditions, maybe next time ^^'");
            return listAttack;
        }

        int attacker = checkList(listAttack);

        listAttack.clear(); //Clear to use it as the List of IDs of territories that can be attacked from a selected territory
        for(Integer n : territories.get(attacker).getNeighbors()){//for all neighbors of the attacker territory
            if(!territories.containsKey(n)){//Only select those who don't belong to the player
                listAttack.add(n);
            }
        }
        System.out.println("Which territory do you want to attack ?");
        //TODO : faire en sorte que le joueur puisse voir le nombre de dés des territoires voisins
        //TODO : pouvoir retourner en arrière pour selectionner un autre territoire
        int defender = checkList(listAttack);

        listAttack.clear();//We reuse listAttack to return 2 variables
        listAttack.add(attacker);
        listAttack.add(defender);
        return listAttack;
    }

    public int endTurn(Game game){//We must have access to game to use the function checkinput
        int turn = 1;
        System.out.println("Do you want to attack ? 1. Yes 0. No");
        turn = Game.checkInput(0, 1);
        return turn;
    }


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
        for(Territory value : territories.values()){
            result.append(value.displayForPlayer());
            result.append("\n");
        }
        result.append("---------------------------------------------------------");

        //result.append(territories); Plus détaillé mais prend plus de place
        System.out.println("\n");
        return result.toString();
    }


}
