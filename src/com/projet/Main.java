package com.projet;
import java.util.Scanner;

public class Main {

    public static int checkInput(int min, int max){
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

    public static void main(String[] Arg){ //Je pense que ça sera a mettre dans la classe game directement
        //Initialization
        int nb, nb2;

        System.out.println("=============== WELCOME TO RISK ===============\n");

        System.out.println("-------------- NUMBER OF PLAYERS --------------\n");
        nb = checkInput(2, 6);
        System.out.println("\nNumber of players: " + nb);

        Game game = new Game(nb);
        game.createPlayers();
        game.displayPlayers();

        System.out.println("\n--------------- SIZE OF THE MAP ---------------\n");
        System.out.println("Enter the number of lines: ");
        nb = checkInput(4, 9);
        System.out.println("Enter the number of columns: ");
        nb2 = checkInput(4, 10);

        Map map = new Map(nb,nb2);
        map.initMap();
        map.displayMap();


    }
}

