package ui;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

class ScreenTest {
    @Test
    void screenContractAcceptsOutputAndInputImplementations() {
        Screen screen = new Screen() {
            @Override
            public void displayOutput(asciiPanel.AsciiPanel terminal) {
            }

            @Override
            public Screen respondToUserInput(KeyEvent key) {
                return null;
            }
        };

        screen.displayOutput(new asciiPanel.AsciiPanel());
        assertNull(screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_ESCAPE, (char) 27)));
    }
}
