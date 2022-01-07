package com.argenti.util;

import com.argenti.Hand;

import static com.argenti.constant.Constants.*;
import static java.lang.System.out;
import static java.util.Arrays.copyOfRange;

public class PokerUtil {

    private PokerUtil() {
    }

    public static boolean validateInput(String input) {
        if (!input.matches(REGEX)) {
            System.out.println(INVALID_INPUT_FORMAT);
            return true;
        }
        return false;
    }

    public static String[] getHandArr(String[] cards, int from, int to) {
        String[] handArray = copyOfRange(cards, from, to);
        if (handArray.length != 5) {
            System.out.println(INVALID_HAND_FORMAT_UNABLE_TO_PARSE_INPUT);
        }
        return handArray;
    }

    public static int winner(Hand handOne, Hand handTwo) {
        int handOneHanCategory = handOne.getHandCategory().getValue();
        int handTwoHanCategory = handTwo.getHandCategory().getValue();
        if (handOneHanCategory > handTwoHanCategory) {
            return 1;
        } else if (handOneHanCategory < handTwoHanCategory) {
            return 2;
        } else if (handOne.getHandValue() > handTwo.getHandValue()) {
            return 1;
        } else if (handOne.getHandValue() < handTwo.getHandValue()) {
            return 2;
        } else {
            // final tie break!
            for (int i = 4; i >= 0; i--) {
                if (handOne.getCard(i).getValue() > handTwo.getCard(i).getValue()) {
                    return 1;
                } else if (handOne.getCard(i).getValue() < handTwo.getCard(i).getValue()) {
                    return 2;
                }
            }
            // theres a tie here...
            return -1;
        }
    }

    public static Hand getHand(String[] cards, int from, int to) {
        return new Hand(getHandArr(cards, from, to));
    }

    public static void printResult(int playerOne, int playerTwo) {
        out.println(PLAYER_1 + playerOne + HANDS);
        out.println(PLAYER_2 + playerTwo + HANDS);
    }
}
