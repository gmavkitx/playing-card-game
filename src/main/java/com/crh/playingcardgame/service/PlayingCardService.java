package com.crh.playingcardgame.service;

/**
 * This interface to used to provide the service of the playing card.
 *
 * @author Ru Hai
 */
public interface PlayingCardService {

    /**
     * Play the game.
     * <p>
     * 1. Create sender and players.
     * 2. Sender send the cards to player.
     * 3. Players compute the points of received cards.
     * 4. Sender judge the result of every rounds.
     * 5. If the points of any player is bigger than 50, then s/he is winner. Game over.
     */
    void playGame();

}
