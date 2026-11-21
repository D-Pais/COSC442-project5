package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

class WorldBuilderTest {
    @Test
    void createsDeterministicMultiLevelCavesWithExitAndConnectingStairs() {
        World world = new WorldBuilder(90, 32, 5, new Random(42)).makeCaves().build();

        assertEquals(90, world.width());
        assertEquals(32, world.height());
        assertEquals(5, world.depth());
        boolean hasExit = false;
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                assertNotNull(world.tile(x, y, 0));
                hasExit |= world.tile(x, y, 0) == Tile.STAIRS_UP;
            }
        }
        assertTrue(hasExit);
        for (int z = 0; z < world.depth() - 1; z++) {
            assertTrue(hasTileAtDepth(world, Tile.STAIRS_DOWN, z));
            assertTrue(hasTileAtDepth(world, Tile.STAIRS_UP, z + 1));
        }
    }

    private static boolean hasTileAtDepth(World world, Tile tile, int depth) {
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                if (world.tile(x, y, depth) == tile) {
                    return true;
                }
            }
        }
        return false;
    }
}
