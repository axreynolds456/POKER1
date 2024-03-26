package players;

import game.HandRanks;
import game.Player;

import java.util.Random;

public class AidanPlayer extends Player {

    boolean hasDecentHand;


    String name;
    public AidanPlayer(String name) {
        super(name);
    }



    @Override
    protected void takePlayerTurn() {
        if (shouldFold()) {
            fold();
        } else if (shouldAllIn())  {
            allIn();
        } else if(shouldRaise()) {
            if(getGameState().getNumRoundStage() == 0) {
                //if I have a pair at the beginning of the game, I raise 15
                if(evaluatePlayerHand().getValue() == HandRanks.PAIR.getValue()) {
                    raise(15);
                }
            }
            else if(getGameState().getNumRoundStage() == 1) {
                //if I have a pair I raise 15
                if(evaluatePlayerHand().getValue() == HandRanks.PAIR.getValue()) {
                    raise(15);
                }
                //if I have two-pair I raise 20
                else if (evaluatePlayerHand().getValue() == HandRanks.TWO_PAIR.getValue()) {
                    raise(20);
                }
                //if I have three of a kind I raise 35
                else if (evaluatePlayerHand().getValue() == HandRanks.THREE_OF_A_KIND.getValue()) {
                    raise(35);
                }
                //if I have anything better than a flush I raise 40
                else if (evaluatePlayerHand().getValue() >= HandRanks.FLUSH.getValue()) {
                    raise(40);
                }
            }
            //as the game goes on I slowly increase how much I am betting depending on what I have
            else if(getGameState().getNumRoundStage() == 2) {
                if (evaluatePlayerHand().getValue() == HandRanks.TWO_PAIR.getValue()) {
                    raise(20);
                }
                else if (evaluatePlayerHand().getValue() == HandRanks.THREE_OF_A_KIND.getValue()) {
                    raise(35);
                }
                else if (evaluatePlayerHand().getValue() == HandRanks.FLUSH.getValue() || evaluatePlayerHand().getValue() == HandRanks.STRAIGHT.getValue()) {
                    raise(45);
                }
                else if(evaluatePlayerHand().getValue() >= HandRanks.FULL_HOUSE.getValue()) {
                    raise(55);
                }
            } else if(getGameState().getNumRoundStage() == 3) {
                if (evaluatePlayerHand().getValue() == HandRanks.THREE_OF_A_KIND.getValue()) {
                    raise(30);
                }
                else if (evaluatePlayerHand().getValue() == HandRanks.FLUSH.getValue() || evaluatePlayerHand().getValue() == HandRanks.STRAIGHT.getValue()) {
                    raise(50);
                }
            }
        } else if (shouldCheck()) {
            check();
        } else if (shouldCall()) {
            call();
        }
    }

    @Override
    protected boolean shouldFold() {
        //if I have a pair midway through the game I fold and if the pot is 50% of my bank without a good hand
        if((hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.PAIR.getValue() && (getGameState().getNumRoundStage() == 2)) || (hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.TWO_PAIR.getValue() && getGameState().getTableBet() >= getBank() * 0.5)) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldCheck() {
        // if there is no bet and I have less than a pair I check
        if(!getGameState().isActiveBet() || hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.PAIR.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldCall() {
        // checks if there is an active bet and that it is less than 20% of my current bank
        if(getGameState().isActiveBet() && getGameState().getTableBet() < getBank() * 0.2) {
            // if I have a full house midway through the game or if I have a 2 pair at the beginning of the game
            if((hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.FULL_HOUSE.getValue() && getGameState().getNumRoundStage() == 2) || (hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.TWO_PAIR.getValue() && getGameState().getNumRoundStage() == 1)) {
                shouldRaise();
            }
            // if I have less than a 2 pair at any stage, I check
            else if(hasDecentHand == evaluatePlayerHand().getValue() >= HandRanks.TWO_PAIR.getValue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean shouldRaise() {
        return true;
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