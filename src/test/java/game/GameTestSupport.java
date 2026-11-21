package game;

import java.awt.Color;
import java.util.Random;

final class GameTestSupport {
    private GameTestSupport() {
    }

    static World floorWorld(int width, int height, int depth) {
        return floorWorld(width, height, depth, new Random(1));
    }

    static World floorWorld(int width, int height, int depth, Random random) {
        Tile[][][] tiles = new Tile[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    tiles[x][y][z] = Tile.FLOOR;
                }
            }
        }
        return new World(tiles, random);
    }

    static Creature creature(World world, int x, int y, int z, char glyph, String name) {
        Creature creature = new Creature(world, glyph, Color.WHITE, name, 100, 10, 0);
        creature.x = x;
        creature.y = y;
        creature.z = z;
        new CreatureAi(creature);
        world.add(creature);
        return creature;
    }
}
