package ui;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;

class ThrowScreenTest {
    @Test
    void selectingAnInventoryItemOpensItsTargetingScreen() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(5, 5), 2, 2, '@', "player");
        player.inventory().add(new Item(',', Color.WHITE, "rock", null));

        Screen result = new ThrowScreen(player, 10, 10).respondToUserInput(
                UiTestSupport.key(KeyEvent.VK_A, 'a'));

        assertInstanceOf(ThrowAtScreen.class, result);
    }
}
