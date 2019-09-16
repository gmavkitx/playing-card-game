package com.crh.playingcardgame.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to define the player and receive the card from Sender
 * and sum the points of cards and feedback the points to sender.
 *
 * @author Ru Hai
 */
@Data
@Slf4j
public class Player {

    private int id;

    private String name;

    private Sender sender;

    private List<Card> cardList = new ArrayList<>();

    private int sum = 0;

    private int currentCardIndex;

    private Boolean canCompute = false;

    public Player(Sender sender) {
        this.sender = sender;
    }

    /**
     * Receive the card from sender.
     * @param card the card object.
     */
    public void addCard(Card card) {
        cardList.add(card);
        currentCardIndex = cardList.indexOf(card);

        //start to compute points.
        canCompute = true;
    }

    /**
     * Compute the total point of cards and inform the sender.
     */
    public void computePoint() {
        if (cardList.size() > currentCardIndex) {
            this.sum += cardList.get(currentCardIndex).getPoint();

            this.sender.collectPointResult();

            //stop to compute points.
            this.canCompute = false;
        }
    }

}
