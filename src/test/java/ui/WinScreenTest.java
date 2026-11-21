package ui;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

class WinScreenTest {
    @Test
    void preservesScreenUntilEnterRestarts() {
        WinScreen screen = new WinScreen();

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_X, 'x')));
        assertInstanceOf(PlayScreen.class,
                screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_ENTER, '\n')));
    }
}
