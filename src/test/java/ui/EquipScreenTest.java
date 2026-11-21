package ui;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;

class EquipScreenTest {
    @Test
    void equipsACombatItemAndRejectsNonEquipment() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        Item weapon = new Item(')', Color.WHITE, "dagger", null);
        weapon.modifyAttackValue(5);
        Item food = new Item('%', Color.YELLOW, "bread", null);
        player.inventory().add(weapon);
        player.inventory().add(food);
        EquipScreen screen = new EquipScreen(player);

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_B, 'b')));
        assertNull(screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_A, 'a')));
        assertSame(weapon, player.weapon());
        assertTrue(player.attackValue() >= 5);
    }
}
