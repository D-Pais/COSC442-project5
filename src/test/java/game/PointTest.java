package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

class PointTest {
    @Test
    void equalityAndHashCodeUseAllCoordinates() {
        Point point = new Point(2, 4, 1);
        Point equal = new Point(2, 4, 1);

        assertEquals(point, equal);
        assertEquals(point.hashCode(), equal.hashCode());
        assertNotEquals(point, new Point(2, 5, 1));
        assertNotEquals(point, new Point(2, 4, 0));
        assertTrue(new HashSet<>(List.of(point, equal)).contains(point));
    }

    @Test
    void returnsAllEightAdjacentPointsOnTheSameDepth() {
        List<Point> neighbors = new Point(3, 5, 2).neighbors8(new Random(5));

        assertEquals(8, neighbors.size());
        assertEquals(8, new HashSet<>(neighbors).size());
        assertTrue(neighbors.stream().allMatch(point -> point.z == 2));
        assertTrue(neighbors.stream().noneMatch(point -> point.x == 3 && point.y == 5));
    }
}
