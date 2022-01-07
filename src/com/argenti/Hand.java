package com.argenti;


import com.argenti.enums.HandCategory;

import java.util.stream.IntStream;

import static com.argenti.enums.HandCategory.*;
import static java.util.Arrays.sort;
import static java.util.Arrays.stream;

public class Hand {
    public Card[] cards;

    public HandCategory category;

    public Integer handValue;

    public Hand(String[] strArr) {
        cards = IntStream.range(0, 5)
                .mapToObj(i -> new Card(strArr[i]))
                .toArray(Card[]::new);
        sort(cards);
    }

    public Card getCard(int index) {
        if (index >= 5) {
            return null;
        }
        return cards[index];
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        stream(cards).forEach(card -> str.append(card.toString()).append(" "));
        if (str.length() > 0) {
            str.append("(");
            str.append(getHandCategory().getDesc());
            str.append(")");
        }
        return str.toString();
    }

    public void evaluate() {
        if (checkSameSuitAndStraight()) return;
        if (checkFour()) return;
        if (checkFullHouse()) return;
        if (checkSameSuit()) return;
        if (checkStraight()) return;
        if (checkThree()) return;
        if (checkTwoPairs()) return;
        if (checkOnePair()) return;
        handValue = getCard(4).getValue();
        category = HIGH_CARD;
    }

    private boolean checkOnePair() {
        if (pair() != -1) {
            category = ONE_PAIR;
            return true;
        }
        return false;
    }

    private boolean checkTwoPairs() {
        if (twoPairs() != -1) {
            category = TWO_PAIRS;
            return true;
        }
        return false;
    }

    private boolean checkThree() {
        if (three() != -1) {
            category = THREE_OF_A_KIND;
            return true;
        }
        return false;
    }

    private boolean checkStraight() {
        if (straight() != -1) {
            category = STRAIGHT;
            return true;
        }
        return false;
    }

    private boolean checkSameSuit() {
        if (allSameSuit() != -1) {
            category = FLUSH;
            return true;
        }
        return false;
    }

    private boolean checkFullHouse() {
        if (fullHouse() != -1) {
            category = FULL_HOUSE;
            return true;
        }
        return false;
    }

    private boolean checkFour() {
        if (four() != -1) {
            category = FOUR_OF_A_KIND;
            return true;
        }
        return false;
    }

    private boolean checkSameSuitAndStraight() {
        if (allSameSuit() != -1 && straight() != -1) {
            if (getCard(0).getValue() == 10) {
                category = ROYAL_FLUSH;
                handValue = 9999;
            } else {
                category = STRAIGHT_FLUSH;
            }
            return true;
        }
        return false;
    }

    private int pair() {
        int prev = cards[4].getValue();
        int total = 0, nOfCards = 1;
        for (int i = 3; i >= 0; i--) {
            if (cards[i].getValue() == prev) {
                total += cards[i].getValue();
                nOfCards++;
            }
            if (nOfCards == 2) {
                break;
            }
            prev = cards[i].getValue();
        }
        if (nOfCards == 2) {
            handValue = total;
            return total;
        }
        return -1;
    }

    private int twoPairs() {
        int prev = cards[4].getValue();
        int i = 3, total = 0, nOfCards = 1;
        for (; i >= 0; i--) {
            if (cards[i].getValue() == prev) {
                total += cards[i].getValue();
                nOfCards++;
            }
            if (nOfCards == 2) {
                break;
            }
            prev = cards[i].getValue();
        }

        if (nOfCards == 2 && i > 0) {
            nOfCards = 1;
            prev = cards[i - 1].getValue();
            for (i = i - 2; i >= 0; i--) {
                if (cards[i].getValue() == prev) {
                    total += cards[i].getValue();
                    nOfCards++;
                }
                if (nOfCards == 2) {
                    break;
                } else {
                    total = 0;
                    nOfCards = 1;
                }
                prev = cards[i].getValue();
            }
        } else {
            return -1;
        }
        if (nOfCards == 2) {
            handValue = total;
            return total;
        }
        return -1;
    }

    private int three() {
        int prev = cards[4].getValue();
        int total = 0, nOfCards = 1;
        for (int i = 3; i >= 0; i--) {
            if (cards[i].getValue() == prev) {
                total += cards[i].getValue();
                nOfCards++;
            } else {
                total = 0;
                nOfCards = 1;
            }
            prev = cards[i].getValue();
        }
        if (nOfCards == 3) {
            handValue = total;
            return total;
        }
        return -1;
    }

    private int fullHouse() {
        boolean changed = false;
        int previous = cards[4].getValue();
        int total = 0, nOfCards = 1;
        for (int i = 3; i >= 0; i--) {
            if (cards[i].getValue() == previous) {
                total += cards[i].getValue();
                nOfCards++;
            } else if (!changed) {
                changed = true;
                if (nOfCards < 2) {
                    handValue = -1;
                    return -1;
                }
                if (nOfCards == 3)
                    handValue = total;
            } else {
                handValue = -1;
                return -1;
            }
            previous = cards[i].getValue();
        }
        handValue = total;
        return total;

    }

    private int four() {
        int previous = cards[4].getValue();
        int total = 0, nOfCards = 1;
        for (int i = 3; i >= 0 && nOfCards < 4; i--) {
            if (cards[i].getValue() == previous) {
                total += cards[i].getValue();
                nOfCards++;
            } else {
                total = 0;
                nOfCards = 1;
            }
            previous = cards[i].getValue();
        }
        if (nOfCards == 4) {
            handValue = total;
            return total;
        }
        return -1;
    }

    private int allSameSuit() {
        char previous = cards[0].getSuit();
        int total = cards[0].getValue();
        for (int i = 1; i < 5; i++) {
            if (cards[i].getSuit() != previous) {
                return -1;
            }
            total += cards[i].getValue();
            previous = cards[i].getSuit();
        }
        handValue = total;
        return total;
    }

    private int straight() {
        int prev = cards[0].getValue();
        int total = prev;
        for (int i = 1; i < 5; i++) {
            if (cards[i].getValue() != prev + 1) {
                return -1;
            }
            prev = cards[i].getValue();
            total += 1;
        }
        handValue = total;
        return total;
    }

    public HandCategory getHandCategory() {
        return category;
    }

    public Integer getHandValue() {
        return handValue;
    }
}