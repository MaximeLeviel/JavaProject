package com.projet;
import java.util.Scanner;

public class Main {

    public static void main(String[] Arg){
        //Initialization
        //Créer la map (j'ai pas la foi ajd uhu)

        System.out.println("--------------- WELCOME TO RISK ---------------\n");
        Scanner sc = new Scanner(System.in);
        int nb;
        System.out.println("Number of players: ");
        do {
            System.out.println("Enter a number between 2 and 6: ");
            while (!sc.hasNextInt()) {
                System.out.println("Please enter an integer: ");
                sc.next(); // delete the last scanner
            }
            nb = sc.nextInt();
        } while (nb < 2 || nb > 6);
        System.out.println("Number of players: " + nb);

        Game game = new Game(nb);


        sc.close();
    }
}

