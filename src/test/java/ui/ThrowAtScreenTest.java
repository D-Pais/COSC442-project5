package ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import game.Creature;
import game.Item;
import game.Tile;

class ThrowAtScreenTest {
    @Test
    void checksClearLineAndThrowingAtCreatureDealsDamageAndUsesItem() {
        game.World world = UiTestSupport.floorWorld(7, 5);
        Creature player = UiTestSupport.creature(world, 1, 2, '@', "player");
        Creature target = UiTestSupport.creature(world, 4, 2, 'g', "goblin");
        Item rock = new Item(',', Color.WHITE, "rock", null);
        rock.modifyThrownAttackValue(5);
        player.inventory().add(rock);
        ThrowAtScreen screen = new ThrowAtScreen(player, 0, 0, rock);
        int initialHp = target.hp();

        assertTrue(screen.isAcceptable(4, 2));
        screen.selectWorldCoordinate(4, 2, 4, 2);

        assertTrue(target.hp() < initialHp);
        assertFalse(player.inventory().contains(rock));
        assertTrue(worldContains(world, rock));
    }

    @Test
    void rejectsTargetsBeyondAWall() {
        Tile[][][] tiles = new Tile[7][5][1];
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 5; y++) {
                tiles[x][y][0] = x == 3 && y == 2 ? Tile.WALL : Tile.FLOOR;
            }
        }
        game.World world = new game.World(tiles);
        Creature player = UiTestSupport.creature(world, 1, 2, '@', "player");

        assertFalse(new ThrowAtScreen(player, 0, 0,
                new Item(',', Color.WHITE, "rock", null)).isAcceptable(4, 2));
    }

    private static boolean worldContains(game.World world, Item item) {
        for (int x = 0; x < world.width(); x++) {
            for (int y = 0; y < world.height(); y++) {
                if (world.item(x, y, 0) == item) {
                    return true;
                }
            }
        }
        return false;
    }
}
