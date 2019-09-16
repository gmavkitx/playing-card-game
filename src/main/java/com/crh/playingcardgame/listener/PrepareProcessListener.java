package com.crh.playingcardgame.listener;

import com.crh.playingcardgame.service.PlayingCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * This listener is used to start the game of playing card.
 *
 * @author Ru Hai
 */
@Slf4j
@Component
public class PrepareProcessListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Autowired
    private PlayingCardService playingCardService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        playingCardService.playGame();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
