package com.example.android2048;

import java.util.Random;

public class Map {
    int[][] matrice = new int[4][4];
    int score = 0;
    int objectif = 2048;
    boolean objectifAtteint = false;

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

    private int choisirValeur(Random rand) {
        return rand.nextInt(10) < 9 ? 2 : 4;
    }

    private void ajouterTuile() {
        boolean libre = false;
        for (int[] row : matrice)
            for (int v : row)
                if (v == 0) { libre = true; break; }
        if (!libre) return;

        Random rand = new Random();
        int i, j;
        do {
            i = rand.nextInt(4);
            j = rand.nextInt(4);
        } while (matrice[i][j] != 0);
        matrice[i][j] = choisirValeur(rand);
    }

    public boolean peutJouer() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (matrice[i][j] == 0) return true;
                if (j < 3 && matrice[i][j] == matrice[i][j + 1]) return true;
                if (i < 3 && matrice[i][j] == matrice[i + 1][j]) return true;
            }
        return false;
    }

    private int[][] copier() {
        int[][] c = new int[4][4];
        for (int i = 0; i < 4; i++) c[i] = matrice[i].clone();
        return c;
    }

    private boolean matricesEgales(int[][] a, int[][] b) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (a[i][j] != b[i][j]) return false;
        return true;
    }

    public boolean gauche() {
        int[][] avant = copier();
        for (int i = 0; i < 4; i++) {
            int[] ligne = new int[4];
            int pos = 0;
            for (int j = 0; j < 4; j++)
                if (matrice[i][j] != 0) ligne[pos++] = matrice[i][j];
            for (int j = 0; j < 3; j++) {
                if (ligne[j] != 0 && ligne[j] == ligne[j + 1]) {
                    ligne[j] *= 2;
                    score += ligne[j];
                    if (!objectifAtteint && score >= objectif)
                        objectifAtteint = true;
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
        if (!matricesEgales(avant, matrice)) { ajouterTuile(); return true; }
        return false;
    }

    public boolean droite() {
        int[][] avant = copier();
        for (int i = 0; i < 4; i++) {
            int[] ligne = new int[4];
            int pos = 3;
            for (int j = 3; j >= 0; j--)
                if (matrice[i][j] != 0) ligne[pos--] = matrice[i][j];
            for (int j = 3; j > 0; j--) {
                if (ligne[j] != 0 && ligne[j] == ligne[j - 1]) {
                    ligne[j] *= 2;
                    score += ligne[j];
                    if (!objectifAtteint && score >= objectif)
                        objectifAtteint = true;
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
        if (!matricesEgales(avant, matrice)) { ajouterTuile(); return true; }
        return false;
    }

    public boolean haut() {
        int[][] avant = copier();
        for (int j = 0; j < 4; j++) {
            int[] col = new int[4];
            int pos = 0;
            for (int i = 0; i < 4; i++)
                if (matrice[i][j] != 0) col[pos++] = matrice[i][j];
            for (int i = 0; i < 3; i++) {
                if (col[i] != 0 && col[i] == col[i + 1]) {
                    col[i] *= 2;
                    score += col[i];
                    if (!objectifAtteint && score >= objectif)
                        objectifAtteint = true;
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
        if (!matricesEgales(avant, matrice)) { ajouterTuile(); return true; }
        return false;
    }

    public boolean bas() {
        int[][] avant = copier();
        for (int j = 0; j < 4; j++) {
            int[] col = new int[4];
            int pos = 3;
            for (int i = 3; i >= 0; i--)
                if (matrice[i][j] != 0) col[pos--] = matrice[i][j];
            for (int i = 3; i > 0; i--) {
                if (col[i] != 0 && col[i] == col[i - 1]) {
                    col[i] *= 2;
                    score += col[i];
                    if (!objectifAtteint && score >= objectif)
                        objectifAtteint = true;
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
        if (!matricesEgales(avant, matrice)) { ajouterTuile(); return true; }
        return false;
    }
}