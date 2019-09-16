package com.crh.playingcardgame.service.impl;

import com.crh.playingcardgame.model.Card;
import com.crh.playingcardgame.model.Player;
import com.crh.playingcardgame.model.PlayingCard;
import com.crh.playingcardgame.model.Sender;
import com.crh.playingcardgame.service.PlayingCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to implement all of the interfaces of playing card service.
 *
 * @author Ru Hai.
 */
@Slf4j
@Service
public class PlayingCardServiceImpl implements PlayingCardService {

    @Value("${game.player.count:3}")
    private int countOfPlayers;

    private int rounds;

    private static final int cardNumber = 54;

    private Sender sender;

    /**
     * Compute the round by the count of players.
     */
    public void computeRoundByPlayers() {
        this.rounds = cardNumber / countOfPlayers;
    }

    @Override
    public void playGame() {
        log.info("##########>>>>>>>>>>>>>>>> Playing card game is started.");

        if(countOfPlayers < 2){
            log.info("##########>>>>>>>>>>>>>>>> Because the count {} of player(s) less than 2, therefore cancel the game.", countOfPlayers);
            return;
        }else{
            log.info("##########>>>>>>>>>>>>>>>> The {} Player(s) join in the game.", countOfPlayers);
        }

        //compute the round by the count of players.
        this.computeRoundByPlayers();
        log.info("##########>>>>>>>>>>>>>>>> There are {} rounds for playing the game.", this.rounds);

        //create sender and players.
        this.createSenderAndPlayers();

        log.info("##########>>>>>>>>>>>>>>>> Start to shuffle the playing card.");
        this.sender.shufflePlayingCard();

        //send the cards to every player in every rounds.
        this.sendCardsToPlayersInEveryRound();

        log.info("##########>>>>>>>>>>>>>>>> Game over.");
    }

    private void createSenderAndPlayers(){
        //use the thread of thread pool to indicate the operation of every player;
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(countOfPlayers, countOfPlayers, 5,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.AbortPolicy());

        //create Sender object.
        this.sender = new Sender(countOfPlayers);

        //create players object and new thread for collecting the points of all received cards.
        for (int i = 1; i <= countOfPlayers; i++) {
            Player player = new Player(sender);
            player.setId(i);
            player.setName("player" + i);
            sender.addPlayer(player);

            //create the thread for player to compute the points of all received cards from sender.
            threadPool.submit(() -> {
                //create the thread for player.
                while (!sender.getGameOverFlag()) {
                    if (!sender.isNextRound() && player.getCanCompute()) {
                        if(log.isDebugEnabled()) {
                            log.info("{} start to compute points", player.getName());
                        }
                        player.computePoint();
                    }
                }
            });
        }
    }

    private void sendCardsToPlayersInEveryRound(){
        List<Card> cardList = new ArrayList<>(countOfPlayers);

        //send cards to players one by one in every round until the points of any player bigger than 50.
        for (int round = 1; round <= this.rounds; round++) {
            cardList.clear();

            //compute the start position and end position of every round.
            int start = (round - 1) * countOfPlayers + 1;
            int end = round * countOfPlayers;

            //check if need to get into the next round.
            while (!sender.isNextRound()) {
                try {
                    //check if game over.
                    if(sender.getGameOverFlag()){
                        break;
                    }

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Error happened. ", e);
                }
            }

            //check if game over.
            if(sender.getGameOverFlag()){
                break;
            }

            //get the random cards for every players.
            for (int i = start; i <= end; i++) {
                cardList.add(sender.getRandomCard(i));
            }

            if(log.isDebugEnabled()) {
                log.info("{}", cardList);
            }

            //send card to players.
            sender.sendCard(round, cardList.toArray(new Card[countOfPlayers]));
        }
    }
}
