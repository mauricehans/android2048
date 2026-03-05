package com.example.android2048;

public class map {
    int[][] matrice = new int[4][4];
    public void affiche(){
        System.out.println("┌────┬────┬────┬────┐");
        for (int i = 0; i <4 ; i++){
            System.out.print("|");
            for (int j = 0; j<4; j++){
                String val = matrice[i][j] == 0 ? " " : String.format("%4d", matrice[i][j]);

            }
            System.out.println("\n├────┼────┼────┼────┤");
        }
        System.out.println("└────┴────┴────┴────┘");
    }
}
