package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;

class TargetBasedScreenTest {
    @Test
    void navigationReportsWorldCoordinatesAndEscapeCancels() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(7, 7), 3, 3, '@', "player");
        ProbeTargetScreen screen = new ProbeTargetScreen(player);

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_UP, ' ')));
        assertEquals(3, screen.enteredX);
        assertEquals(2, screen.enteredY);
        assertNull(screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_ESCAPE, (char) 27)));
    }

    @Test
    void rejectsMovementAndSelectsCurrentCoordinateOnEnter() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(7, 7), 3, 3, '@', "player");
        ProbeTargetScreen screen = new ProbeTargetScreen(player);

        screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_LEFT, ' '));
        assertEquals(3, screen.enteredX);
        assertEquals(3, screen.enteredY);
        assertNull(screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_ENTER, '\n')));
        assertEquals(3, screen.selectedX);
        assertEquals(3, screen.selectedY);
    }

    private static final class ProbeTargetScreen extends TargetBasedScreen {
        private int enteredX;
        private int enteredY;
        private int selectedX;
        private int selectedY;

        ProbeTargetScreen(Creature player) {
            super(player, "Target", 10, 10);
        }

        @Override
        public boolean isAcceptable(int x, int y) {
            return x >= player.x;
        }

        @Override
        public void enterWorldCoordinate(int x, int y, int screenX, int screenY) {
            enteredX = x;
            enteredY = y;
        }

        @Override
        public void selectWorldCoordinate(int x, int y, int screenX, int screenY) {
            selectedX = x;
            selectedY = y;
        }
    }
}
