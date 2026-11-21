package ui;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;

class LoseScreenTest {
    @Test
    void preservesScreenUntilEnterRestarts() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        LoseScreen screen = new LoseScreen(player);

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_X, 'x')));
        assertInstanceOf(PlayScreen.class,
                screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_ENTER, '\n')));
    }
}
