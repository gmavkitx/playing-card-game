package com.crh.playingcardgame.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to define the sender and manage the players and send cards to players and judge the result of game.
 *
 * @author Ru Hai.
 */
@Data
@Slf4j
public class Sender {

    private String name;

    private static volatile Boolean gameOverFlag = false;

    private Map<Integer, Player> players = new HashMap<>();

    private int feedbackCount = 0;

    private boolean nextRound = true;

    private int countOfPlayers;

    private StringBuilder msg = new StringBuilder();

    private PlayingCard playingCard;

    /**
     * Constructor
     *
     * @param countOfPlayers the count of players.
     */
    public Sender(int countOfPlayers) {
        this.countOfPlayers = countOfPlayers;
        this.playingCard = new PlayingCard();
    }

    /**
     * Sender shuffle the playing card when playing new game.
     */
    public void shufflePlayingCard() {
        this.playingCard.shufflePlayingCard();
    }

    /**
     * Collect the info of the players for sending the card to them.
     *
     * @param player
     */
    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    /**
     * Synchronous collect the point result.
     */
    synchronized public void collectPointResult() {
        if (log.isDebugEnabled()) {
            log.info("before: feedbackCount = {}", feedbackCount);
        }

        addFeedbackCount();

        if (log.isDebugEnabled()) {
            log.info("after: feedbackCount = {}", feedbackCount);
        }

        if (feedbackCount == this.countOfPlayers) {
            if (log.isDebugEnabled()) {
                log.info("start to judge....");
            }
            judgeGameOver();
        }
    }

    /**
     * Judge if game over.
     */
    private void judgeGameOver() {
        StringBuilder winnerInfo = new StringBuilder();

        for (int i = 1; i <= countOfPlayers; i++) {
            msg.append(players.get(i).getName()).append("=").append(players.get(i).getSum()).append(",");
            if (players.get(i).getSum() > 50) {
                winnerInfo.append(players.get(i).getName()).append(", ");
            }
        }

        msg.deleteCharAt(msg.lastIndexOf(","));

        if (winnerInfo.length() > 0) {
            winnerInfo.deleteCharAt(winnerInfo.lastIndexOf(",")).append(" win.");
            msg.append(" -> ").append(winnerInfo);

            gameOver();
        } else {
            msg.append(";").toString();

            announceResult();

            this.nextRound = true;
            this.feedbackCount = 0;
        }
    }

    /**
     * Increase the count of the feedback for deciding to judge the result of game in current round.
     */
    public void addFeedbackCount() {
        this.feedbackCount++;
    }

    /**
     * Get the random card by index number.
     *
     * @param i the index of random card.
     * @return the card object.
     */
    public Card getRandomCard(int i) {
        return this.playingCard.getOutOfOrderCardMap().get(i);
    }

    /**
     * Send the cards to every player in the specified round.
     *
     * @param round the round number.
     * @param card  the cards array.
     */
    public void sendCard(int round, Card... card) {
        if (card.length == this.countOfPlayers) {
            msg.append("Round ").append(round).append(" = Sender [");
            for (int i = 0; i < card.length; i++) {
                Player player = players.get(i + 1);
                msg.append("\"").append(card[i].getSuit()).append("-").append(card[i].getName()).append("\",");
                player.addCard(card[i]);
            }

            msg.deleteCharAt(msg.lastIndexOf(",")).append("]").append(" -> ");

        } else {
            log.error("The number of cards is incorrect.");
        }

        this.nextRound = false;
    }

    /**
     * Announce the result of current round.
     */
    private void announceResult() {
        log.info(msg.toString());

        //clear msg for the next round.
        msg.delete(0, msg.length());
    }

    /**
     * Game over and print the winner(s).
     */
    private void gameOver() {
        announceResult();
        gameOverFlag = true;
    }

    /**
     * @return the flag of game over.
     */
    public boolean getGameOverFlag() {
        return gameOverFlag;
    }

}
