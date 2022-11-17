package Game;

import java.io.IOException;

public class GameEngine implements Runnable{

    // client 1
    Client client1;
    // client 2
    Client client2;
    // Type of game
    Tictactoe game;

    public GameEngine(Client client1, Client client2) throws IOException{
        // client 1 initializing...
        this.client1 = client1;
        // client 2 initializing...
        this.client2 = client2;

        // assign game program
        this.game = new Tictactoe();
    }

    @Override
    public void run() {

        // initial state
        client1.write("initial state\n" + game.arrayToString());
        client2.write("initial state\n" + game.arrayToString());

        boolean exit = false;
        boolean chance = true;
        String c1Mark = "x";
        String c2Mark = "o";

        while(!exit) {

            if(chance) 
            { // client 1 turn
                exit = play(client1, client2, c1Mark);
                chance = false;   
            } 
            else 
            { // client 2 turn
                exit = play(client2 ,client1, c2Mark);
                chance = true;
            }
        }
        client1.close();
        client2.close();
    }

    private boolean play(Client c1, Client c2, String mark) {
        String msg;
        try {
            boolean marked = false;
            while (!marked) {
                try {
                    c1.write("Please enter from 1 - 9 ...");
                    msg = c1.read(); // read client move
                    game.cardinal(msg, mark); // input client move to game program
                    marked = true; // if the given move is marked successfully then exit
                } catch(PositionAlreadyMarkedException ge) {
                    c1.write("Position already occupied\nSelect another");
                }
            }

            // send current state to the players
            c1.write(game.arrayToString());
            c2.write(game.arrayToString());

            c1.write("Waiting for opponents move...");
            c2.write("Your move!");
        } catch (PlayerWonException pwe) {
            c1.write("\nCongratulations you have won! With winning position: " + pwe);
            c2.write("\nSorry you have lost...");
            c1.write(game.arrayToString());
            c2.write(game.arrayToString());
            return true;
        } catch (GameDrawException gde) {
            c1.write("\nGame Draw!");
            c2.write("\nGame Draw!");
            c1.write(game.arrayToString());
            c2.write(game.arrayToString());
            return true;
        }
        return false;
        
    }

}
