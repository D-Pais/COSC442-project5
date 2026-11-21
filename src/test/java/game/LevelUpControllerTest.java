package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.List;

import org.junit.jupiter.api.Test;

class LevelUpControllerTest {
    @Test
    void exposesNamedOptionsAndReturnsNullForUnknownNames() {
        LevelUpController controller = new LevelUpController();
        List<String> options = controller.getLevelUpOptions();

        assertEquals(7, options.size());
        for (String option : options) {
            assertNotNull(controller.getLevelUpOption(option));
        }
        assertNull(controller.getLevelUpOption("not an upgrade"));
    }

    @Test
    void everyConfiguredOptionChangesItsAssociatedCreatureAttribute() {
        LevelUpController controller = new LevelUpController();
        for (String name : controller.getLevelUpOptions()) {
            Creature creature = new Creature(GameTestSupport.floorWorld(2, 2, 1),
                    '@', Color.WHITE, "player", 20, 2, 0);
            new CreatureAi(creature);
            int hp = creature.maxHp();
            int mana = creature.maxMana();
            int attack = creature.attackValue();
            int defense = creature.defenseValue();
            int vision = creature.visionRadius();

            controller.getLevelUpOption(name).invoke(creature);

            boolean changed = creature.maxHp() != hp
                    || creature.maxMana() != mana
                    || creature.attackValue() != attack
                    || creature.defenseValue() != defense
                    || creature.visionRadius() != vision;
            if (name.equals("Increased hp regeneration")) {
                creature.modifyHp(-1, "test damage");
                for (int i = 0; i < 50; i++) {
                    creature.update();
                }
                changed = creature.hp() == creature.maxHp();
            } else if (name.equals("Increased mana regeneration")) {
                creature.modifyMana(-1);
                for (int i = 0; i < 35; i++) {
                    creature.update();
                }
                changed = creature.mana() == creature.maxMana();
            }
            assertTrue(changed, name);
        }
    }

    @Test
    void autoLevelUpAppliesOneValidUpgrade() {
        Creature creature = new Creature(GameTestSupport.floorWorld(2, 2, 1),
                '@', Color.WHITE, "player", 20, 2, 0);
        new CreatureAi(creature);
        int hp = creature.maxHp();
        int mana = creature.maxMana();
        int attack = creature.attackValue();
        int defense = creature.defenseValue();
        int vision = creature.visionRadius();

        new LevelUpController().autoLevelUp(creature);

        assertTrue(creature.maxHp() != hp
                || creature.maxMana() != mana
                || creature.attackValue() != attack
                || creature.defenseValue() != defense
                || creature.visionRadius() != vision);
    }
}
