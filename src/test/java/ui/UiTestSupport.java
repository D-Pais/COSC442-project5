package ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.Creature;
import game.CreatureAi;
import game.Tile;
import game.World;

final class UiTestSupport {
    private UiTestSupport() {
    }

    static World floorWorld(int width, int height) {
        Tile[][][] tiles = new Tile[width][height][1];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y][0] = Tile.FLOOR;
            }
        }
        return new World(tiles, new Random(7));
    }

    static Creature creature(World world, int x, int y, char glyph, String name) {
        Creature creature = new Creature(world, glyph, Color.WHITE, name, 100, 10, 0);
        creature.x = x;
        creature.y = y;
        new CreatureAi(creature);
        world.add(creature);
        return creature;
    }

    static RecordingAi recordingAi(Creature creature) {
        return new RecordingAi(creature);
    }

    static KeyEvent key(int code, char character) {
        return new KeyEvent(new java.awt.Canvas(), KeyEvent.KEY_PRESSED, 0, 0, code, character);
    }

    static final class RecordingAi extends CreatureAi {
        private final List<String> notifications = new ArrayList<>();

        RecordingAi(Creature creature) {
            super(creature);
        }

        @Override
        public void onNotify(String message) {
            notifications.add(message);
        }

        List<String> notifications() {
            return notifications;
        }
    }
}
