package ui;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Effect;
import game.Item;

class ReadSpellScreenTest {
    @Test
    void selectingAListedSpellOpensItsTargetingScreen() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        Item book = new Item('+', Color.WHITE, "spellbook", null);
        book.addWrittenSpell("heal", 2, new Effect(1));
        player.inventory().add(book);
        ReadSpellScreen screen = new ReadSpellScreen(player, 2, 3, book);

        assertInstanceOf(CastSpellScreen.class,
                screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_A, 'a')));
    }

    @Test
    void rejectsLettersOutsideTheSpellbookAndSupportsCancel() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        Item book = new Item('+', Color.WHITE, "spellbook", null);
        book.addWrittenSpell("heal", 2, new Effect(1));
        player.inventory().add(book);
        ReadSpellScreen screen = new ReadSpellScreen(player, 2, 3, book);

        assertSame(screen, screen.respondToUserInput(UiTestSupport.key(KeyEvent.VK_B, 'b')));
        org.junit.jupiter.api.Assertions.assertNull(screen.respondToUserInput(
                UiTestSupport.key(KeyEvent.VK_ESCAPE, (char) 27)));
    }
}
