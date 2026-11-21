package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

class BatAiTest {
    @Test
    void takesTwoWanderingStepsDuringEachUpdate() {
        Random alwaysPositiveStep = new Random(1) {
            private static final long serialVersionUID = 1L;

            @Override
            public int nextInt(int bound) {
                return bound - 1;
            }
        };
        World world = GameTestSupport.floorWorld(6, 6, 1, alwaysPositiveStep);
        Creature bat = new Creature(world, 'b', java.awt.Color.WHITE, "bat", 10, 2, 0);
        bat.x = 1;
        bat.y = 1;
        new BatAi(bat);
        world.add(bat);

        bat.update();

        assertEquals(3, bat.x);
        assertEquals(3, bat.y);
    }
}
