package game;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class GoblinAiTest {
    @Test
    void equipsBetterWeaponBeforeTakingAnotherAction() {
        World world = GameTestSupport.floorWorld(10, 10, 1);
        Creature player = GameTestSupport.creature(world, 8, 8, 0, '@', "player");
        Creature goblin = new Creature(world, 'g', Color.GREEN, "goblin", 50, 5, 1);
        goblin.x = 1;
        goblin.y = 1;
        new GoblinAi(goblin, player);
        world.add(goblin);
        Item sword = new Item(')', Color.WHITE, "sword", null);
        sword.modifyAttackValue(10);
        goblin.inventory().add(sword);

        goblin.update();

        assertSame(sword, goblin.weapon());
    }
}
