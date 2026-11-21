package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class PathTest {
    @Test
    void exposesRouteFromCreatureTowardDestinationWithoutIncludingStart() {
        World world = GameTestSupport.floorWorld(5, 3, 1);
        Creature creature = GameTestSupport.creature(world, 0, 1, 0, 'g', "goblin");

        Path path = new Path(creature, 3, 1);

        assertFalse(path.points().isEmpty());
        assertEquals(new Point(1, 1, 0), path.points().get(0));
        assertEquals(new Point(3, 1, 0), path.points().get(path.points().size() - 1));
    }
}
