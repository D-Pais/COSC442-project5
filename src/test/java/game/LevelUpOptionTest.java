package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class LevelUpOptionTest {
    @Test
    void storesItsNameAndInvokesItsDefinedUpgrade() {
        LevelUpOption option = new LevelUpOption("Test upgrade") {
            @Override
            public void invoke(Creature creature) {
                creature.modifyAttackValue(3);
            }
        };
        Creature creature = new Creature(GameTestSupport.floorWorld(2, 2, 1),
                '@', Color.WHITE, "player", 20, 2, 0);

        option.invoke(creature);

        assertEquals("Test upgrade", option.name());
        assertEquals(5, creature.attackValue());
    }
}
