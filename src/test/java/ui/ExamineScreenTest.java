package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Color;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;

class ExamineScreenTest {
    @Test
    void reportsTheItemNameWithTheCorrectArticleAndDetails() {
        Creature player = UiTestSupport.creature(UiTestSupport.floorWorld(3, 3), 1, 1, '@', "player");
        UiTestSupport.RecordingAi ai = UiTestSupport.recordingAi(player);
        Item apple = new Item('%', Color.RED, "apple", null);
        apple.modifyFoodValue(10);
        player.inventory().add(apple);

        Screen result = new ExamineScreen(player).respondToUserInput(
                UiTestSupport.key(KeyEvent.VK_A, 'a'));

        assertNull(result);
        assertEquals("It's an apple.  food:10", ai.notifications().get(0));
    }
}
