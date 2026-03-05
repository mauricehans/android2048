package com.example.android2048;

public class map {
    int[][] matrice = new int[4][4];
    public void affiche(){
        System.out.println("┌────┬────┬────┬────┐");
        for (int i = 0; i <4 ; i++){
            System.out.print("|");
            for (int j = 0; j<4; j++){
                String val = matrice[i][j] == 0 ? " " : String.format("%4d", matrice[i][j]);
                System.out.print(val + "|");
            }
            System.out.println("\n├────┼────┼────┼────┤");
        }
        System.out.println("└────┴────┴────┴────┘");
    }
    public void droite() {
        for (int i = 0; i < 4; i++) {

            // Étape 1 : tasser les valeure
            int[] ligne = new int[4];
            int pos = 3;
            for (int j = 3; j >= 0; j--) {
                if (matrice[i][j] != 0) {
                    ligne[pos--] = matrice[i][j];
                }
            }

            // Étape 2 : fusionner les tuiles égales (de droite à gauche)
            for (int j = 3; j > 0; j--) {
                if (ligne[j] != 0 && ligne[j] == ligne[j - 1]) {
                    ligne[j] *= 2;
                    ligne[j - 1] = 0;
                    j--;
                }
            }

            // Étape 3 : retasser après fusion suttout pour eviter les vide
            int[] result = new int[4];
            pos = 3;
            for (int j = 3; j >= 0; j--) {
                if (ligne[j] != 0) {
                    result[pos--] = ligne[j];
                }
            }

            matrice[i] = result;
        }
    }

    public void gauche() {
        for (int i = 0; i < 4; i++) {

            // Étape 1 : tasser à gauche
            int[] ligne = new int[4];
            int pos = 0;  // curseur part de la gauche
            for (int j = 0; j < 4; j++) {
                if (matrice[i][j] != 0) {
                    ligne[pos++] = matrice[i][j];
                }
            }

            // Étape 2 : fusionner de gauche à droite
            for (int j = 0; j < 3; j++) {
                if (ligne[j] != 0 && ligne[j] == ligne[j + 1]) {
                    ligne[j] *= 2;
                    ligne[j + 1] = 0;
                    j++;
                }
            }

            // Étape 3 : retasser à gauche
            int[] result = new int[4];
            pos = 0;
            for (int j = 0; j < 4; j++) {
                if (ligne[j] != 0) {
                    result[pos++] = ligne[j];
                }
            }

            matrice[i] = result;
        }
    }

    public void haut() {
        for (int j = 0; j < 4; j++) {  // on itère sur les colonnes

            // Étape 1 : tasser vers le haut
            int[] col = new int[4];
            int pos = 0;  // curseur part du haut
            for (int i = 0; i < 4; i++) {
                if (matrice[i][j] != 0) {
                    col[pos++] = matrice[i][j];
                }
            }

            // Étape 2 : fusionner de haut en bas
            for (int i = 0; i < 3; i++) {
                if (col[i] != 0 && col[i] == col[i + 1]) {
                    col[i] *= 2;
                    col[i + 1] = 0;
                    i++;
                }
            }

            // Étape 3 : retasser vers le haut
            int[] result = new int[4];
            pos = 0;
            for (int i = 0; i < 4; i++) {
                if (col[i] != 0) {
                    result[pos++] = col[i];
                }
            }

            // Réécrire la colonne dans la matrice
            for (int i = 0; i < 4; i++) {
                matrice[i][j] = result[i];
            }
        }
    }

    public void bas() {
        for (int j = 0; j < 4; j++) {


            int[] col = new int[4];
            int pos = 3;
            for (int i = 3; i >= 0; i--) {
                if (matrice[i][j] != 0) {
                    col[pos--] = matrice[i][j];
                }
            }


            for (int i = 3; i > 0; i--) {
                if (col[i] != 0 && col[i] == col[i - 1]) {
                    col[i] *= 2;
                    col[i - 1] = 0;
                    i--;
                }
            }


            int[] result = new int[4];
            pos = 3;
            for (int i = 3; i >= 0; i--) {
                if (col[i] != 0) {
                    result[pos--] = col[i];
                }
            }


            for (int i = 0; i < 4; i++) {
                matrice[i][j] = result[i];
            }
        }
    }




}
