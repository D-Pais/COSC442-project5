package game;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

class ApplicationMainTest {
    @Test
    void constructsAndHandlesKeyEventsWhenGraphicsAreAvailable() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Swing window test requires a display");

        ApplicationMain app = new ApplicationMain();
        try {
            assertDoesNotThrow(() -> app.keyPressed(new KeyEvent(
                    app, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER, '\n')));
            assertDoesNotThrow(() -> app.keyReleased(new KeyEvent(
                    app, KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_A, 'a')));
            assertDoesNotThrow(() -> app.keyTyped(new KeyEvent(
                    app, KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_UNDEFINED, 'a')));
        } finally {
            app.dispose();
        }
    }
}
