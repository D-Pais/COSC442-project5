package ui;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;

class ReadScreenTest {
    @Test
    void onlyOpensSpellbooksAndCancelsOnEscape() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        Item book = new Item('+', Color.WHITE, "spellbook", null);
        book.addWrittenSpell("heal", 2, new game.Effect(1));
        player.inventory().add(book);
        player.inventory().add(new Item('!', Color.RED, "potion", null));
        ReadScreen screen = new ReadScreen(player, 4, 5);

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_B, 'b')));
        assertInstanceOf(ReadSpellScreen.class,
                screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_A, 'a')));
        org.junit.jupiter.api.Assertions.assertNull(
                screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_ESCAPE, (char) 27)));
    }
}
