package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FieldOfViewTest {
    @Test
    void tracksVisibilityByRadiusAndDepthAndRemembersSeenTiles() {
        World world = GameTestSupport.floorWorld(7, 7, 2);
        FieldOfView fov = new FieldOfView(world);

        assertEquals(Tile.UNKNOWN, fov.tile(3, 3, 0));
        fov.update(3, 3, 0, 2);

        assertTrue(fov.isVisible(3, 3, 0));
        assertTrue(fov.isVisible(4, 3, 0));
        assertFalse(fov.isVisible(5, 3, 0));
        assertFalse(fov.isVisible(3, 3, 1));
        assertEquals(Tile.FLOOR, fov.tile(4, 3, 0));
        assertEquals(Tile.UNKNOWN, fov.tile(3, 3, 1));
    }

    @Test
    void keepsWallVisibleButDoesNotRevealTheTileDirectlyBehindIt() {
        Tile[][][] tiles = new Tile[7][5][1];
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 5; y++) {
                tiles[x][y][0] = x == 3 && y == 2 ? Tile.WALL : Tile.FLOOR;
            }
        }
        fovForWorld(new World(tiles));
    }

    private static void fovForWorld(World world) {
        FieldOfView fov = new FieldOfView(world);
        fov.update(2, 2, 0, 4);

        assertTrue(fov.isVisible(3, 2, 0));
        assertEquals(Tile.WALL, fov.tile(3, 2, 0));
        assertFalse(fov.isVisible(4, 2, 0));
    }
}
