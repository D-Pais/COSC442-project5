package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class WorldTest {
    @Test
    void reportsDimensionsAndBoundsAndDigsOnlyWalls() {
        Tile[][][] tiles = new Tile[3][4][2];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 2; z++) {
                    tiles[x][y][z] = Tile.FLOOR;
                }
            }
        }
        tiles[1][1][0] = Tile.WALL;
        World world = new World(tiles);

        assertEquals(3, world.width());
        assertEquals(4, world.height());
        assertEquals(2, world.depth());
        assertEquals(Tile.BOUNDS, world.tile(-1, 0, 0));
        world.dig(1, 1, 0);
        assertEquals(Tile.FLOOR, world.tile(1, 1, 0));
        world.dig(-1, 0, 0);
        assertEquals(Tile.BOUNDS, world.tile(-1, 0, 0));
    }

    @Test
    void rendersCreatureBeforeItemBeforeTileAndSupportsRemoval() {
        World world = GameTestSupport.floorWorld(3, 3, 1);
        Item item = new Item('!', Color.RED, "potion", null);
        Creature creature = new Creature(world, '@', Color.WHITE, "player", 10, 1, 0);
        creature.x = 1;
        creature.y = 1;
        world.addAtEmptySpace(item, 1, 1, 0);
        assertEquals('!', world.glyph(1, 1, 0));
        assertEquals(Color.RED, world.color(1, 1, 0));

        world.add(creature);
        assertEquals('@', world.glyph(1, 1, 0));
        assertSame(Color.WHITE, world.color(1, 1, 0));
        world.remove(creature);
        world.remove(item);
        assertNull(world.creature(1, 1, 0));
        assertNull(world.item(1, 1, 0));
    }

    @Test
    void placesItemsInNearbyEmptySpaceAndHandlesNullItems() {
        World world = GameTestSupport.floorWorld(3, 3, 1);
        Item first = new Item('a', Color.WHITE, "first", null);
        Item second = new Item('b', Color.WHITE, "second", null);

        assertTrue(world.addAtEmptySpace(first, 1, 1, 0));
        assertTrue(world.addAtEmptySpace(second, 1, 1, 0));
        assertTrue(world.addAtEmptySpace(null, 1, 1, 0));
        assertFalse(world.item(1, 1, 0) == second);
        world.remove(second);
        boolean foundSecond = false;
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                foundSecond |= world.item(x, y, 0) == second;
            }
        }
        assertFalse(foundSecond);
    }
}
