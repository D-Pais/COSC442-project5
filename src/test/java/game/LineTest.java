package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class LineTest {
    @Test
    void includesBothEndpointsAndMovesAlongHorizontalAndVerticalLines() {
        List<Point> horizontal = new Line(1, 2, 4, 2).getPoints();
        List<Point> vertical = new Line(3, 1, 3, 4).getPoints();

        assertEquals(4, horizontal.size());
        assertEquals(List.of(1, 2, 3, 4), horizontal.stream().map(point -> point.x).collect(Collectors.toList()));
        assertTrue(horizontal.stream().allMatch(point -> point.y == 2));
        assertEquals(4, vertical.size());
        assertTrue(vertical.stream().allMatch(point -> point.x == 3));
        assertEquals(List.of(1, 2, 3, 4), vertical.stream().map(point -> point.y).collect(Collectors.toList()));
    }

    @Test
    void handlesReverseDiagonalAndSinglePointLines() {
        List<Point> reverse = new Line(3, 3, 0, 0).getPoints();
        List<Point> single = new Line(2, 5, 2, 5).getPoints();

        assertEquals(4, reverse.size());
        assertEquals(new Point(3, 3, 0), reverse.get(0));
        assertEquals(new Point(0, 0, 0), reverse.get(reverse.size() - 1));
        assertEquals(List.of(new Point(2, 5, 0)), single);
    }
}
