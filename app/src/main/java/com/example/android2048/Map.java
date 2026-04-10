package com.example.android2048;

import java.util.Random;

public class Map {
    int[][] matrice = new int[4][4];
    int score = 0;

    public Map() {
        Random rand = new Random();
        int i1 = rand.nextInt(4);
        int j1 = rand.nextInt(4);
        int i2, j2;
        do {
            i2 = rand.nextInt(4);
            j2 = rand.nextInt(4);
        } while (i2 == i1 && j2 == j1);
        matrice[i1][j1] = choisirValeur(rand);
        matrice[i2][j2] = choisirValeur(rand);
    }

    public Map(int[] flat, int score) {
        this.score = score;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                matrice[i][j] = flat[i * 4 + j];
    }

    private int choisirValeur(Random rand) {
        double prob4 = 0.1 + 0.4 * Math.log(score + 1) / Math.log(2049);
        return rand.nextDouble() < prob4 ? 4 : 2;
    }

    // Copie profonde de la matrice pour comparaison avant/après
    private int[][] copierMatrice() {
        int[][] copie = new int[4][4];
        for (int i = 0; i < 4; i++)
            copie[i] = matrice[i].clone();
        return copie;
    }

    // Compare deux matrices : retourne true si elles sont différentes
    private boolean aChange(int[][] avant) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (matrice[i][j] != avant[i][j]) return true;
        return false;
    }

    public void ajouterTuile() {
        boolean caseVide = false;
        for (int[] row : matrice)
            for (int v : row)
                if (v == 0) { caseVide = true; break; }
        if (!caseVide) return;

        Random rand = new Random();
        int i, j;
        do {
            i = rand.nextInt(4);
            j = rand.nextInt(4);
        } while (matrice[i][j] != 0);
        matrice[i][j] = choisirValeur(rand);
    }

    public boolean aGagne() {
        for (int[] row : matrice)
            for (int v : row)
                if (v == 2048) return true;
        return false;
    }

    public boolean estBloque() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (matrice[i][j] == 0) return false;
                if (i < 3 && matrice[i][j] == matrice[i + 1][j]) return false;
                if (j < 3 && matrice[i][j] == matrice[i][j + 1]) return false;
            }
        return true;
    }

    public void afficheterminal() {
        System.out.println("┌────┬────┬────┬────┐");
        for (int i = 0; i < 4; i++) {
            System.out.print("│");
            for (int j = 0; j < 4; j++) {
                String val = matrice[i][j] == 0 ? "    " : String.format("%4d", matrice[i][j]);
                System.out.print(val);
                System.out.print("│");
            }
            System.out.println();
            if (i < 3) System.out.println("├────┼────┼────┼────┤");
        }
        System.out.println("└────┴────┴────┴────┘");
    }

    public void droite() {
        int[][] avant = copierMatrice();
        for (int i = 0; i < 4; i++) {
            int[] ligne = new int[4];
            int pos = 3;
            for (int j = 3; j >= 0; j--)
                if (matrice[i][j] != 0) ligne[pos--] = matrice[i][j];
            for (int j = 3; j > 0; j--) {
                if (ligne[j] != 0 && ligne[j] == ligne[j - 1]) {
                    ligne[j] *= 2;
                    score += ligne[j];
                    ligne[j - 1] = 0;
                    j--;
                }
            }
            int[] result = new int[4];
            pos = 3;
            for (int j = 3; j >= 0; j--)
                if (ligne[j] != 0) result[pos--] = ligne[j];
            matrice[i] = result;
        }
        if (aChange(avant)) ajouterTuile(); // ← uniquement si la grille a changé
    }

    public void gauche() {
        int[][] avant = copierMatrice();
        for (int i = 0; i < 4; i++) {
            int[] ligne = new int[4];
            int pos = 0;
            for (int j = 0; j < 4; j++)
                if (matrice[i][j] != 0) ligne[pos++] = matrice[i][j];
            for (int j = 0; j < 3; j++) {
                if (ligne[j] != 0 && ligne[j] == ligne[j + 1]) {
                    ligne[j] *= 2;
                    score += ligne[j];
                    ligne[j + 1] = 0;
                    j++;
                }
            }
            int[] result = new int[4];
            pos = 0;
            for (int j = 0; j < 4; j++)
                if (ligne[j] != 0) result[pos++] = ligne[j];
            matrice[i] = result;
        }
        if (aChange(avant)) ajouterTuile();
    }

    public void haut() {
        int[][] avant = copierMatrice();
        for (int j = 0; j < 4; j++) {
            int[] col = new int[4];
            int pos = 0;
            for (int i = 0; i < 4; i++)
                if (matrice[i][j] != 0) col[pos++] = matrice[i][j];
            for (int i = 0; i < 3; i++) {
                if (col[i] != 0 && col[i] == col[i + 1]) {
                    col[i] *= 2;
                    score += col[i];
                    col[i + 1] = 0;
                    i++;
                }
            }
            int[] result = new int[4];
            pos = 0;
            for (int i = 0; i < 4; i++)
                if (col[i] != 0) result[pos++] = col[i];
            for (int i = 0; i < 4; i++) matrice[i][j] = result[i];
        }
        if (aChange(avant)) ajouterTuile();
    }

    public void bas() {
        int[][] avant = copierMatrice();
        for (int j = 0; j < 4; j++) {
            int[] col = new int[4];
            int pos = 3;
            for (int i = 3; i >= 0; i--)
                if (matrice[i][j] != 0) col[pos--] = matrice[i][j];
            for (int i = 3; i > 0; i--) {
                if (col[i] != 0 && col[i] == col[i - 1]) {
                    col[i] *= 2;
                    score += col[i];
                    col[i - 1] = 0;
                    i--;
                }
            }
            int[] result = new int[4];
            pos = 3;
            for (int i = 3; i >= 0; i--)
                if (col[i] != 0) result[pos--] = col[i];
            for (int i = 0; i < 4; i++) matrice[i][j] = result[i];
        }
        if (aChange(avant)) ajouterTuile();
    }
}