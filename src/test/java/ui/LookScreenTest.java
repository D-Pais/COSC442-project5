package ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;

class LookScreenTest {
    @Test
    void updatesCaptionForVisibleCreatureAndItem() {
        game.World world = UiTestSupport.floorWorld(5, 5);
        Creature player = UiTestSupport.creature(world, 1, 1, '@', "player");
        Creature target = UiTestSupport.creature(world, 2, 1, 'g', "goblin");
        LookScreen screen = new LookScreen(player, "Looking", 0, 0);

        screen.enterWorldCoordinate(2, 1, 1, 1);
        assertTrue(screen.caption.startsWith("g goblin"));

        world.remove(target);
        Item item = new Item('!', Color.RED, "health potion", "red potion");
        world.addAtEmptySpace(item, 2, 1, 0);
        screen.enterWorldCoordinate(2, 1, 1, 1);
        assertTrue(screen.caption.startsWith("! red potion"));
    }
}
