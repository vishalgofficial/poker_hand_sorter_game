package com.argenti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.argenti.constant.Constants.TIE;
import static com.argenti.util.PokerUtil.*;
import static java.lang.System.*;

public class PokerSorter {

    public static void main(String[] args) {
        int playerOne = 0;
        int playerTwo = 0;
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String input = br.readLine();
                if (input == null) {
                    break;
                }
                if (validateInput(input)) {
                    break;
                }
                String[] cards = input.split(" ");
                Hand handOne = getHand(cards, 0, 5);
                Hand handTwo = getHand(cards, 5, 10);
                handOne.evaluate();
                handTwo.evaluate();
                int res = winner(handOne, handTwo);
                if (res == 1) {
                    playerOne++;
                } else if (res == 2) {
                    playerTwo++;
                } else {
                    out.println(TIE);
                }
            }
            printResult(playerOne, playerTwo);
            exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




