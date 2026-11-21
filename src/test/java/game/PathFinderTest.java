package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class PathFinderTest {
    @Test
    void findsAPathAroundAnImpassableTile() {
        Tile[][][] tiles = new Tile[5][3][1];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y][0] = x == 2 && y == 1 ? Tile.WALL : Tile.FLOOR;
            }
        }
        World world = new World(tiles);
        Creature creature = GameTestSupport.creature(world, 0, 1, 0, 'g', "goblin");

        java.util.List<Point> path = new PathFinder().findPath(
                creature, new Point(0, 1, 0), new Point(4, 1, 0), 100);

        assertFalse(path.isEmpty());
        assertEquals(new Point(4, 1, 0), path.get(path.size() - 1));
        assertFalse(path.contains(new Point(2, 1, 0)));
    }

    @Test
    void returnsEmptyPathForSameStartAndEndAndNullWhenTrapped() {
        World open = GameTestSupport.floorWorld(3, 3, 1);
        Creature creature = GameTestSupport.creature(open, 1, 1, 0, 'g', "goblin");
        PathFinder finder = new PathFinder();
        assertEquals(java.util.Collections.emptyList(), finder.findPath(
                creature, new Point(1, 1, 0), new Point(1, 1, 0), 10));

        Tile[][][] tiles = new Tile[5][5][1];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                tiles[x][y][0] = Tile.WALL;
            }
        }
        tiles[0][0][0] = Tile.FLOOR;
        tiles[4][4][0] = Tile.FLOOR;
        World trapped = new World(tiles);
        Creature trappedCreature = GameTestSupport.creature(trapped, 0, 0, 0, 'g', "goblin");

        assertNull(finder.findPath(trappedCreature, new Point(0, 0, 0), new Point(4, 4, 0), 20));
    }

}
