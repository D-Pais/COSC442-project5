package ui;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class HelpScreenTest {
    @Test
    void dismissesOnAnyKey() {
        assertNull(new HelpScreen().respondToUserInput(UiTestSupport.key(
                java.awt.event.KeyEvent.VK_A, 'a')));
    }
}
