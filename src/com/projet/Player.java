package com.projet;

import java.util.*;

public class Player {
    //Attributes
    private final int ID;
    private final String name;
    private final TreeMap<Integer, Territory> territories;
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

    public Territory getTerritory(int id){
        return territories.get(id);
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
    public static int checkList(ArrayList<Integer> list) throws OutOfListException, ZeroInputException {
        int input;
        Scanner sc = new Scanner(System.in);
        System.out.println("The number must be between this values : " + list);
        while (!sc.hasNextInt()) {
            System.out.println("Please enter an integer: ");
            sc.next(); // delete the last scanner
        };
        input = sc.nextInt();
        if(input == 0){
            throw new ZeroInputException("The input is negative.");
        }
        if(!list.contains(input)){
            throw new OutOfListException("Input out of list.");
        }
        return input;
    }


    private ArrayList<Integer> getAttackant(){
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
        return listAttack;
    }

    //Ask the user for the territory which will attack and the territory which will defend
    public ArrayList<Integer> attackTerritories(Maps myMap){
        ArrayList<Integer> listAttack = getAttackant();

        int attacker;

        try{
            attacker = attackFrom(listAttack);

            listAttack = getDefenders(attacker);

            System.out.println("Which territory do you want to attack ? (Enter 0 if you want to change the territory you attack from)");

            int defender;

            while(true){ //keep asking user if wrong selected territory
                try{
                    defender = checkList(listAttack);
                    break;//stop the while if no exception
                }
                catch (OutOfListException e) {
                    System.out.println("You can't attack the territory you chose. Please select one in the list.");
                }
                catch (ZeroInputException e){
                    return attackTerritories(myMap);
                }
            }

            listAttack.clear();//We reuse listAttack to return 2 variables
            listAttack.add(attacker);
            listAttack.add(defender);
            return listAttack;
        }
        catch (ImpossibleAttackException e){
            System.out.println("Sorry you can not attack in this conditions, maybe next time ^^'");
        }
        return null;
    }

    private int attackFrom(ArrayList<Integer> listAttack) throws ImpossibleAttackException{

        //If no territories can attack
        if (listAttack.size() == 0){
            throw new ImpossibleAttackException("Impossible attack.");
        }

        System.out.println("From which territory will you attack ?");

        int attacker;

        while(true){ //keep asking user if wrong selected territory
            try{
                attacker = checkList(listAttack);
                break;//stop the while if no exception
            }
            catch(OutOfListException | ZeroInputException e){
                System.out.print("You can't attack from the territory you chose. Please select one in the list.\n");
            }
        }

        listAttack.clear(); //Clear to use it as the List of IDs of territories that can be attacked from a selected territory
        return attacker;
    }

    public ArrayList<Integer> getDefenders(int attacker){
        ArrayList<Integer> listAttack = new ArrayList<>();
        for(Integer n : territories.get(attacker).getNeighbors()){//for all neighbors of the attacker territory
            if(!territories.containsKey(n)){//Only select those who don't belong to the player
                listAttack.add(n);
            }
        }
        return listAttack;
    }

    public boolean isAttackable(int id, int attacker){
        ArrayList<Integer> listAttack = getDefenders(attacker);
        return listAttack.contains(id);
    }

    public static class ImpossibleAttackException extends Throwable {
        public ImpossibleAttackException(String errorMessage){
            super(errorMessage);
        }
    }

    public static class OutOfListException extends Throwable {
        public OutOfListException(String errorMessage){
            super(errorMessage);
        }
    }

    public static class ZeroInputException extends Throwable {
        public ZeroInputException(String errorMessage){
            super(errorMessage);
        }
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

        System.out.println("\n");
        return result.toString();
    }


}
