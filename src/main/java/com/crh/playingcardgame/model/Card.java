package com.crh.playingcardgame.model;

import lombok.Data;

/**
 * This class is used to indicate the single card of playing card.
 *
 * @author Ru hai
 */
@Data
public class Card {

    /**
     * The id of card.
     */
    private int id;

    /**
     * The name of card.
     */
    private String name;

    /**
     * The suit of card.
     */
    private String suit;

    /**
     * The point of card.
     */
    private int point;

}
