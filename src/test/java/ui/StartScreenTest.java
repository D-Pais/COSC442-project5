package ui;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

class StartScreenTest {
    @Test
    void waitsForEnterAndStartsPlay() {
        StartScreen screen = new StartScreen();

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_SPACE, ' ')));
        assertInstanceOf(PlayScreen.class,
                screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_ENTER, '\n')));
    }
}
