package ui;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;

class InventoryBasedScreenTest {
    @Test
    void usesOnlyAcceptableOccupiedSlotsAndEscapeCancels() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        Item accepted = new Item('a', Color.WHITE, "acceptable", null);
        Item rejected = new Item('b', Color.WHITE, "rejected", null);
        player.inventory().add(accepted);
        player.inventory().add(rejected);
        ProbeScreen screen = new ProbeScreen(player);

        assertNull(screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_A, 'a')));
        assertSame(accepted, screen.used);
        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_B, 'b')));
        assertNull(screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_ESCAPE, (char) 27)));
    }

    private static final class ProbeScreen extends InventoryBasedScreen {
        private Item used;

        ProbeScreen(Creature player) {
            super(player);
        }

        @Override
        protected String getVerb() {
            return "select";
        }

        @Override
        protected boolean isAcceptable(Item item) {
            return item.name().equals("acceptable");
        }

        @Override
        protected Screen use(Item item) {
            used = item;
            return null;
        }
    }
}
