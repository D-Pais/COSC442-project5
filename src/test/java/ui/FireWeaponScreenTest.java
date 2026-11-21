package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;
import game.Tile;

class FireWeaponScreenTest {
    @Test
    void acceptsVisibleClearLineAndRejectsBlockedLine() {
        Tile[][][] tiles = floorTiles(7, 5);
        tiles[3][2][0] = Tile.WALL;
        game.World world = new game.World(tiles);
        Creature player = UiTestSupport.creature(world, 1, 2, '@', "player");
        Item bow = new Item(')', Color.WHITE, "bow", null);
        bow.modifyRangedAttackValue(5);
        player.inventory().add(bow);
        player.equip(bow);
        FireWeaponScreen screen = new FireWeaponScreen(player, 0, 0);

        assertTrue(screen.isAcceptable(2, 2));
        assertFalse(screen.isAcceptable(4, 2));
    }

    @Test
    void firingAtVisibleCreatureReducesItsHealth() {
        game.World world = UiTestSupport.floorWorld(7, 5);
        Creature player = UiTestSupport.creature(world, 1, 2, '@', "player");
        Creature target = UiTestSupport.creature(world, 4, 2, 'g', "goblin");
        Item bow = new Item(')', Color.WHITE, "bow", null);
        bow.modifyRangedAttackValue(5);
        player.inventory().add(bow);
        player.equip(bow);
        FireWeaponScreen screen = new FireWeaponScreen(player, 0, 0);
        int initialHp = target.hp();
        int initialFood = player.food();

        screen.selectWorldCoordinate(4, 2, 4, 2);

        assertTrue(target.hp() < initialHp);
        assertEquals(initialFood - 2, player.food());
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
