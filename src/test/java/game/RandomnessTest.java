package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.util.Random;

import org.junit.jupiter.api.Test;

class RandomnessTest {
    @Test
    void seededWorldsPlaceItemsAtTheSameLocations() {
        World firstWorld = new World(floorTiles(5, 5, 1), new Random(42));
        World secondWorld = new World(floorTiles(5, 5, 1), new Random(42));
        Item firstItem = new Item('!', Color.WHITE, "potion", null);
        Item secondItem = new Item('!', Color.WHITE, "potion", null);

        firstWorld.addAtEmptyLocation(firstItem, 0);
        secondWorld.addAtEmptyLocation(secondItem, 0);

        assertEquals(itemPosition(firstWorld, firstItem), itemPosition(secondWorld, secondItem));
    }

    @Test
    void combatUsesTheWorldRandomSourceForDamage() {
        long seed = 42;
        World world = new World(floorTiles(5, 5, 1), new Random(seed));
        Creature attacker = new Creature(world, '@', Color.WHITE, "player", 100, 8, 0);
        Creature defender = new Creature(world, 'g', Color.GREEN, "goblin", 100, 0, 5);
        attacker.x = 1;
        attacker.y = 1;
        defender.x = 2;
        defender.y = 1;
        new CreatureAi(attacker);
        new CreatureAi(defender);
        world.add(attacker);
        world.add(defender);

        int expectedDamage = new Random(seed).nextInt(3) + 1;
        attacker.meleeAttack(defender);

        assertEquals(100 - expectedDamage, defender.hp());
    }

    @Test
    void zeroEffectiveAttackDamageStillDealsOnePoint() {
        World world = new World(floorTiles(3, 3, 1), new Random(42));
        Creature attacker = new Creature(world, '@', Color.WHITE, "player", 100, 0, 0);
        Creature defender = new Creature(world, 'g', Color.GREEN, "goblin", 100, 0, 5);
        attacker.x = 1;
        attacker.y = 1;
        defender.x = 2;
        defender.y = 1;
        new CreatureAi(attacker);
        new CreatureAi(defender);
        world.add(attacker);
        world.add(defender);

        attacker.meleeAttack(defender);

        assertEquals(99, defender.hp());
    }

    @Test
    void seededCaveBuildersProduceTheSameMap() {
        World firstWorld = new WorldBuilder(90, 32, 5, new Random(42))
                .makeCaves()
                .build();
        World secondWorld = new WorldBuilder(90, 32, 5, new Random(42))
                .makeCaves()
                .build();

        for (int x = 0; x < firstWorld.width(); x++) {
            for (int y = 0; y < firstWorld.height(); y++) {
                for (int z = 0; z < firstWorld.depth(); z++) {
                    assertEquals(firstWorld.tile(x, y, z), secondWorld.tile(x, y, z));
                }
            }
        }
    }

    private static Tile[][][] floorTiles(int width, int height, int depth) {
        Tile[][][] tiles = new Tile[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    tiles[x][y][z] = Tile.FLOOR;
                }
            }
        }
        return tiles;
    }

    private static String itemPosition(World world, Item item) {
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                for (int z = 0; z < world.depth(); z++) {
                    if (world.item(x, y, z) == item)
                        return x + "," + y + "," + z;
                }
            }
        }
        throw new AssertionError("Item was not placed in the world");
    }
}
