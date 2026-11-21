package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class PlayerAiTest {
    @Test
    void reportsMessagesAndUsesTheFieldOfView() {
        World world = GameTestSupport.floorWorld(5, 5, 1);
        FieldOfView fov = new FieldOfView(world);
        List<String> messages = new ArrayList<>();
        Creature player = new Creature(world, '@', Color.WHITE, "player", 20, 2, 0);
        player.x = 2;
        player.y = 2;
        new PlayerAi(player, messages, fov);
        fov.update(2, 2, 0, 2);

        player.notify("Hello %s.", "world");

        assertEquals(List.of("Hello world."), messages);
        assertTrue(player.canSee(2, 2, 0));
        assertFalse(player.canSee(4, 2, 0));
        assertEquals(Tile.UNKNOWN, player.tile(4, 2, 0));
    }

    @Test
    void digsAWallInsteadOfEnteringIt() {
        Tile[][][] tiles = new Tile[4][3][1];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y][0] = Tile.FLOOR;
            }
        }
        tiles[2][1][0] = Tile.WALL;
        World world = new World(tiles);
        Creature player = new Creature(world, '@', Color.WHITE, "player", 20, 2, 0);
        player.x = 1;
        player.y = 1;
        new PlayerAi(player, new ArrayList<>(), new FieldOfView(world));

        player.moveBy(1, 0, 0);

        assertEquals(Tile.FLOOR, world.tile(2, 1, 0));
        assertEquals(1, player.x);
        assertEquals(1, player.y);
    }
}
