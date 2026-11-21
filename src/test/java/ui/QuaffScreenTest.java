package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Effect;
import game.Item;

class QuaffScreenTest {
    @Test
    void consumesAnItemWithAQuaffEffectAndRejectsItemsWithoutOne() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        Item potion = new Item('!', Color.RED, "potion", null);
        potion.setQuaffEffect(new Effect(2) {
            @Override
            public void start(Creature creature) {
                creature.modifyHp(5, "test potion");
            }
        });
        player.modifyHp(-10, "test damage");
        player.inventory().add(potion);
        player.inventory().add(new Item('?', Color.WHITE, "empty bottle", null));
        QuaffScreen screen = new QuaffScreen(player);

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_B, 'b')));
        assertNull(screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_A, 'a')));
        assertEquals(95, player.hp());
        assertNull(player.inventory().get(0));
    }
}
