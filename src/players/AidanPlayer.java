package players;

import game.HandRanks;
import game.Player;
import java.util.Random;

public class AidanPlayer extends Player {
    double random = 0.9;
    double maxRaiseMultiplier = 20.0;
    Random factor = new Random();

    boolean hasDecentHand;

    public AidanPlayer(String name) {
        super(name);
    }


    @Override
    protected void takePlayerTurn() {
        if (shouldFold()) {
            fold();
        } else if (shouldCheck()) {
            check();
        } else if (shouldCall()) {
            call();
        } else if (shouldRaise()) {
            if (getGameState().getNumRoundStage() == 0) {
                double randomFactor = 20.0 + factor.nextDouble() * (maxRaiseMultiplier - 12.0); // Random factor between 20.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 1) {
                double randomFactor = 20.0 + factor.nextDouble() * (maxRaiseMultiplier - 10.0); // Random factor between 20.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 2) {
                double randomFactor = 20.0 + factor.nextDouble() * (maxRaiseMultiplier - 6.0); // Random factor between 20.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 3) {
                double randomFactor = 10.0 + factor.nextDouble() * (maxRaiseMultiplier - 4.0); // Random factor between 10.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
        } else if (shouldAllIn()) {
            allIn();
        }
    }

    @Override
    protected boolean shouldFold() {
        //if I have a pair midway through the game I fold and if the pot is 50% of my bank without a good hand
        if ((hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.PAIR.getValue() && (getGameState().getNumRoundStage() == 2)) || (hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.TWO_PAIR.getValue() && getGameState().getTableBet() >= getBank() * 0.5)) {
            return true;
        }
        else if (hasDecentHand == evaluatePlayerHand().getValue() >= HandRanks.THREE_OF_A_KIND.getValue()) {
            return false;
        }
        return false;
    }

    @Override
    protected boolean shouldCheck() {
        // if there is no bet and I have less than a pair I check
        if (!getGameState().isActiveBet() || hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.HIGH_CARD.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldCall() {
        // checks if there is an active bet and that it is less than 20% of my current bank
        if (getGameState().isActiveBet() && getGameState().getTableBet() < getBank() * 0.2) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldRaise() {
        if(hasDecentHand == evaluatePlayerHand().getValue() >= HandRanks.TWO_PAIR.getValue()) {
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    protected boolean shouldAllIn() {
        //if I have a full house at the end of the game, I go all in
        if(evaluatePlayerHand().getValue() >= HandRanks.FULL_HOUSE.getValue() && getGameState().getNumRoundStage() == 3) {
            return true;
        }
        return false;
    }
}