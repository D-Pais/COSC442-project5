package game;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Random;

import org.junit.jupiter.api.Test;

class FungusAiTest {
    @Test
    void spreadsToAnEmptyNearbyCellWhenTheRandomRollSucceeds() {
        Random spreadingRandom = new Random(1) {
            private static final long serialVersionUID = 1L;

            @Override
            public double nextDouble() {
                return 0.0;
            }

            @Override
            public int nextInt(int bound) {
                return bound == 11 ? 6 : 0;
            }
        };
        World world = GameTestSupport.floorWorld(8, 8, 1, spreadingRandom);
        StuffFactory factory = new StuffFactory(world);
        Creature fungus = new Creature(world, 'f', java.awt.Color.GREEN, "fungus", 10, 0, 0);
        fungus.x = 2;
        fungus.y = 2;
        new FungusAi(fungus, factory);
        world.add(fungus);

        fungus.update();

        assertNotNull(world.creature(3, 3, 0));
        assertNull(world.creature(2, 3, 0));
    }

    @Test
    void doesNotSpreadWhenRandomRollFails() {
        Random noSpreadRandom = new Random(1) {
            private static final long serialVersionUID = 1L;

            @Override
            public double nextDouble() {
                return 0.5;
            }
        };
        World world = GameTestSupport.floorWorld(5, 5, 1, noSpreadRandom);
        Creature fungus = new Creature(world, 'f', java.awt.Color.GREEN, "fungus", 10, 0, 0);
        fungus.x = 2;
        fungus.y = 2;
        new FungusAi(fungus, new StuffFactory(world));
        world.add(fungus);

        fungus.update();

        int creatureCount = 0;
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                if (world.creature(x, y, 0) != null) {
                    creatureCount++;
                }
            }
        }
        org.junit.jupiter.api.Assertions.assertEquals(1, creatureCount);
    }
}
