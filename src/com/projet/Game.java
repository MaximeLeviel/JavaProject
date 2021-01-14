package com.projet;

import java.util.*;

public class Game {
    //Attributes
    protected final int nbPlayer;
    protected final int nbTerritories;
    protected ArrayList<Player> players;

    //Constructor
    public Game(int _nbPlayer, int _nbTerritories) {
        this.nbPlayer = _nbPlayer;
        this.nbTerritories = _nbTerritories;
    }

    //Methods for players
    public void createPlayers() {
        this.players = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < nbPlayer; i++) {

            System.out.println("\n------ Player number " + (i+1) + " ------");

            System.out.println("Enter the name of the player:");
            String name = sc.nextLine();

            Player player = new Player(i, name);
            this.players.add(player);
        }
    }

    public void displayPlayers(){
        for (Player player : this.players) {
            System.out.println(player);
        }
    }

    //find the territory in map and sets the playerID
    public Territory findTerritoryByID(Territory[][] map, int ID) throws Maps.NonexistentIdException {
        for(Territory[] t : map){
            for(Territory territory : t){
                if(ID == territory.getID()){ //when we found the territory selected in map
                    return territory;
                }
            }
        }
        throw new Maps.NonexistentIdException("Id doesn't exist.");
    }

    //Methods for map
    public void initMap(Territory[][] map) { //Give the strength and players to each territory
        int excess = nbTerritories % nbPlayer;
        int totalDices = (nbTerritories/nbPlayer)*5; //Number of dices per player = average of 5 dices per territory
        int strength, territoryId;
        Territory territory;
        ArrayList<Integer> takenTerritories = new ArrayList<>();

        for (Player player : players){
            for (int i = 0; i < nbTerritories/nbPlayer; i++) {
                do{
                    territoryId = (int) (Math.random() * nbTerritories + 1); //associate a random territory (from 1 to nbTerritories)
                }
                while(takenTerritories.contains(territoryId)); //Draw until the territory is not already taken
                takenTerritories.add(territoryId);  //The territory is added to the list of taken territories

                try {
                    territory = findTerritoryByID(map, territoryId); //returns the territory associated to the territoryID
                    territory.setPlayerID(player.getID());
                    player.addToTerritories(territory); //associate the territory to the player
                } catch (Maps.NonexistentIdException e) {
                    System.out.println("The territory has not been found");
                }
            }
        }

        if(excess !=0){
            int cpt = 0;
            do{
                for(int i = 1; i <= nbTerritories; i++){
                    if (!takenTerritories.contains(i)){
                        takenTerritories.add(i);//We add the territory has a taken territory
                        try {
                            territory = findTerritoryByID(map, i); //returns the territory associated to the territoryID
                            territory.setPlayerID(-1); //We block the territory
                            cpt++;
                        } catch (Maps.NonexistentIdException e) {
                            System.out.println("The territory has not been found"); //Should never happen
                        }
                    }
                }
                System.out.println("TEST");
            }while(cpt != excess);
        }

        int cpt;
        //Dispatch the dices to the territories for each players
        for (Player p : players) {
            cpt = 1;
            for (Map.Entry<Integer, Territory> entry : p.getTerritories().entrySet()) {
                do {
                    strength = (int) (Math.random() * 8 + 1); //between 1 and 8

                } while ((totalDices - (p.getNbDices() + strength) < (p.getTerritories().size()-cpt))// [number of remaining dices < number of remaining territories] = not enough dices for remaining territories
                        || (totalDices - (p.getNbDices() + strength) > 8 * (p.getTerritories().size()-cpt)));//Or [number of remaining dices > 8 * number of remaining territories] = too much dices left for remaining territories

                cpt++;
                p.setNbDices(strength + p.getNbDices());//Add the dices to player
                entry.getValue().setStrength(strength);//associate a strength to the territory
            }
        }
    }



    public void displayMap(Territory[][] map){
        System.out.println("------------------------------------------- MAP -------------------------------------------");
        for (Territory[] territories : map) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(territories[j].displayTerritory() + "  ");
            }
            System.out.println("");
        }
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("[] : ID of the territory");
        System.out.println("{} : Strength of the territory");
        System.out.println("() : Owner of the territory");
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    public boolean endCondition(){
        //If a player has all territories
        for(Player player : players){
            int var = nbTerritories % nbPlayer; //number of territories not distributed
            if(player.getTerritories().size() == nbTerritories - var){ //If a player has all territories except the ones not distributes
                return true;//Then the game ends
            }
        }
        return false;
    }

    public static int checkInput(int min, int max){ //Check the inputs of the user if expected an integer with min and max values
        int input;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Enter a number between " + min +" and "+ max +": ");
            while (!sc.hasNextInt()) {
                System.out.println("Please enter an integer: ");
                sc.next(); // delete the last scanner
            }
            input = sc.nextInt();
        } while (input < min || input > max);
        return input;
    }

    public int throwDices(int nbDices){//Simulate the throw of nbDices of dices
        int dices = 0;
        for(int i = 0; i < nbDices; i++){
            int dice = (int) (Math.random() * 6 + 1); //between 1 and 6
            dices = dices + dice;
        }
        return dices;
    }

    //attack between 2 territories
    public boolean attack(Player playerAttacker, ArrayList<Integer> listAttack, Territory[][] map){
        boolean win = true;
        Territory attacker, defender = null;
        Player playerDefender = null;
        attacker = playerAttacker.getTerritories().get(listAttack.get(0));
        outerloop:
        for(Territory[] t : map){//We need to get the Territory of ID defender
            for(Territory territory : t){
                if(territory.getID() == listAttack.get(1)){
                    defender = territory;
                    playerDefender = players.get(territory.getPlayerID());
                    break outerloop; //allows to break out of the nested for loops once we've found our territory
                }
            }
        }

        System.out.println("\n----------- Territory "+ listAttack.get(0) + " attacks territory " + listAttack.get(1) + " -----------");
        System.out.println("\nAttacker:\nPlayer " + (attacker.getPlayerID() + 1) + "\nNumber of dices: " + attacker.getStrength());
        System.out.println("\nDefender:\nPlayer " + (defender.getPlayerID() + 1) + "\nNumber of dices: " + defender.getStrength());

        System.out.println("Press enter to throw the dices");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        int diceAttacker = throwDices(attacker.getStrength());
        int diceDefender = throwDices(defender.getStrength());

        System.out.println("Dices of the attacker : " + diceAttacker);
        System.out.println("Dices of the defender : " + diceDefender);

        if(diceAttacker > diceDefender){
            System.out.println("======================================================================\nThe attacker won the battle, the territory " + (defender.getID()) + " now belongs to player " + (playerAttacker.getID() + 1) + "\n======================================================================");
            playerDefender.setNbDices(playerDefender.getNbDices() - defender.getStrength()); //We remove the number of dices of the defending territory for the defender
            defender.setStrength(attacker.getStrength()-1); //We change the number of dices of the defending territories by the remaining dices
            attacker.setStrength(1); //We leave one dice to the attacking territory
            defender.setPlayerID(attacker.getPlayerID()); // The defending territory now belongs to the attacker
            playerAttacker.addToTerritories(defender); // We add this territory to the territories of the attacker
            playerDefender.deleteToTerritories(defender.getID()); //We remove the territory from the defender

            if(playerDefender.getTerritories().size() == 0){ //If the defender has lost all of their territories
                System.out.println("\n######################################################\nplayer " + playerDefender.getID() + " has lost all their territories, RIP " + playerDefender.getName() + "\n######################################################");
            }
        }
        else{
            System.out.println("======================================================================\nThe attacker looses the battle, player number " + (attacker.getPlayerID() + 1) + " looses " + (attacker.getStrength()-1) + " dices\n======================================================================");
            win = false;
            playerAttacker.setNbDices(playerAttacker.getNbDices() - (attacker.getStrength()-1));//We remove the dices of the territory from the player (except 1)
            attacker.setStrength(1); //We replace the dices of the attacking territories by 1
        }
        return win;
    }

    //Distribute the bonus dices to the territories
    public void distributeDices(Player player, int bonusDices){

        //If the number of dices with the bonus dices is greater than 8 dices per territory
        if ((player.getNbDices() + bonusDices) > 8 * player.getTerritories().size()){
            System.out.println("Oh no !, it seems like your territories can not take all of the bonusDices...");
            for (Map.Entry<Integer, Territory> entry : player.getTerritories().entrySet()) {
                entry.getValue().setStrength(8);//We set the strength of all territories to 8 as a maximum
            }
            player.setNbDices(8 * player.getTerritories().size()); //We set the number of dices to its maximum
        }

        else{
            Territory territory;
            Integer randomKey;
            Random random;
            List<Integer> keys = new ArrayList<Integer>(player.getTerritories().keySet()); //Make a list of the keys to chose randomly

            for(int i = 0; i < bonusDices; i++){//For each bonus dice
                do{
                    random = new Random();
                    randomKey = keys.get( random.nextInt(keys.size()) );
                    territory = player.getTerritories().get(randomKey);
                }while(territory.getStrength() == 8); //if the territory has already 8 dices draw again
                territory.setStrength(territory.getStrength()+1);//Add one dice to the territory
            }
            player.setNbDices(player.getNbDices() + bonusDices);//Add the bonusDices to the number of dices
        }
    }

    //Compute the number of bonus dices to add
    public void bonusDices(Player player){
        HashMap< Integer, ArrayList<Territory> > contiguous = new HashMap<>();//Map with key = ID of each territory in territories and Value = direct contiguous territories
        for (Map.Entry<Integer, Territory> entry : player.getTerritories().entrySet()) { //for each territory of player
            contiguous.put(entry.getKey(), new ArrayList<>()); //We create an array which represents their direct contiguous territories that belong to the player
            contiguous.get(entry.getKey()).add(entry.getValue());//We add the studied territory to the array of contiguous territories
            for(Integer neighbor : entry.getValue().getNeighbors()){//for each neighbor of the territory
                if (player.getTerritories().containsKey(neighbor)){//If the neighbor territory belongs to the player
                    contiguous.get(entry.getKey()).add(player.getTerritories().get(neighbor));//add the territory to the array
                }
            }
        }

        for (Map.Entry<Integer, ArrayList<Territory>> entry : contiguous.entrySet()) {
            int cpt = 1;
            //TODO : optimiser pour ne pas checker les territoires qui on déja été checker dans entry precedent
            while(cpt < contiguous.get(entry.getKey()).size()){
                //TODO : optimiser pour ne pas checker index 1
                for(Territory territory : contiguous.get(entry.getValue().get(cpt).getID())){//adds the not direct contiguous territories to contiguous
                    if(!entry.getValue().contains(territory)){//if the territory is not already in the list of contiguous then we add it
                        entry.getValue().add(territory);
                    }
                }
                cpt++;
            }
        }
        int maxContiguous = 1;
        for (Map.Entry<Integer, ArrayList<Territory>> entry : contiguous.entrySet()) {
            if ((entry.getValue().size() > maxContiguous)){
                maxContiguous = entry.getValue().size();
            }
        }
        System.out.println("==============================\nYou get a bonus of " + maxContiguous + " dices\n==============================");
        distributeDices(player, maxContiguous);//call the function to distribute the dices randomly
    }

    //MAIN
    public static void main(String[] Arg) {
        int opt = 1;
        do {
            int nbPlayer, line, column;

            System.out.println("=================================== WELCOME TO RISK ===================================\n");

            System.out.println("-------------- NUMBER OF PLAYERS --------------\n");
            nbPlayer = checkInput(2, 6);
            System.out.println("\nNumber of players: " + nbPlayer);

            int nbTerritories;

            //Creation of the map
            Maps myMap;
            System.out.println("\nDo you want to load the map from a CSV file ? \n0.Yes\n1.No");
            Scanner sc = new Scanner(System.in);
            int response = checkInput(0,1);

            if (response == 0){
                System.out.print("\nEnter the name of the CSV file.\n");
                String name = sc.nextLine();
                myMap = new Maps(name + ".csv");
                nbTerritories = myMap.map.length * myMap.map[0].length;
            }
            else{
                System.out.println("\n--------------- SIZE OF THE MAP ---------------\n");
                System.out.println("Enter the number of lines: ");
                line = checkInput(4, 9);
                System.out.println("Enter the number of columns: ");
                column = checkInput(4, 10);
                nbTerritories = line*column;

                myMap = new Maps(line, column); //Create a map of dimension line*column (empty)
                myMap.createMap(); //Fill the map with Territories ID
            }

            //Creation of the game
            System.out.println("\n------------- NAME OF THE PLAYERS -------------");
            Game game = new Game(nbPlayer, nbTerritories);

            //Creation of the players
            game.createPlayers();
            game.initMap(myMap.map); //Fill the territories with a strength and a player's ID
            myMap.initNeighbors(); //Fill the neighbors for each territory



            //Display
            System.out.println("\n----------------------------------------- INITIALIZATION -----------------------------------------\n");
            game.displayMap(myMap.map);
            game.displayPlayers();

            //Just for better display
            System.out.println("Press enter to continue");
            String input = sc.nextLine();


            System.out.println("\n------------------------------------ START OF THE GAME ------------------------------------");
            int player = (int) (Math.random() * nbPlayer);
            System.out.println("Player " + (player+1) + " will begin:");

            do{
                boolean win = true;
                int turn = 1;
                do{
                    game.displayMap(myMap.map);
                    System.out.print("\n--------------------- PLAYER " + (player + 1) + ", IT'S TIME TO ATTACK ! ---------------------");
                    System.out.println(game.players.get(player));
                    turn = game.players.get(player).endTurn(game);

                    if(turn == 1){//If the player wants to attack
                        ArrayList<Integer> listAttack;
                        listAttack = game.players.get(player).attackTerritories(myMap); //select the territory that will attack and the defender

                        if(listAttack != null && listAttack.size() != 0){//If the player can attack
                            win = game.attack(game.players.get(player), listAttack, myMap.map); //launch the attack
                        }

                        else{
                            game.bonusDices(game.players.get(player)); //If player decides to stop, they get bonus dices
                            turn = 0;
                        }
                    }

                    else{//If the player wants to stop attacking
                        game.bonusDices(game.players.get(player)); //If player decides to stop, they get bonus dices
                    }

                    //Just for better display
                    System.out.println("Press enter to continue");
                    sc = new Scanner(System.in);
                    input = sc.nextLine();

                }while(win && turn != 0);

                System.out.print("-------------------- Here is your army after you played --------------------");
                System.out.println(game.players.get(player));

                //Just for better display
                System.out.println("Press enter to continue");
                sc = new Scanner(System.in);
                input = sc.nextLine();

                do{
                    if(player != (nbPlayer-1)){//if we are not at the last player's turn
                        player = player + 1; //We go to the next player
                    }
                    else{
                        player = 0; //We go back to first player
                    }
                }while(game.players.get(player).getTerritories().size() == 0);// go to next player if player has lost all their territories

            }while(!game.endCondition());

            System.out.println("\n------------------------------------ END OF THE GAME ------------------------------------");
            System.out.println("\n====================================\n====================================\nCongratulation " + game.players.get(player).getName() + " your army has won ! ^(^u^)^ ^(^u^)^ ^(^u^)^ \n====================================\n====================================");
            System.out.println("\n\nDo you want to start a new game ? : 1. Yes  0. No");
            opt = checkInput(0, 1);
        }
        while(opt != 0);


    }
}
