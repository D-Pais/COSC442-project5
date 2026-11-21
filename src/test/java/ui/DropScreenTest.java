package ui;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;

class DropScreenTest {
    @Test
    void dropsTheSelectedItemAndCloses() {
        game.World world = UiTestSupport.floorWorld(5, 5);
        Creature player = UiTestSupport.creature(world, 2, 2, '@', "player");
        Item item = new Item('a', Color.WHITE, "rock", null);
        player.inventory().add(item);

        assertNull(new DropScreen(player).respondToUserInput(UiTestSupport.key(
                java.awt.event.KeyEvent.VK_A, 'a')));
        assertNull(player.inventory().get(0));
        boolean found = false;
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                found |= world.item(x, y, 0) == item;
            }
        }
        org.junit.jupiter.api.Assertions.assertTrue(found);
    }

    @Test
    void cancelsWithoutDroppingOnEscape() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        Item item = new Item('a', Color.WHITE, "rock", null);
        player.inventory().add(item);
        DropScreen screen = new DropScreen(player);

        assertNull(screen.respondToUserInput(UiTestSupport.key(
                java.awt.event.KeyEvent.VK_ESCAPE, (char) 27)));
        assertSame(item, player.inventory().get(0));
    }
}
