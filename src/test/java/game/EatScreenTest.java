package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

class EatScreenTest {
    @Test
    void eatingAnInventoryItemAppliesItsFoodValueAndClosesTheScreen() {
        World world = GameTestSupport.floorWorld(3, 3, 1);
        Creature player = new Creature(world, '@', Color.WHITE, "player", 20, 1, 0);
        player.x = 1;
        player.y = 1;
        new CreatureAi(player);
        Item food = new Item('%', Color.YELLOW, "bread", null);
        food.modifyFoodValue(40);
        player.inventory().add(food);
        int initialFood = player.food();
        EatScreen screen = new EatScreen(player);

        ui.Screen result = screen.respondToUserInput(new KeyEvent(
                new java.awt.Canvas(), KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_A, 'a'));

        assertNull(result);
        assertEquals(initialFood + 40, player.food());
        assertNull(player.inventory().get(0));
    }

    @Test
    void rejectsItemsThatDoNotContainFood() {
        Creature player = new Creature(GameTestSupport.floorWorld(3, 3, 1),
                '@', Color.WHITE, "player", 20, 1, 0);
        new CreatureAi(player);
        player.inventory().add(new Item('!', Color.RED, "potion", null));
        EatScreen screen = new EatScreen(player);

        ui.Screen result = screen.respondToUserInput(new KeyEvent(
                new java.awt.Canvas(), KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_A, 'a'));

        assertSame(screen, result);
    }
}
