package com.crh.playingcardgame.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used to initial all cards of the playing card.
 *
 * @author Ru Hai
 */
@Data
@Slf4j
public class PlayingCard {

    private Map<Integer, Card> cardMap = new HashMap<>();

    /**
     * Store the generated random card list.
     */
    private Map<Integer, Card> outOfOrderCardMap = new ConcurrentHashMap<>();

    /**
     * 红桃
     */
    private static final String HEART = "Heart";

    /**
     * 黑桃
     */
    private static final String SPADE = "Spade";

    /**
     * 方块
     */
    private static final String DIAMOND = "Diamond";

    /**
     * 梅花
     */
    private static final String CLUB = "Club";

    private static final String RED = "Red";

    private static final String BLACK = "Black";

    private static final String JOKER = "Joker";

    public PlayingCard() {
        initialPlayingCard();
    }

    /**
     * Initial all the cards of playing card.
     */
    private void initialPlayingCard() {
        int index = 0;
        for (int i = 1; i <= 15; i++) {
            if (i < 14) {
                StringBuilder nameSb = new StringBuilder();
                switch (i) {
                    case 1:
                        nameSb.append("A");
                        break;
                    case 11:
                        nameSb.append("J");
                        break;
                    case 12:
                        nameSb.append("Q");
                        break;
                    case 13:
                        nameSb.append("K");
                        break;
                    default:
                        nameSb.append(i);
                }

                for (int j = 1; j <= 4; j++) {
                    Card card = new Card();
                    card.setId(++index);
                    card.setName(nameSb.toString());
                    card.setPoint(i);

                    switch (j) {
                        case 1:
                            card.setSuit(HEART);
                            break;
                        case 2:
                            card.setSuit(SPADE);
                            break;
                        case 3:
                            card.setSuit(DIAMOND);
                            break;
                        case 4:
                            card.setSuit(CLUB);
                            break;
                        default:
                    }
                    cardMap.put(card.getId(), card);
                }
            } else {
                Card card = new Card();
                card.setId(++index);
                card.setName(JOKER);
                card.setPoint(20);

                switch (i) {
                    case 14:
                        card.setSuit(BLACK);
                        break;
                    case 15:
                        card.setSuit(RED);
                        break;
                    default:
                }

                cardMap.put(card.getId(), card);
            }
        }
    }

    public void shufflePlayingCard() {
        int num;
        int count = 0;
        List<Integer> numList = new ArrayList<>(54);

        while (count < 54) {
            num = (int) (Math.random() * 54 + 1);
            if (!numList.contains(num)) {
                this.outOfOrderCardMap.put(++count, cardMap.get(num));

                numList.add(num);

                if(log.isDebugEnabled()) {
                    log.info("{} = {}", count, num);
                }
            }
        }
        if(log.isDebugEnabled()) {
            log.info("successful.");
        }
    }

//    public static void main(String[] args) {
//        PlayingCard playingCard = new PlayingCard();
//        System.out.println(playingCard.getCardMap().entrySet());
//
//        playingCard.shufflePlayingCard();
//
//        System.out.println(playingCard.getOutOfOrderCardMap().entrySet());
//    }

}
