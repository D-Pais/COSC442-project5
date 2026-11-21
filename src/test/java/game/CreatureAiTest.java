package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

class CreatureAiTest {
    @Test
    void entersGroundButDoesNotEnterAWall() {
        Tile[][][] tiles = floorTiles(5, 5);
        tiles[2][1][0] = Tile.WALL;
        World world = new World(tiles);
        Creature creature = new Creature(world, 'g', Color.WHITE, "goblin", 10, 1, 0);
        creature.x = 1;
        creature.y = 1;
        CreatureAi ai = new CreatureAi(creature);

        ai.onEnter(2, 1, 0, Tile.FLOOR);
        assertEquals(2, creature.x);
        ai.onEnter(2, 1, 0, Tile.WALL);
        assertEquals(2, creature.x);
    }

    @Test
    void checksDepthRangeAndLineOfSight() {
        Tile[][][] tiles = floorTiles(7, 5);
        tiles[3][2][0] = Tile.WALL;
        World world = new World(tiles);
        Creature creature = GameTestSupport.creature(world, 1, 2, 0, 'g', "goblin");
        CreatureAi ai = creatureAi(creature);

        assertTrue(ai.canSee(2, 2, 0));
        assertFalse(ai.canSee(4, 2, 0));
        assertFalse(ai.canSee(1, 2, 1));
        assertFalse(ai.canSee(7, 2, 0));
    }

    @Test
    void remembersUnknownTilesAndLearnsItemNames() {
        Creature creature = GameTestSupport.creature(GameTestSupport.floorWorld(3, 3, 1), 1, 1, 0, 'g', "goblin");
        CreatureAi ai = creatureAi(creature);
        Item item = new Item('!', Color.WHITE, "health potion", "red potion");

        assertEquals(Tile.UNKNOWN, ai.rememberedTile(0, 0, 0));
        assertEquals("red potion", ai.getName(item));
        ai.setName(item, "health potion");
        assertEquals("health potion", ai.getName(item));
    }

    @Test
    void huntsTowardTargetUsingTheFirstPathStep() {
        World world = GameTestSupport.floorWorld(6, 3, 1);
        Creature hunter = GameTestSupport.creature(world, 1, 1, 0, 'g', "goblin");
        Creature target = GameTestSupport.creature(world, 4, 1, 0, '@', "player");
        CreatureAi ai = creatureAi(hunter);

        ai.hunt(target);

        assertEquals(2, hunter.x);
        assertEquals(1, hunter.y);
    }

    private static CreatureAi creatureAi(Creature creature) {
        return new CreatureAi(creature);
    }

    private static Tile[][][] floorTiles(int width, int height) {
        Tile[][][] tiles = new Tile[width][height][1];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y][0] = Tile.FLOOR;
            }
        }
        return tiles;
    }
}
