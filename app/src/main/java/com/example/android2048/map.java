package com.example.android2048;
import java.util.Random;

public class map {
    int[][] matrice = new int[4][4];

    // Constructeur : initialise 2 tuiles aléatoires
    public map() {
        Random rand = new Random();

        // Première case aléatoire
        int i1 = rand.nextInt(4);
        int j1 = rand.nextInt(4);

        // Deuxième case différente de la première
        int i2, j2;
        do {
            i2 = rand.nextInt(4);
            j2 = rand.nextInt(4);
        } while (i2 == i1 && j2 == j1);

        // Placer les tuiles
        matrice[i1][j1] = choisirValeur(rand);
        matrice[i2][j2] = choisirValeur(rand);
    }

    // 90% chance de 2, 10% chance de 4
    private int choisirValeur(Random rand) {
        return rand.nextInt(10) < 9 ? 2 : 4;
    }

    // Ajouter une tuile après chaque mouvement
    public void ajouterTuile() {
        Random rand = new Random();
        int i, j;
        do {
            i = rand.nextInt(4);
            j = rand.nextInt(4);
        } while (matrice[i][j] != 0);
        matrice[i][j] = choisirValeur(rand);
    }

    // Affichage terminal stylisé
    public void afficheterminal() {
        System.out.println("┌────┬────┬────┬────┐");
        for (int i = 0; i < 4; i++) {
            System.out.print("│");
            for (int j = 0; j < 4; j++) {
                String val = matrice[i][j] == 0 ? "    " : String.format("%4d", matrice[i][j]);
                System.out.print(val + "│");  // ← bug corrigé : val était ignoré
            }
            System.out.println();
            if (i < 3) {
                System.out.println("├────┼────┼────┼────┤");
            }
        }
        System.out.println("└────┴────┴────┴────┘");
    }

    public void droite() {
        for (int i = 0; i < 4; i++) {
            int[] ligne = new int[4];
            int pos = 3;
            for (int j = 3; j >= 0; j--) {
                if (matrice[i][j] != 0) ligne[pos--] = matrice[i][j];
            }
            for (int j = 3; j > 0; j--) {
                if (ligne[j] != 0 && ligne[j] == ligne[j - 1]) {
                    ligne[j] *= 2;
                    ligne[j - 1] = 0;
                    j--;
                }
            }
            int[] result = new int[4];
            pos = 3;
            for (int j = 3; j >= 0; j--) {
                if (ligne[j] != 0) result[pos--] = ligne[j];
            }
            matrice[i] = result;
        }
        ajouterTuile();  // nouvelle tuile après mouvement
    }

    public void gauche() {
        for (int i = 0; i < 4; i++) {
            int[] ligne = new int[4];
            int pos = 0;
            for (int j = 0; j < 4; j++) {
                if (matrice[i][j] != 0) ligne[pos++] = matrice[i][j];
            }
            for (int j = 0; j < 3; j++) {
                if (ligne[j] != 0 && ligne[j] == ligne[j + 1]) {
                    ligne[j] *= 2;
                    ligne[j + 1] = 0;
                    j++;
                }
            }
            int[] result = new int[4];
            pos = 0;
            for (int j = 0; j < 4; j++) {
                if (ligne[j] != 0) result[pos++] = ligne[j];
            }
            matrice[i] = result;
        }
        ajouterTuile();  // nouvelle tuile après mouvement
    }

    public void haut() {
        for (int j = 0; j < 4; j++) {
            int[] col = new int[4];
            int pos = 0;
            for (int i = 0; i < 4; i++) {
                if (matrice[i][j] != 0) col[pos++] = matrice[i][j];
            }
            for (int i = 0; i < 3; i++) {
                if (col[i] != 0 && col[i] == col[i + 1]) {
                    col[i] *= 2;
                    col[i + 1] = 0;
                    i++;
                }
            }
            int[] result = new int[4];
            pos = 0;
            for (int i = 0; i < 4; i++) {
                if (col[i] != 0) result[pos++] = col[i];
            }
            for (int i = 0; i < 4; i++) matrice[i][j] = result[i];
        }
        ajouterTuile();  // nouvelle tuile après mouvement
    }

    public void bas() {
        for (int j = 0; j < 4; j++) {
            int[] col = new int[4];
            int pos = 3;
            for (int i = 3; i >= 0; i--) {
                if (matrice[i][j] != 0) col[pos--] = matrice[i][j];
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
                if (col[i] != 0) result[pos--] = col[i];
            }
            for (int i = 0; i < 4; i++) matrice[i][j] = result[i];
        }
        ajouterTuile();  // nouvelle tuile après mouvement
    }
}
