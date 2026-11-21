package game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.Random;

import org.junit.jupiter.api.Test;

class ZombieAiTest {
    @Test
    void huntsAndAttacksAVisibleAdjacentPlayerWhenNotSkippingItsTurn() {
        Random proceedRandom = new Random(1) {
            private static final long serialVersionUID = 1L;

            @Override
            public double nextDouble() {
                return 0.9;
            }
        };
        World world = GameTestSupport.floorWorld(5, 5, 1, proceedRandom);
        Creature player = new Creature(world, '@', Color.WHITE, "player", 100, 1, 0);
        Creature zombie = new Creature(world, 'z', Color.WHITE, "zombie", 50, 10, 0);
        player.x = 2;
        player.y = 2;
        zombie.x = 1;
        zombie.y = 2;
        new CreatureAi(player);
        new ZombieAi(zombie, player);
        world.add(player);
        world.add(zombie);

        zombie.update();

        assertTrue(player.hp() < player.maxHp());
    }

    @Test
    void skipsItsTurnWhenTheRandomRollIsBelowTheSkipChance() {
        Random skipRandom = new Random(1) {
            private static final long serialVersionUID = 1L;

            @Override
            public double nextDouble() {
                return 0.0;
            }
        };
        World world = GameTestSupport.floorWorld(5, 5, 1, skipRandom);
        Creature player = new Creature(world, '@', Color.WHITE, "player", 100, 1, 0);
        Creature zombie = new Creature(world, 'z', Color.WHITE, "zombie", 50, 10, 0);
        player.x = 2;
        player.y = 2;
        zombie.x = 1;
        zombie.y = 2;
        new CreatureAi(player);
        new ZombieAi(zombie, player);
        world.add(player);
        world.add(zombie);

        zombie.update();

        org.junit.jupiter.api.Assertions.assertEquals(100, player.hp());
        org.junit.jupiter.api.Assertions.assertEquals(1, zombie.x);
    }
}
