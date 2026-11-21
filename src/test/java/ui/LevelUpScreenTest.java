package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;

class LevelUpScreenTest {
    @Test
    void ignoresInvalidKeysAndClosesAfterRequestedNumberOfChoices() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        int maxHp = player.maxHp();
        LevelUpScreen screen = new LevelUpScreen(player, 2);

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_X, 'x')));
        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_1, '1')));
        assertEquals(maxHp + 10, player.maxHp());
        assertNull(screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_1, '1')));
        assertEquals(maxHp + 20, player.maxHp());
    }
}
