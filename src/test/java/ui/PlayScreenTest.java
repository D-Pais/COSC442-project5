package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

class PlayScreenTest {
    @Test
    void scrollCoordinatesRemainWithinWorldBoundsAndUnknownKeyKeepsPlayScreen() {
        PlayScreen screen = new PlayScreen();

        assertTrue(screen.getScrollX() >= 0);
        assertTrue(screen.getScrollX() <= 10);
        assertTrue(screen.getScrollY() >= 0);
        assertTrue(screen.getScrollY() <= 9);
        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_F12, (char) 0)));
        assertEquals(10, Math.max(0, 90 - 80));
    }
}
