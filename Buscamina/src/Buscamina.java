import java.util.Random;
import java.util.Scanner;

public class Buscamina {
    private static final int FILAS = 10;
    private static final int COLUMNAS = 10;
    private static final int MINAS = 10;

    private char[][] tablero;
    private boolean[][] minas;
    private boolean[][] descubierto;

    public Buscamina() {
        tablero = new char[FILAS][COLUMNAS];
        minas = new boolean[FILAS][COLUMNAS];
        descubierto = new boolean[FILAS][COLUMNAS];
        inicializarTablero();
        colocarMinas();
    }

    private void inicializarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = '-';
                minas[i][j] = false;
                descubierto[i][j] = false;
            }
        }
    }

    private void colocarMinas() {
        Random rand = new Random();
        int minasColocadas = 0;

        while (minasColocadas < MINAS) {
            int fila = rand.nextInt(FILAS);
            int columna = rand.nextInt(COLUMNAS);

            if (!minas[fila][columna]) {
                minas[fila][columna] = true;
                minasColocadas++;
            }
        }
    }

    private int contarMinasAdyacentes(int fila, int columna) {
        int contador = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nuevaFila = fila + i;
                int nuevaColumna = columna + j;
                if (nuevaFila >= 0 && nuevaFila < FILAS && nuevaColumna >= 0 && nuevaColumna < COLUMNAS) {
                    if (minas[nuevaFila][nuevaColumna]) {
                        contador++;
                    }
                }
            }
        }
        return contador;
    }

    private void revelar(int fila, int columna) {
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS || descubierto[fila][columna]) {
            return;
        }

        descubierto[fila][columna] = true;
        if (minas[fila][columna]) {
            tablero[fila][columna] = '*'; // Mina encontrada
            return;
        }

        int minasAdyacentes = contarMinasAdyacentes(fila, columna);
        if (minasAdyacentes == 0) {
            tablero[fila][columna] = ' ';
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    revelar(fila + i, columna + j);
                }
            }
        } else {
            tablero[fila][columna] = (char) ('0' + minasAdyacentes);
        }
    }

    public void mostrarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (descubierto[i][j]) {
                    System.out.print(tablero[i][j] + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    public void jugar() {
        Scanner leer = new Scanner(System.in);
        while (true) {
            mostrarTablero();
            System.out.print("Ingrese fila y columna (separados por espacio): ");
            int fila = leer.nextInt();
            int columna = leer.nextInt();

            if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) {
                System.out.println("Coordenadas inválidas. Inténtelo de nuevo.");
                continue;
            }

            if (descubierto[fila][columna]) {
                System.out.println("Esta celda ya está descubierta. Inténtelo de nuevo.");
                continue;
            }

            revelar(fila, columna);

            if (minas[fila][columna]) {
                System.out.println("¡Perdiste! Has tocado una mina.");
                mostrarTablero();
                break;
            }
        }
        leer.close();
    }

    public static void main(String[] args) {
        Buscamina juego = new Buscamina();
        juego.jugar();
    }
}
