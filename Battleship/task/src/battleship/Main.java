package battleship;

import java.util.Scanner;

public class Main {

    enum Ship {

        AIRCRAFT("Aircraft Carrier", 5),
        BATTLESHIP("Battleship", 4),
        SUBMARINE("Submarine", 3),
        CRUISE("Cruiser", 3),
        DESTROYER("Destroyer", 2);

        private final String shipName;
        private final int length;

        Ship(String shipName, int length) {
            this.shipName = shipName;
            this.length = length;
        }
    }

    public static void main(String[] args) {

        char[][] boardGame_p1 = new char[10][10];
        char[][] boardGame_p2 = new char[10][10];
        int[] stateOfShip_p1 = {5, 4, 3, 3, 2};
        int[] stateOfShip_p2 = {5, 4, 3, 3, 2};


        initializeGame(boardGame_p1);
        initializeGame(boardGame_p2);


        System.out.println("Player 1, place your ships on the game field");
        printGame(boardGame_p1, true);

        // posicionamento das embarcações do primeiro jogador
        for (Ship ship : Ship.values()) {
            positionShip(boardGame_p1, ship, false);
        }

        waitPlayer();

        System.out.println("Player 2, place your ships on the game field");
        printGame(boardGame_p2, true);

        // posicionamento das embarcações do segundo jogador
        for (Ship ship : Ship.values()) {
            positionShip(boardGame_p2, ship, false);
        }


        boolean endGame = false;
        boolean player_1 = true;

        while (!endGame) { // verifica se todas as embarcações de um dos jogadores foi destruída

            waitPlayer();
            endGame = takeShot(player_1 ? boardGame_p2 : boardGame_p1,
                    player_1 ? boardGame_p1 : boardGame_p2,
                    player_1 ? stateOfShip_p1 : stateOfShip_p2, player_1 ? 1 : 2); // efetua um disparo
            player_1 = !player_1;
        }

    }

    /**
     * Para o jogo para poder haver a trocar de jogador, exibindo uma mensagem informativa.
     */
    private static void waitPlayer() {

        System.out.println("\nPress Enter and pass the move to another player");
        new Scanner(System.in).nextLine();
    }

    /**
     * Responsável pela lógica a cada jogada (tiro). Fazendo as alterações nos arrays bidimensional de acordo ao que foi
     * atingido (embarcação ou água).
     *
     * @param enemyBoardGame array 2D que representa o campo inimigo.
     * @param playerBoardGame array 2D que representa o campo do jogador atual.
     * @param stateOfShips arrays que representa as condições das embarcações (0 a embarcação fui destruída).
     * @param player jogador atual
     * @return retorna true caso um dos jogadores atinja todas as embarcações inimigas.
     */
    private static boolean takeShot(char[][] enemyBoardGame, char[][] playerBoardGame, int[] stateOfShips, int player) {

        int currentShip = -1;
        printGame(enemyBoardGame, true);
        System.out.println("---------------------");
        printGame(playerBoardGame, false);
        System.out.printf("\nPlayer %d, it's your turn:\n", player);

        String coordinate = new Scanner(System.in).nextLine();

        // transforma as coordenas em números válidos do array
        int x = coordinate.charAt(0) - 'A';
        int y = Integer.parseInt(coordinate.substring(1)) - 1;

        if (x > 9 || y > 9) { // caso as coordenadas estejam fora dos limites do campo
            System.out.println("Error! You entered the wrong coordinates! Try again:");
        } else {

            // verifica se o tiro atingio uma embarcação
            if (Character.isDigit(enemyBoardGame[x][y])) {

                // verifica qual embarcação atingida e altera a sua condição subtraindo 1
                switch (enemyBoardGame[x][y]) {

                    case '0' -> {
                        stateOfShips[0] -= 1;
                        currentShip = 0;
                    }
                    case '1' -> {
                        stateOfShips[1] -= 1;
                        currentShip = 1;
                    }
                    case '2' -> {
                        stateOfShips[2] -= 1;
                        currentShip = 2;
                    }
                    case '3' -> {
                        stateOfShips[3] -= 1;
                        currentShip = 3;
                    }
                    case '4' -> {
                        stateOfShips[4] -= 1;
                        currentShip = 4;
                    }

                }

                enemyBoardGame[x][y] = 'X'; // marca o local atingindo
                printGame(enemyBoardGame, false);

                int countState = 0;
                // verifica os estados das embarcações
                for (int state : stateOfShips) {
                    countState += state;
                }


                if (countState == 0) { // todas as embarcações foram destruídas
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    return true;
                } else {

                    boolean sankShip = false;
                    // analisa se uma embarcação foi totalmente atingida
                    for (int i = 0; i < stateOfShips.length; i++) {
                        if (stateOfShips[i] == 0 && currentShip == i) {
                            sankShip = true;
                            break;
                        }
                    }
                    System.out.println(sankShip ? "You sank a ship!" : "You hit a ship!\n");
                }

            } else if (enemyBoardGame[x][y] == 'X') { // caso acerte um local já atingido anteriormente

                System.out.println("You hit a ship again!\n");
            } else { // caso não acerte nenhuma embarcação

                enemyBoardGame[x][y] = 'M';
                System.out.println("You missed!");
            }
        }

        return false;
    }

    /**
     * Inicia o campo de batalha, preenchendo um arrays com "~".
     *
     * @param boardGame arrays a ser preenchido.
     */
    private static void initializeGame(char[][] boardGame) {

        for (int i = 0; i < boardGame.length; i++) {
            for (int j = 0; j < boardGame.length; j++) {
                boardGame[i][j] = '~';
            }
        }
    }


    /**
     * Imprime o campo de batalha do jogo.
     * @param boardGame array que representa o campo de batalha.
     * @param fogOfWar boolean caso deseje utilizar a névoa de guerra para enconbrir as embacações ao imprimir.
     */
    private static void printGame(char[][] boardGame, boolean fogOfWar) {

        System.out.print("  ");
        for (int i = 0; i < boardGame.length + 1; i++) {
            for (int j = 0; j < boardGame.length + 1; j++) {

                if (i == 0 && j != 0) { // imprimi a primeira linha, com os números das coordenadas das colunas
                    System.out.printf("%d ", j);
                } else if (j == 0 && i != 0) { // imprimi a primeira coluna, com letras das coordenadas das linhas
                    System.out.printf("%c ", (char) i + 64);
                } else if (i > 0) {

                    // cobre com a névoa de guerra as embarcações
                    if (fogOfWar && (boardGame[i - 1][j - 1] != 'M' && boardGame[i - 1][j - 1] != 'X')) {

                        System.out.print("~ ");
                    } else {

                        if (Character.isDigit(boardGame[i - 1][j - 1])) { // imprime as partes das embarcações
                            System.out.print("O ");
                        } else {
                            System.out.printf("%s ", boardGame[i - 1][j - 1]); // imprime partes atingidas por tiros
                        }
                    }
                }
            }

            System.out.println();
        }
    }

    /**
     * Posiciona as embarcações no campo.
     * @param boardGame array que representa o campo.
     * @param ship embarcações a ser posicionada.
     * @param errorCall boolean caso a função esteja a ser chamada devido a uma entrada com erro.
     */
    private static void positionShip(char[][] boardGame, Ship ship, boolean errorCall) {

        Scanner scanner = new Scanner(System.in);
        if (!errorCall) { // verifica se não é chamada devido a uma entrada inválida
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.shipName, ship.length);
        }

        String[] coordinate = scanner.nextLine().split(" "); // obtem as coordenadas

        // obtém número válido para o array
        int n1 = Integer.parseInt(coordinate[0].substring(1)) - 1;
        int n2 = Integer.parseInt(coordinate[1].substring(1)) - 1;
        // obtém número válido para o arrays, a partir das letras das coordenadas
        int l1 = coordinate[0].charAt(0) - 'A';
        int l2 = coordinate[1].charAt(0) - 'A';

        // verifica se as coordenadas são descrecente e as inverte
        if (n1 > n2) {
            int aux = n1;
            n1 = n2;
            n2 = aux;
        }
        // verifica se as coordenadas são descrecente e as inverte
        if (l1 > l2) {
            int aux = l1;
            l1 = l2;
            l2 = aux;
        }


        if (l1 != l2 && n1 != n2) { // verifica se o posicionamento da embarcação não é uma reta

            System.out.println("Error! Wrong ship location! Try again:");
            positionShip(boardGame, ship, true);

          // verifica se o tamanho dado pelas coordenadas é o mesmo da embarcação
        } else if (l1 == l2 && n2 - n1 != ship.length - 1 ||
                n1 == n2 && l2 - l1 != ship.length - 1 ) {

            System.out.printf("Error! Wrong length of the %s! Try again:", ship.shipName);
            positionShip(boardGame, ship, true);
        } else if (isBusy(boardGame, l1, n1, l2, n2)) { // verifica se não há outra embarcação nas coordenadas dadas

            System.out.println("Error! You placed it too close to another one. Try again:");
            positionShip(boardGame, ship, true);
        } else {

            if (n1 != n2 ) { // posiciona na hozizontal

                for (int i = n1; i <= n2; i++) {
                    boardGame[l1][i] = (char) (ship.ordinal() + '0');
                }
                printGame(boardGame, false);
            } else { // posiciona na vertical

                for (int i = l1; i <= l2; i++) {
                    boardGame[i][n1] =  (char) (ship.ordinal() + '0');
                }
                printGame(boardGame, false);
            }
        }
    }


    /**
     * Verifica lugares ocupados por embarcações.
     * @param boardGame arrays que representa o campo de batalha.
     * @param l1 letra da primeira coordenada.
     * @param n1 número da primeira coordenada.
     * @param l2 letra da segunda coordenada.
     * @param n2 número da segunda coordenada.
     * @return true caso o local esteja ocupado.
     */
    private static boolean isBusy(char[][] boardGame, int l1, int n1, int l2, int n2) {
        int rows = boardGame.length;
        int columns = boardGame[0].length;

        for (int i = n1 - 1; i <= n2 + 1; i++) {
            for (int j = l1 - 1; j <= l2 + 1; j++) {
                if (i >= 0 && i < columns && j >= 0 && j < rows && Character.isDigit(boardGame[j][i])) {
                    return true;
                }
            }
        }

        return false;
    }
}