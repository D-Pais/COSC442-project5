package game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TileTest {
    @Test
    void classifiesGroundAndDiggableTiles() {
        assertTrue(Tile.FLOOR.isGround());
        assertTrue(Tile.STAIRS_UP.isGround());
        assertTrue(Tile.STAIRS_DOWN.isGround());
        assertTrue(Tile.UNKNOWN.isGround());
        assertFalse(Tile.WALL.isGround());
        assertFalse(Tile.BOUNDS.isGround());
        assertTrue(Tile.WALL.isDiggable());
        assertFalse(Tile.FLOOR.isDiggable());
    }

    @Test
    void providesDisplayDetailsForEachTile() {
        for (Tile tile : Tile.values()) {
            assertNotNull(tile.color());
            assertNotNull(tile.details());
        }
    }
}
